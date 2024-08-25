package org.todolist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;
import org.todolist.FunctionClass.*;

import java.util.Deque;
import java.util.Scanner;

import static org.todolist.UserMessages.*;

public class FuncMenuMgr {
    /**
     * The maximum number of {@link TaskClass} instances allowed in the application.
     * This constant sets an upper limit on the number of tasks that can be stored in memory
     * to prevent excessive resource consumption.
     */
    public static final int TASK_COUNT_LIMIT = 100000;
    private static final Logger LOGGER = LogManager.getLogger(FuncMenuMgr.class);

    /**
     * A global deque holding all the {@link TaskClass} instances currently in memory.
     * This deque is shared across various functions within the class and is used as the primary data structure
     * for storing tasks during runtime.
     *
     * <p>Note: Modifying this deque directly can have wide-ranging effects on the application,
     * so it should be done with care to avoid concurrency issues or data corruption.</p>
     */
    public static Deque<TaskClass> tasksInData;

    /**
     * A global flag indicating whether the task list has reached its capacity.
     * When {@code true}, no more tasks can be added until space is made available.
     */
    public static boolean isTasksFull = false;

    /**
     * A global flag that indicates whether the loading of tasks from the disk has failed.
     * When {@code true}, an error occurred during the loading process, and appropriate error-handling measures should be taken.
     */
    public static boolean isLoadFail;

    /**
     * A private flag controlling the running state of the program.
     * When {@code false}, the program should initiate shutdown procedures.
     */
    private static boolean isRunning = true;

    static {
        System.out.println(LOADING_MSG.getMessage());
        try {
            tasksInData = DataIO.readDataFile();
        } catch (Exception e) {
            LOGGER.error("Error occurred while reading data: ", e);
            ResetAllFile.resetAllFile();
            isLoadFail = true;
        }
    }

    public static void menuCmdMgr() {
        if (isLoadFail) {
            System.err.println(FAIL_TO_LOAD_MSG.getMessage());
            return;
        }

        do {
            System.gc();
            displayMenu();
            int command = getUserCommand();
            executeCommand(command);
        } while (isRunning);
    }

    private static void executeCommand(int command) {
        switch (command) {
            case 0 -> isRunning = false;
            case 1 -> TaskAdder.addTask();
            case 2 -> TaskAsCompletedMarker.markTaskCompleted();
            case 3 -> TaskRemover.inputAndDeleteTask();
            case 4 -> SearchMgr.filterController();
            case 5 -> TasksLister.listTasks(tasksInData);
            case 6 -> TaskAutoRemover.removeExpiredTasks();
            default -> System.out.println(INVALID_COMMAND_MSG.getMessage());
        }
    }

    private static void displayMenu() {
        if (tasksInData.size() >= TASK_COUNT_LIMIT) {
            System.err.print(TASK_FULL_MSG.getMessage());
            isTasksFull = true;
        } else {
            isTasksFull = false;
        }

        System.out.print(MAIN_MENU.getMessage());
    }

    private static int getUserCommand() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(PROMPT_COMMAND_MSG.getMessage());

            if (scanner.hasNextInt()) {
                int command = scanner.nextInt();
                scanner.nextLine();
                return command;
            } else {
                System.out.println(INVALID_INTEGER_INPUT_MSG.getMessage());
                scanner.nextLine();
            }
        }
    }
}