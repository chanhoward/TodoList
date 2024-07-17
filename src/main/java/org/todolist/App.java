package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("Application started");

            // Display menu and handle user commands
            LOGGER.info("Starting menu command manager");
            Menu.menuCommandManager();

        } catch (Exception e) {
            LOGGER.error("Error occurred: ", e);
        } finally {
            LOGGER.info("Application terminated");
        }
    }
}
