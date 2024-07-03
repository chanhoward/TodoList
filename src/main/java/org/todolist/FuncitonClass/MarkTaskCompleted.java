package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.List;
import java.util.Scanner;

public class MarkTaskCompleted {

    public static void markTaskCompleted() {
        while (true) {

            List<TaskClass> tasks = FileAccess.readDataFile();

            if (tasks.isEmpty()) {
                System.out.println("There are currently no to-do task");
                break;
            }

            ListTask.listTask();
            int taskID = inputTaskID();
            boolean taskFound = false;

            if (taskID == 0) {
                break;
            }

            for (TaskClass task : tasks) {

                if (task.getTaskID() != taskID) {
                    continue;
                }

                if (task.getIsTaskCompleted()) {
                    System.out.println("Task is already marked as completed.");
                } else {
                    task.setTaskCompleted();
                    FileAccess.writeToDataFile(tasks);
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
            System.out.println("Invalid input.");
            inputTaskID.next(); // clear invalid input
            System.out.print("Input task ID to mark as completed: ");
        }
        return inputTaskID.nextInt();

    }

}
