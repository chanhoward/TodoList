package org.todolist;

import org.todolist.FuncitonClass.AddTask;
import org.todolist.FuncitonClass.ListTask;
import org.todolist.FuncitonClass.MarkTaskCompleted;

import java.util.Scanner;

public class Menu {
    private static boolean isRunning = true;

    public static void commandMain() {
        while (isRunning) {
            displayMenu();

            int command = getUserCommand();

            switch (command) {
                case 0:
                    isRunning = false;
                    break;
                case 1:
                    AddTask.addTask();
                    break;
                case 2:
                    ListTask.listTask();
                    break;
                case 3:
                    MarkTaskCompleted.markTaskCompleted();
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }

    }

    private static void displayMenu() {
        System.out.println();
        System.out.println("Welcome to your To-Do-List!");
        System.out.println("0. Exit");
        System.out.println("1. Add a task");
        System.out.println("2. List the task");
        System.out.println("3. Mark a task as completed");
    }

    private static int getUserCommand() {
        Scanner inputCommand = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your command: ");

            if (inputCommand.hasNextInt()) {
                return inputCommand.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a number.");
                inputCommand.next();
            }
        }
    }

}
