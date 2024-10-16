package org.todolist;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 處理任務資料檔案的讀取與寫入，支援加密、壓縮和平行處理。
 * 此類別使用 Guava 的快取機制來優化任務資料的讀取，並透過平行處理來提升大資料的處理效能。
 */

public class DataIO {

    private static final Logger LOGGER = LogManager.getLogger(DataIO.class);
    private static final String DATA_FILE = "Data.dat";
    private static final int INITIAL_CAPACITY = 1000;
    private static final int MAX_CHUNK_SIZE = 100;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int BUFFER_SIZE = 8192;
    private static volatile boolean isInitialized = false;
    private static final LoadingCache<String, Deque<TaskClass>> tasksCache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                @Override
                public Deque<TaskClass> load(String key) {
                    LOGGER.info("Loading tasks from disk...");
                    return readDataFileFromDisk();
                }
            });
    /**
     * 通過設定加密參數來初始化資料輸入/輸出系統。
     * 此方法是同步的，以確保初始化只發生一次。
     */

    private static synchronized void initialize() {
        if (!isInitialized) {
            LOGGER.info("Initializing data...");
            EncryptionService.initializeKeyAndIv();
            isInitialized = true;
        }
    }

    /**
     * 從磁碟讀取並解密資料，解壓縮後進行平行處理。
     *
     * @return 從資料檔案載入的 {@link TaskClass} 物件的雙端佇列（Deque）。
     */

    private static Deque<TaskClass> readDataFileFromDisk() {
        LOGGER.info("Reading data file from disk...");
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists() || dataFile.length() == 0) {
            return new ArrayDeque<>(0);
        }

        Deque<TaskClass> tasks = new ArrayDeque<>(0);
        try {
            LOGGER.info("Decrypting and decompressing data file...");
            byte[] decryptedData = decryptAndDecompress(dataFile);

            try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(decryptedData);
                 ObjectInputStream objIn = new ObjectInputStream(byteArrayIn)) {

                Deque<Object> allObjects = new ArrayDeque<>(INITIAL_CAPACITY);
                while (true) {
                    try {
                        allObjects.add(objIn.readObject());
                    } catch (EOFException e) {
                        break; // EOF reached
                    }
                }

                LOGGER.debug("Processing {} objects in parallel...", allObjects.size());
                tasks = processObjectsInParallel(allObjects);
                return tasks;
            }
        } catch (Exception e) {
            cleanUpOnError(tasks);
            throw new RuntimeException("Failed to read data file: ", e);
        }
    }

    /**
     * 在資料處理過程中發生錯誤時清理資源。
     *
     * @param deque 要清除的雙端佇列，如果不為 null。
     */

    private static void cleanUpOnError(Deque<?> deque) {
        LOGGER.error("An error occurred, clearing memory...");
        if (deque != null) {
            deque.clear();
        }
        tasksCache.invalidateAll();
        System.gc();
    }

    /**
     * 解密並解壓縮資料檔案。
     *
     * @param dataFile 要解密和解壓縮的檔案。
     * @return 解壓縮後的位元組陣列。
     * @throws Exception 如果在解密或解壓縮過程中發生任何錯誤。
     */

    private static byte[] decryptAndDecompress(File dataFile) throws Exception {
        LOGGER.info("Starting decryption and decompression of data file...");
        try (FileInputStream fileIn = new FileInputStream(dataFile);
             BufferedInputStream bufferedIn = new BufferedInputStream(fileIn, BUFFER_SIZE);
             CipherInputStream cipherIn = new CipherInputStream(bufferedIn, EncryptionService.getDecryptCipher());
             GZIPInputStream gzipIn = new GZIPInputStream(cipherIn);
             ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                byteArrayOut.write(buffer, 0, bytesRead);
            }
            LOGGER.info("Decryption and decompression completed.");
            return byteArrayOut.toByteArray();
        }
    }

    /**
     * 以平行區塊的方式處理物件，以提升效能。
     *
     * @param allObjects 要處理的所有物件的雙端佇列。
     * @return 以平行方式處理的 {@link TaskClass} 物件的雙端佇列。
     */

    private static Deque<TaskClass> processObjectsInParallel(Deque<Object> allObjects) {
        LOGGER.info("Starting parallel processing with {} threads...", THREAD_COUNT);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Deque<TaskClass>>> futures = new ArrayList<>(INITIAL_CAPACITY);

        int totalSize = allObjects.size();
        int adaptiveChunkSize = calculateAdaptiveChunkSize(totalSize);
        LOGGER.debug("Total objects: {}, Chunk size: {}", totalSize, adaptiveChunkSize);

        Iterator<Object> iterator = allObjects.iterator();
        int chunkCount = 0;
        while (iterator.hasNext()) {
            Deque<Object> chunk = new ArrayDeque<>(adaptiveChunkSize);
            for (int i = 0; i < adaptiveChunkSize && iterator.hasNext(); i++) {
                chunk.add(iterator.next());
            }
            LOGGER.debug("Submitting chunk {} to executor", ++chunkCount);
            int finalChunkCount = chunkCount;
            futures.add(executor.submit(() -> {
                LOGGER.debug("Processing chunk {} in {}", finalChunkCount, Thread.currentThread().getName());
                return processChunk(chunk);
            }));
        }

        Deque<TaskClass> result = new ConcurrentLinkedDeque<>();
        for (Future<Deque<TaskClass>> future : futures) {
            try {
                result.addAll(future.get());
            } catch (Exception e) {
                throw new RuntimeException("Error processing chunk", e);
            }
        }

        shutdown(executor);

        LOGGER.info("Parallel processing completed.");
        return result;
    }

    /**
     * 關閉執行器服務並等待其終止。
     *
     * @param executor 要關閉的執行器服務。
     */

    private static void shutdown(ExecutorService executor) {
        LOGGER.info("Shutting down executor service...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                LOGGER.warn("Executor did not terminate in the specified time.");
                executor.shutdownNow();
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    LOGGER.error("Executor did not terminate after being forcefully shut down.");
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("Shutdown interrupted", e);
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }

    /**
     * 處理一批物件並將其轉換為 {@link TaskClass} 實例。
     *
     * @param chunk 要處理的物件批次。
     * @return {@link TaskClass} 物件的雙端佇列。
     */

    private static Deque<TaskClass> processChunk(Deque<Object> chunk) {
        Deque<TaskClass> tasks = new ArrayDeque<>(INITIAL_CAPACITY);
        try {
            for (Object obj : chunk) {
                if (!(obj instanceof Deque<?> rawDeque)) {
                    throw new ClassCastException("Deserialized object is not of type Deque<TaskClass>");
                }
                for (Object item : rawDeque) {
                    if (item instanceof TaskClass task) {
                        tasks.add(task);
                    } else {
                        throw new ClassCastException("Element in Deque is not of type TaskClass. Actual type: " + item.getClass().getName());
                    }
                }
            }
        } finally {
            chunk.clear();
        }
        return tasks;
    }

    /**
     * 根據物件的總數和可用執行緒的數量計算自適應的區塊大小。
     *
     * @param totalSize 要處理的物件總數。
     * @return 計算出的區塊大小。
     */

    private static int calculateAdaptiveChunkSize(int totalSize) {
        int idealChunkCount = THREAD_COUNT << 1; // Each thread processes 2 chunks
        int idealChunkSize = Math.max(totalSize / idealChunkCount, 1);

        return Math.min(idealChunkSize, MAX_CHUNK_SIZE);
    }

    /**
     * 從快取中讀取資料，如果快取中不存在則從磁碟載入資料。
     *
     * @return 從快取或磁碟載入的 {@link TaskClass} 物件的雙端佇列。
     */

    public static Deque<TaskClass> readDataFile() {
        long startTime = System.currentTimeMillis();
        if (!isInitialized) {
            initialize();
        }

        try {
            return tasksCache.get("taskCache");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load tasks from cache", e);
        } finally {
            LOGGER.info("Time taken to load data: {} ms", (System.currentTimeMillis() - startTime));
        }
    }

    /**
     * 將任務資料以加密和壓縮的方式寫入檔案。
     *
     * @param tasks 要寫入資料檔案的 {@link TaskClass} 物件的雙端佇列。
     */

    public static void writeDataFile(Deque<TaskClass> tasks) {
        long startTime = System.currentTimeMillis();
        if (!isInitialized) {
            initialize();
        }

        int totalTasks = tasks.size();
        int numPartitions = Math.max(1, totalTasks / 1000);
        int chunkSize = (int) Math.ceil((double) totalTasks / numPartitions);

        LOGGER.info("Writing data file in {} chunks...", numPartitions);

        try (FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, BUFFER_SIZE);
             CipherOutputStream cipherOut = new CipherOutputStream(bufferedOut, EncryptionService.getEncryptCipher());
             GZIPOutputStream gzipOut = new GZIPOutputStream(cipherOut);
             ObjectOutputStream objOut = new ObjectOutputStream(gzipOut)) {

            LOGGER.info("Writing {} tasks to data file...", totalTasks);

            Iterator<TaskClass> iterator = tasks.iterator();
            Deque<TaskClass> chunk = new ArrayDeque<>(chunkSize);

            while (iterator.hasNext()) {
                while (iterator.hasNext() && chunk.size() < chunkSize) {
                    chunk.add(iterator.next());
                }
                objOut.writeObject(new ArrayDeque<>(chunk));
                chunk.clear();
            }

            tasksCache.put("taskCache", tasks);
            LOGGER.info("Data file written successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to write data file: ", e);
        } finally {
            LOGGER.info("Time taken to write data: {} ms", (System.currentTimeMillis() - startTime));
        }
    }




}
