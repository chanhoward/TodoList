package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FunctionClass.TasksLister;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        try {
            LOGGER.info("Application started");

            //Thread.sleep(10000);

            // Display menu and handle user commands
            LOGGER.info("Starting menu command manager");
            FuncMenuMgr.menuCmdMgr();

        } catch (Exception e) {
            LOGGER.error("Error occurred: ", e);
        } finally {
            TasksLister.shutdown();
            LOGGER.info("Application terminated");
        }
    }
}
