package org.todolist.FuncitonClass;

import org.todolist.FileAccess;

import java.util.Scanner;

public class RemoveTask extends TodoListManager {

    public static void removeTask() {
        while (true) {
            if (tasksInData.isEmpty()) {
                System.out.println("No task to remove.");
                break;
            }

            int index = inputIndex();
            if (index == 0) {
                break;
            }

            if (index < 1 || index > tasksInData.size()) {
                System.out.println("Invalid taskID. No task removed.");
                continue;
            }

            tasksInData.remove(index - 1);
            rearrangeTasksID();
            FileAccess.writeDataFile(tasksInData);

            System.out.println("Task removed successfully.");
            break;
        }
    }

    private static int inputIndex() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            ListTasks.listTasks();
            System.out.print("Enter your taskID: ");

            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();  // Clear the invalid input
            }
        }
    }

    private static void rearrangeTasksID() {
        for (int i = 0; i < tasksInData.size(); i++) {
            tasksInData.get(i).setTaskId(i + 1);
        }
    }

}
