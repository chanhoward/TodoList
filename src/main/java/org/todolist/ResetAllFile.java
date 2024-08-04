package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

/**
 * This class is responsible for resetting all files related to the application.
 * It includes methods to delete the data file, key file, and IV file.
 */
public class ResetAllFile extends FileAccess {

    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final String DATA_FILE = "Data.dat";
    private static final String KEY_FILE = "key.bin";
    private static final String IV_FILE = "iv.bin";

    /**
     * Resets all related files by deleting them and reinitializing the necessary data structures.
     * Prompts the user for confirmation before performing the reset.
     */
    public static void resetAllFile() {
        if (!isAgreeToResetFile()) {
            return;
        }

        // Reset files
        LOGGER.info("Resetting all files...");
        deleteFile(DATA_FILE);
        deleteFile(KEY_FILE);
        deleteFile(IV_FILE);

        initialize();
        buildDataFile();

        isAccessFail = false;
        System.out.println("All files have been reset.");
    }

    /**
     * Prompts the user for confirmation to reset all data files.
     *
     * @return True if the user agrees to reset the files, false otherwise.
     */
    private static boolean isAgreeToResetFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to reset all data? This will make you lose all of the data (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            System.out.println("Reset confirmed.");
            return true;
        } else {
            System.out.println("Reset canceled.");
            return false;
        }
    }

    /**
     * Deletes the specified file from the file system.
     *
     * @param fileName The name of the file to delete.
     */
    private static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            LOGGER.warn("File does not exist: {}", fileName);
            return;
        }

        boolean deleted = file.delete();
        if (deleted) {
            LOGGER.info("File deleted successfully: {}", fileName);
        } else {
            LOGGER.warn("Failed to delete file: {}", fileName);
        }
    }
}
