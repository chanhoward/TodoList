package org.todolist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final String DATA_FILE = "Data.dat";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static boolean isInitialized = false;
    private static List<TaskClass> tasksCache = new ArrayList<>();

    // Initialize encryption key and IV
    static void initialize() {
        LOGGER.info("Initializing data...");
        FileEncryption.initializeKeyAndIv();
        isInitialized = true;
    }

    public static List<TaskClass> readDataFile() {
        if (!isInitialized) {
            initialize();
        }

        LOGGER.info("Reading data file...");

        // Return cached tasks if is empty
        if (!tasksCache.isEmpty()) {
            return tasksCache;
        }

        File dataFile = new File(DATA_FILE);

        // Check if the file exists and is not empty
        if (!dataFile.exists() || dataFile.length() == 0) {
            LOGGER.warn("Data file does not exist or is empty.");
            buildDataFile();
            return new ArrayList<>();
        }

        try (FileChannel fileChannel = FileChannel.open(dataFile.toPath(), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(buffer);
            buffer.flip();

            byte[] fileData = new byte[buffer.remaining()];
            buffer.get(fileData);

            // Decrypt the data
            String decryptedData = FileEncryption.decrypt(fileData);
            tasksCache = objectMapper.readValue(decryptedData, new TypeReference<>() {
            });

        } catch (Exception e) {
            LOGGER.error("Error occurred while reading or decrypting the data file: ", e);
            ResetAllFile.resetAllFile();
            tasksCache = new ArrayList<>();
        }

        return tasksCache;
    }

    public static void writeDataFile(List<TaskClass> tasks) {
        if (!isInitialized) {
            initialize();
        }

        LOGGER.info("Writing data file...");
        try {
            // Convert tasks list to JSON string
            String json = objectMapper.writeValueAsString(tasks);

            // Encrypt the JSON string
            byte[] encryptedData = FileEncryption.encrypt(json);

            try (FileChannel fileChannel = FileChannel.open(Path.of(DATA_FILE), StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
                ByteBuffer buffer = ByteBuffer.wrap(encryptedData);
                while (buffer.hasRemaining()) {
                    int bytesWritten = fileChannel.write(buffer);
                    if (bytesWritten == 0) {
                        LOGGER.warn("No bytes written, potential issue with file write.");
                    }
                }
            }
            // Update the cache
            tasksCache = new ArrayList<>(tasks);
        } catch (Exception e) {
            LOGGER.error("Error occurred while writing the data file: ", e);
        }
    }

    public static void buildDataFile() {
        LOGGER.info("Building data file...");
        try {
            File dataFile = new File(DATA_FILE);
            if (dataFile.createNewFile()) {
                LOGGER.info("Data file created successfully: " + DATA_FILE);
            } else {
                LOGGER.warn("Data file already exists: " + DATA_FILE);
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating the data file: ", e);
        }
    }
}
