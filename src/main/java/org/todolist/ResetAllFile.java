package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

/**
 * This class is responsible for resetting all files related to the application.
 * It includes methods to delete the data file, key and IV files, and initialize them again.
 */
public class ResetAllFile {

    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final String DATA_FILE = "Data.dat";
    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";

    public static void resetAllFile() {
        if (!isAgreeToResetFile()) {
            return;
        }

        // Reset files
        LOGGER.info("Resetting all files...");
        deleteDataFile();
        deleteKeyAndIvFile();
        FileEncryption.initializeKeyAndIv();
//        FileAccess.buildDataFile();
        System.out.println("All files have been reset.");
    }

    private static boolean isAgreeToResetFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to reset all data? (That will make you lose all of the data) (y/n)");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            System.out.println("Reset confirmed.");
            return true;
        } else {
            System.out.println("Reset canceled.");
            return false;
        }
    }

    private static void deleteDataFile() {
        File dataFile = new File(DATA_FILE);
        if (!dataFile.exists()) {
            LOGGER.warn("File does not exist: " + DATA_FILE);
        }

        boolean deleted = dataFile.delete();
        if (deleted) {
            LOGGER.info("File deleted successfully: " + DATA_FILE);
        } else {
            LOGGER.warn("Failed to delete file: " + DATA_FILE);
        }
    }

    private static void deleteKeyAndIvFile() {
        File keyFile = new File(KEY_FILE);
        File ivFile = new File(IV_FILE);
        boolean deletedKey = false;
        boolean deletedIv = false;

        if (keyFile.exists()) {
            deletedKey = keyFile.delete();
            if (!deletedKey) {
                LOGGER.warn("Failed to delete key file: " + KEY_FILE);
            }
        } else {
            LOGGER.warn("Key file does not exist: " + KEY_FILE);
        }

        if (ivFile.exists()) {
            deletedIv = ivFile.delete();
            if (!deletedIv) {
                LOGGER.warn("Failed to delete IV file: " + IV_FILE);
            }
        } else {
            LOGGER.warn("IV file does not exist: " + IV_FILE);
        }

        if (deletedKey && deletedIv) {
            LOGGER.info("Key and IV files deleted successfully");
        }
    }

}
