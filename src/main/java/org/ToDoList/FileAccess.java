package org.ToDoList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

    private static final String DATA_FILE = "Data.json";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);

    public static void addTaskToFile(TaskClass newTask) {
        List<TaskClass> tasks = new ArrayList<>();

        readFile();
        tasks.add(newTask);
        writeFile(tasks);
    }

    public static List<TaskClass> readFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(DATA_FILE), new TypeReference<>() {
            });
        } catch (IOException e) {
            LOGGER.error("讀取文件時發生錯誤：", e);
            return new ArrayList<>();
        }
    }

    public static void writeFile(List<TaskClass> tasks) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(DATA_FILE), tasks);
        } catch (IOException e) {
            LOGGER.error("寫入文件時發生錯誤：", e);
        }
    }
}