package org.todolist;

import org.todolist.FuncitonClass.TodoListManager;

import java.util.Scanner;

public class Menu {
    private static boolean isRunning = true;

    public static void menuCommandManager() {

        while (isRunning) {
            displayMenu();

            int command = getUserCommand();

            switch (command) {
                case 0 -> isRunning = false;
                case 1 -> TodoListManager.addTask();
                case 2 -> TodoListManager.markTaskCompleted();
                case 3 -> TodoListManager.removeTask();
                case 4 -> TodoListManager.searchTypeManager();
                case 5 -> TodoListManager.listTasks();
                default -> System.out.println("Invalid command.");
            }
        }

    }

    private static void displayMenu() {

        String menu = """

                Welcome to your To-Do-List!
                0. Exit
                1. Add a task
                2. Mark task as completed
                3. Remove a task
                4. Search task
                5. List the tasks
                """;

        System.out.print(menu);
    }

    private static int getUserCommand() {
        Scanner inputCommand = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your command: ");

            if (inputCommand.hasNextInt()) {
                return inputCommand.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a integer.");
                inputCommand.next();
            }
        }
    }

}
