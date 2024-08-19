package org.todolist;

import org.todolist.FunctionClass.TodoListManager;

import java.util.Scanner;

import static org.todolist.UserMessages.*;

public class Menu extends TodoListManager {
    private static boolean isRunning = true;

    public static void menuCommandManager() {
        if (isLoadFail) {
            System.err.println(FAIL_TO_LOAD_MSG.getMessage());
            return;
        }

        do {
            displayMenu();
            int command = getUserCommand();
            executeCommand(command);
        } while (isRunning);
    }

    private static void executeCommand(int command) {
        switch (command) {
            case 0 -> isRunning = false;
            case 1 -> TodoListManager.taskAdder();
            case 2 -> TodoListManager.taskCompletedMarker();
            case 3 -> TodoListManager.removeTask();
            case 4 -> TodoListManager.searchTypeManager();
            case 5 -> TodoListManager.tasksLister(tasksInData);
            case 6 -> TodoListManager.taskAutoRemover();
            default -> System.out.println(INVALID_COMMAND_MSG.getMessage());
        }
    }


    private static void displayMenu() {
        if (tasksInData.size() >= TASK_COUNT_LIMIT) {
            System.err.println(TASK_FULL_MSG.getMessage());
            isTasksFull = true;
        } else {
            isTasksFull = false;
        }

        String menu = """

                Welcome to your To-Do-List!
                0. Exit
                1. Add a task
                2. Mark task as completed
                3. Remove a task
                4. Search task
                5. List the tasks
                6. Auto remove tasks to clear space
                """;

        System.out.print(menu);
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
