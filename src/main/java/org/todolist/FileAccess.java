package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class FileAccess {

    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final String DATA_FILE = "Data.dat";
    private static final int BUFFER_SIZE = 8192;
    private static boolean isInitialized = false;
    private static List<TaskClass> tasksCache = new ArrayList<>(1000);

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

        if (!tasksCache.isEmpty()) {
            return tasksCache;
        }

        LOGGER.info("Reading data file...");
        File dataFile = new File(DATA_FILE);

        if (dataFile.length() == 0 || !dataFile.exists()) {
            return tasksCache;
        }

        try (FileInputStream fileIn = new FileInputStream(dataFile);
             BufferedInputStream bufferedIn = new BufferedInputStream(fileIn, BUFFER_SIZE);
             CipherInputStream cipherIn = new CipherInputStream(bufferedIn, FileEncryption.getDecryptCipher());
             GZIPInputStream gzipIn = new GZIPInputStream(cipherIn);
             ObjectInputStream objIn = new ObjectInputStream(gzipIn)) {

            Object obj = objIn.readObject();
            if (obj instanceof List<?>) {
                tasksCache = (List<TaskClass>) obj;
            } else {
                throw new ClassNotFoundException("Deserialized object is not of type List<TaskClass>");
            }
        } catch (Exception e) {
            throw new RuntimeException("Fail to read data file: ", e);
        }
        return tasksCache;
    }

    public static void writeDataFile(List<TaskClass> tasks) {
        if (!isInitialized) {
            initialize();
        }

        try (FileOutputStream fileOut = new FileOutputStream(DATA_FILE);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut, BUFFER_SIZE);
             CipherOutputStream cipherOut = new CipherOutputStream(bufferedOut, FileEncryption.getEncryptCipher());
             GZIPOutputStream gzipOut = new GZIPOutputStream(cipherOut);
             ObjectOutputStream objOut = new ObjectOutputStream(gzipOut)) {

            LOGGER.info("Writing data file...");
            objOut.writeObject(tasks);
            tasksCache = new ArrayList<>(tasks);
        } catch (Exception e) {
            throw new RuntimeException("Fail to write data file: ", e);
        }
    }
}
