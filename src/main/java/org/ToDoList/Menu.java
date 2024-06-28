package org.ToDoList;

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
                    MainFunction.addTask();
                    break;
                case 2:
                    MainFunction.listTask();
                    break;
                /*case 3:
                    MainFunction.viewCompletedTasks();
                    break;*/
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
        //System.out.println("3. View completed tasks");

    }

    private static int getUserCommand() {
        System.out.print("Enter your command: ");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // 清除無效輸入
            System.out.print("Enter your command: ");
        }
        return scanner.nextInt();

    }

}
