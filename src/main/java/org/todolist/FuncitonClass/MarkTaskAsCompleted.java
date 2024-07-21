package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.List;
import java.util.Scanner;

public class MarkTaskAsCompleted extends TodoListManager {
    private static final List<TaskClass> tasksInData = TodoListManager.tasksInData;

    public static void markTaskCompleted() {
        while (true) {

            if (isAccessFail) {
                System.err.println("Failed to access data file.");
                break;
            }

            if (tasksInData.isEmpty()) {
                System.out.println("There are currently no to-do task");
                break;
            }

            listTasks(tasksInData);

            int inputTaskID = inputTaskID();
            if (inputTaskID == 0) {
                break;
            }

            boolean taskFound = false;

            for (TaskClass task : tasksInData) {

                if (task.getTaskId() != inputTaskID) {
                    continue;
                }

                if (task.isTaskCompleteStatus()) {
                    System.out.println("Task is already marked as completed.");
                } else {
                    task.setTaskCompleteStatus(true);
                    FileAccess.writeDataFile(tasksInData);
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

        Scanner scanner = new Scanner(System.in);

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input.");
            System.out.print("Input task ID to mark as completed: ");
            scanner.next(); // clear invalid input
        }
        return scanner.nextInt();

    }

}
