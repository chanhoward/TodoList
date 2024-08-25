package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

import static org.todolist.UserMessages.*;

/**
 * This class is responsible for resetting all files related to the application.
 * It includes methods to delete the data file, key file, and IV file.
 */
public class ResetAllFile extends DataIO {

    private static final Logger LOGGER = LogManager.getLogger(ResetAllFile.class);
    private static final String[] filesToBeReset = {"Data.dat", "key.bin", "iv.bin"};

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
        for (String fileName : filesToBeReset) {
            try {
                deleteFiles(fileName);
            } catch (Exception e) {
                LOGGER.error(e);
                System.err.println("Fail to reset system.");
                System.exit(1);
            }
        }

        FuncMenuMgr.isLoadFail = false;
        System.out.println(RESET_SUCCESS_MSG.getMessage());
        System.out.println(RESTART_TO_RESET_MSG.getMessage());
        System.exit(0);
    }

    /**
     * Prompts the user for confirmation to reset all data files.
     *
     * @return True if the user agrees to reset the files, false otherwise.
     */
    private static boolean isAgreeToResetFile() {
        Scanner scanner = new Scanner(System.in);
        System.err.println(SOME_THING_WRONG_MSG.getMessage());
        System.err.print(RESET_CONFIRMATION_MSG.getMessage());
        String confirmation = scanner.nextLine().trim().toLowerCase();
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            System.out.println(RESET_CONFIRM_MSG.getMessage());
            return true;
        } else {
            System.out.println(RESET_CANCEL_MSG.getMessage());
            return false;
        }
    }

    /**
     * Deletes the specified file from the file system.
     *
     * @param fileName The name of the file to delete.
     */
    private static void deleteFiles(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            LOGGER.warn("File does not exist: {}", fileName);
            return;
        }

        boolean deleted = file.delete();
        if (deleted) {
            LOGGER.info("File deleted successfully: {}", fileName);
        } else {
            throw new RuntimeException("Failed to delete file: ", new Throwable(fileName));
        }
    }
}
