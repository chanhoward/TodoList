package org.ToDoList.FuncitonClass;

import org.ToDoList.FileAccess;
import org.ToDoList.TaskClass;

import java.util.List;
import java.util.Scanner;

public class MarkTaskCompleted {
    public static void markTaskCompleted() {
        while (true) {
            ListTask.listTask();
            int taskID = inputTaskID();
            List<TaskClass> tasks = FileAccess.readFile();
            boolean taskFound = false;

            if (taskID == 0)
                break;

            for (TaskClass task : tasks) {
                if (task.getTaskID() != taskID) {
                    continue;
                }

                if (task.getIsTaskCompleted()) {
                    System.out.println("Task is already marked as completed.");
                } else {
                    task.setTaskCompleted();
                    FileAccess.writeFile(tasks);
                    System.out.println("Task marked as completed successfully.");
                }

                taskFound = true;
                break;

            }

            if (!taskFound) {
                System.out.println("Task not found.");
            }
        }
    }

    private static int inputTaskID() {
        System.out.print("Input task ID to mark as completed: ");
        Scanner inputTaskID = new Scanner(System.in);
        while (!inputTaskID.hasNextInt()) {
            System.out.println("Invalid input. Please enter a task ID.");
            inputTaskID.next(); // clear invalid input
            System.out.print("Enter your task ID: ");
        }
        return inputTaskID.nextInt();

    }

}
