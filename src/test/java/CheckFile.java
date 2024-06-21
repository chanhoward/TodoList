import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CheckFile {
    private static final String FILE_NAME = "document.json";
    private static boolean isFileNeedToRemove;

    public static void main(String[] args) {
        if (!isFileExist()) {
            return;
        }
        askRemoveFile();
        readFile();
        if (isFileNeedToRemove) {
            removeFile();
        }
    }

    public static boolean isFileExist() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            return true;
        } else {
            System.out.println("File doesn't exist.");
            return false;
        }
    }

    public static void askRemoveFile() {
        System.out.println("Remove file? (y/n):");
        boolean isValidCommand = false;
        try (Scanner input = new Scanner(System.in)) {
            while (!isValidCommand) {
                try {
                    char command = input.next().charAt(0);
                    switch (command) {
                        case 'y':
                            isFileNeedToRemove = true;
                            isValidCommand = true;
                            break;
                        case 'n':
                            isFileNeedToRemove = false;
                            isValidCommand = true;
                            break;
                        default:
                            System.out.println("Invalid command, please try again.");
                            break;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Invalid command, please try again.");
                    input.nextLine();
                }
            }
        }

    } // (method

    public static void readFile() {
        try (FileReader reader = new FileReader(FILE_NAME);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Loading file error: " + e.getMessage());
        }
    } // (method

    public static void removeFile() {
        File file = new File(FILE_NAME);

        if (file.delete()) {
            System.out.printf("file have been removed: %s%n.", FILE_NAME);
        } else {
            System.out.println("Fail to remove file.");
        }
    }
}