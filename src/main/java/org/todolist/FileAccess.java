package org.todolist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final String DATA_FILE = "Data.dat";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<TaskClass> tasks = new ArrayList<>();

    static {
        FileEncryption.initializeKeyAndIv();
    }

    public static List<TaskClass> readDataFile() {

        try (FileInputStream fis = new FileInputStream(DATA_FILE)) {
            File dataFile = new File(DATA_FILE);

            if (!dataFile.exists() || dataFile.length() == 0) {
                return buildDataFile(tasks);
            }

            byte[] fileData = new byte[(int) dataFile.length()];
            int bytesRead = fis.read(fileData);
            if (bytesRead != fileData.length) {
                LOGGER.error("Can't read file data");
            }

            String decryptedData = FileEncryption.decrypt(fileData);
            tasks = objectMapper.readValue(decryptedData, new TypeReference<>() {
            });

        } catch (Exception e) {
            LOGGER.error("Can't find file or file has broken", e);

            /**
             * This is for fix bug when program can't find file or file has broken and can't decrypt data, but it will delete all data in file.
             * This action can fix most of the problem in user interface.
             */
            ResetAllFile.resetAllFile();
        }
        return tasks;
    }

    public static void writeToDataFile(List<TaskClass> tasks) {
        try {
            String json = objectMapper.writeValueAsString(tasks);
            byte[] encryptedData = FileEncryption.encrypt(json);
            try (FileOutputStream fos = new FileOutputStream(DATA_FILE)) {
                fos.write(encryptedData);
            }
        } catch (Exception e) {
            LOGGER.error("Writing file fail：", e);
        }
    }

    public static List<TaskClass> buildDataFile(List<TaskClass> tasks) {
        try {
            File dataFile = new File(DATA_FILE);

            boolean created = dataFile.createNewFile();
            if (created) {
                LOGGER.info("File create successfully：" + DATA_FILE);
            } else {
                LOGGER.warn("File exist：" + DATA_FILE);
            }
            return tasks;

        } catch (Exception e) {
            LOGGER.error("Creating file fail：", e);
        }
        return tasks;
    }
}