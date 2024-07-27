package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final String DATA_FILE = "Data.dat";
    public static boolean isAccessFail = false;
    private static boolean isInitialized = false;
    private static List<TaskClass> tasksCache = new ArrayList<>();

    static void initialize() {
        LOGGER.info("Initializing data...");
        FileEncryption.initializeKeyAndIv();
        isInitialized = true;
    }

    @SuppressWarnings("unchecked")
    public static List<TaskClass> readDataFile() {
        if (!isInitialized) {
            initialize();
        }

        LOGGER.info("Reading data file...");

        if (!tasksCache.isEmpty()) {
            return tasksCache;
        }

        File dataFile = new File(DATA_FILE);

        if (!dataFile.exists()) {
            LOGGER.warn("Data file does not exist.");
            buildDataFile();
            return new ArrayList<>();
        }

        if (dataFile.length() == 0) {
            LOGGER.warn("Data file is empty.");
            return new ArrayList<>();
        }

        try (BufferedInputStream bufferedIn = new BufferedInputStream(new FileInputStream(dataFile));
             CipherInputStream cipherIn = new CipherInputStream(bufferedIn, FileEncryption.getDecryptCipher());
             ObjectInputStream objIn = new ObjectInputStream(cipherIn)) {

            Object obj = objIn.readObject();

            if (obj instanceof List<?>) {
                tasksCache = (List<TaskClass>) obj;
            } else {
                LOGGER.error("Deserialized object is not of type List<TaskClass>");
                throw new ClassNotFoundException("Deserialized object is not of type List<TaskClass>");
            }

        } catch (Exception e) {
            isAccessFail = true;
            LOGGER.error("Error occurred while reading or decrypting the data file: ", e);
            ResetAllFile.resetAllFile();
        }

        return tasksCache;
    }

    public static void writeDataFile(List<TaskClass> tasks) {
        if (!isInitialized) {
            initialize();
        }

        try (BufferedOutputStream bufferedOut = new BufferedOutputStream(new FileOutputStream(DATA_FILE));
             CipherOutputStream cipherOut = new CipherOutputStream(bufferedOut, FileEncryption.getEncryptCipher());
             ObjectOutputStream objOut = new ObjectOutputStream(cipherOut)) {

            LOGGER.info("Writing data file...");
            objOut.writeObject(tasks);
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
