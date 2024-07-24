package org.todolist;

import org.todolist.FunctionClass.TodoListManager;

import java.util.Scanner;

public class Menu extends TodoListManager {
    private static boolean isRunning = true;

    public static void menuCommandManager() {

        while (isRunning) {
            displayMenu();

            int command = getUserCommand();

            switch (command) {
                case 0 -> isRunning = false;
                case 1 -> TodoListManager.taskAdder();
                case 2 -> TodoListManager.taskCompletedMarker();
                case 3 -> TodoListManager.removeTask();
                case 4 -> TodoListManager.searchTypeManager();
                case 5 -> TodoListManager.tasksLister(tasksInData);
                case 6 -> TodoListManager.taskAutoRemover();
                default -> System.out.println("Invalid command.");
            }
        }

    }

    private static void displayMenu() {
        if (tasksInData.size() >= TASK_COUNT_LIMIT) {
            System.err.println("\nTask count limit reached(100000). Please remove some tasks before adding new ones.");
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
            System.out.print("Enter your command: ");

            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a integer.");
                scanner.next();
            }
        }
    }

}
