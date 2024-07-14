package org.todolist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final String DATA_FILE = "Data.dat";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static List<TaskClass> tasks = new ArrayList<>();

    static {
        FileEncryption.initializeKeyAndIv();
    }

    public static List<TaskClass> readDataFile() {
        File dataFile = new File(DATA_FILE);

        if (!dataFile.exists() || dataFile.length() == 0) {
            return buildDataFile(tasks);
        }

        try (FileInputStream fis = new FileInputStream(dataFile)) {
            byte[] fileData = fis.readAllBytes();

            String decryptedData = FileEncryption.decrypt(fileData);
            tasks = OBJECT_MAPPER.readValue(decryptedData, new TypeReference<>() {
            });

        } catch (IOException e) {
            LOGGER.error("Error reading file: ", e);
            ResetAllFile.resetAllFile();
        } catch (Exception e) {
            LOGGER.error("Error decrypting file: ", e);
            ResetAllFile.resetAllFile();
        }
        return tasks;
    }

    public static void writeDataFile(List<TaskClass> tasks) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(tasks);
            byte[] encryptedData = FileEncryption.encrypt(json);
            try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
                fos.write(encryptedData);
            }
        } catch (IOException e) {
            LOGGER.error("Error writing file: ", e);
        } catch (Exception e) {
            LOGGER.error("Error encrypting data: ", e);
        }
    }

    public static List<TaskClass> buildDataFile(List<TaskClass> tasks) {
        File dataFile = new File(DATA_FILE);

        try {
            if (dataFile.createNewFile()) {
                LOGGER.info("File created successfully: " + DATA_FILE);
            } else {
                LOGGER.warn("File already exists: " + DATA_FILE);
            }
        } catch (IOException e) {
            LOGGER.error("Error creating file: ", e);
        }
        return tasks;
    }
}
