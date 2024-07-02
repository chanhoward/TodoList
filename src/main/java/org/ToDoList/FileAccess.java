package org.ToDoList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final String DATA_FILE = "Data.dat";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);

    static {
        try {
            FileEncryption.initializeKeyAndIv();
        } catch (IOException | NoSuchAlgorithmException e) {
            LOGGER.error("An error occurred while initializing the key and IV：", e);
        }
    }

    public static void addTaskToFile(TaskClass newList) {
        List<TaskClass> tasks = readFile();
        tasks.add(newList);
        writeFile(tasks);
    }

    public static List<TaskClass> readFile() {
        List<TaskClass> tasks = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            File DataFile = new File(DATA_FILE);

            if (!DataFile.exists()) {
                boolean created = DataFile.createNewFile();
                if (created) {
                    LOGGER.info("File create successfully：" + DATA_FILE);
                } else {
                    LOGGER.warn("File exist：" + DATA_FILE);
                }
                return tasks; // return blank list
            }

            byte[] fileData = new byte[(int) DataFile.length()];
            int bytesRead = fis.read(fileData);
            if (bytesRead != fileData.length) {
                LOGGER.error("Can't read file data");
            }

            String decryptedData = FileEncryption.decrypt(fileData);
            ObjectMapper objectMapper = new ObjectMapper();
            tasks = objectMapper.readValue(decryptedData, new TypeReference<>() {
            });

        } catch (Exception e) {
            LOGGER.warn("No find file or file was broken");
        }
        return tasks;
    }

    public static void writeFile(List<TaskClass> tasks) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(tasks);
            byte[] encryptedData = FileEncryption.encrypt(json);
            try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
                fos.write(encryptedData);
            }
        } catch (Exception e) {
            LOGGER.error("Writing file fail：", e);
        }
    }
}