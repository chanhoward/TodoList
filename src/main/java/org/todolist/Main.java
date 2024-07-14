package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("Application started");
            FileAccess.readDataFile();
            Menu.menuCommandManager();

        } catch (Exception e) {
            LOGGER.error("Error occurred: " + e.getMessage());
        }
    }
}