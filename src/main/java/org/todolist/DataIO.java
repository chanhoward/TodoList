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
 * Handles reading and writing of task data files with support for encryption, compression, and parallel processing.
 * This class uses Guava's caching mechanisms to optimize the reading of task data and handles large data processing
 * in parallel to improve performance.
 */
public class DataIO {

    private static final Logger LOGGER = LogManager.getLogger(DataIO.class);
    private static final String DATA_FILE = "Data.dat";
    private static final int INITIAL_CAPACITY = 1000;
    private static final int MAX_CHUNK_SIZE = 100;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int BUFFER_SIZE = 8192;
    private static volatile boolean isInitialized = false;

    /**
     * Initializes the data IO system by setting up encryption parameters.
     * This method is synchronized to ensure that initialization happens only once.
     */
    private static synchronized void initialize() {
        if (!isInitialized) {
            LOGGER.info("Initializing data...");
            EncryptionService.initializeKeyAndIv();
            isInitialized = true;
        }
    }

    /**
     * Reads and decrypts data from the disk, decompresses it, and processes it in parallel.
     *
     * @return A deque of {@link TaskClass} objects loaded from the data file.
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
     * Cleans up resources in case of an error during data processing.
     *
     * @param deque The deque to clear, if not null.
     */
    private static void cleanUpOnError(Deque<?> deque) {
        LOGGER.error("An error occurred, clearing memory...");
        if (deque != null) {
            deque.clear();
        }
        tasksCache.invalidateAll();
        System.gc();
    }    private static final LoadingCache<String, Deque<TaskClass>> tasksCache = CacheBuilder.newBuilder()
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
     * Decrypts and decompresses the data file.
     *
     * @param dataFile The file to be decrypted and decompressed.
     * @return The decompressed byte array.
     * @throws Exception If any error occurs during decryption or decompression.
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
     * Processes objects in parallel chunks to improve performance.
     *
     * @param allObjects The deque of all objects to be processed.
     * @return A deque of {@link TaskClass} objects processed in parallel.
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
     * Shuts down the executor service and waits for termination.
     *
     * @param executor The executor service to shut down.
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
     * Processes a chunk of objects and converts them to {@link TaskClass} instances.
     *
     * @param chunk The chunk of objects to process.
     * @return A deque of {@link TaskClass} objects.
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
     * Calculates an adaptive chunk size based on the total number of objects and the number of available threads.
     *
     * @param totalSize The total number of objects to be processed.
     * @return The calculated chunk size.
     */
    private static int calculateAdaptiveChunkSize(int totalSize) {
        int idealChunkCount = THREAD_COUNT << 1; // Each thread processes 2 chunks
        int idealChunkSize = Math.max(totalSize / idealChunkCount, 1);

        return Math.min(idealChunkSize, MAX_CHUNK_SIZE);
    }

    /**
     * Reads data from the cache or loads it from the disk if not present in the cache.
     *
     * @return A deque of {@link TaskClass} objects loaded from the cache or disk.
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
     * Writes task data to the file with encryption and compression.
     *
     * @param tasks The deque of {@link TaskClass} objects to write to the data file.
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
