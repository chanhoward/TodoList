package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.List;
import java.util.Scanner;

public class MarkTaskAsCompleted implements TodoListManager {

    public static void markTaskCompleted() {
        while (true) {

            List<TaskClass> tasks = FileAccess.readDataFile();

            if (tasks.isEmpty()) {
                System.out.println("There are currently no to-do task");
                break;
            }

            ListTasks.listTasks();

            int inputTaskID = inputTaskID();
            if (inputTaskID == 0) {
                break;
            }

            boolean taskFound = false;

            for (TaskClass task : tasks) {

                if (task.getTaskID() != inputTaskID) {
                    continue;
                }

                if (task.isTaskCompleteStatus()) {
                    System.out.println("Task is already marked as completed.");
                } else {
                    task.setTaskCompleteStatus(true);
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
            System.out.print("Input task ID to mark as completed: ");
            inputTaskID.next(); // clear invalid input
        }
        return inputTaskID.nextInt();

    }

}
