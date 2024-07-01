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

    private static final String FILE_NAME = "Data.json";
    private static final Logger LOGGER = LogManager.getLogger(FileAccess.class);

    public static void addTaskToFile(TaskClass newList) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TaskClass> lists = new ArrayList<>();

        // 讀取現有的 JSON 文件
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                lists = objectMapper.readValue(file, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            LOGGER.error("讀取文件時發生錯誤：", e);
        }

        // 添加新的記錄
        lists.add(newList);

        // 寫回 JSON 文件
        try {
            objectMapper.writeValue(new File(FILE_NAME), lists);
        } catch (IOException e) {
            LOGGER.error("寫入文件時發生錯誤：", e);
        }
    }

    public static List<TaskClass> readFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(FILE_NAME), new TypeReference<>() {
            });
        } catch (IOException e) {
            LOGGER.error("讀取文件時發生錯誤：", e);
            return new ArrayList<>();
        }
    }
}