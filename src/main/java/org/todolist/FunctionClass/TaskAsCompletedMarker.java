package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.Scanner;

public class TaskAsCompletedMarker extends TodoListManager {

    private static final Scanner scanner = new Scanner(System.in);

    public static void markTaskCompleted() {
        while (true) {
            if (tasksInData.isEmpty()) {
                System.out.println("There are currently no to-do tasks.");
                break;
            }

            tasksLister(tasksInData);

            int inputTaskID = inputTaskID();
            if (inputTaskID == 0) {
                System.out.println("Operation cancelled.");
                break; // Exit if user chooses to cancel
            }

            TaskClass task = findTaskById(inputTaskID);
            if (task == null) {
                System.out.println("Task not found.");
            } else if (task.isTaskCompleteStatus()) {
                System.out.println("Task is already marked as completed.");
            } else {
                task.setTaskCompleteStatus(true);
                FileAccess.writeDataFile(tasksInData);
                System.out.println("Task marked as completed successfully.");
            }
        }
    }

    /**
     * Finds a task by its ID.
     *
     * @param taskId The ID of the task to find.
     * @return The task with the specified ID, or null if not found.
     */
    private static TaskClass findTaskById(int taskId) {
        for (TaskClass task : tasksInData) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private static int inputTaskID() {
        System.out.print("Input task ID to mark as completed (or 0 to cancel): ");

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid task ID.");
            scanner.next(); // clear invalid input
            System.out.print("Input task ID to mark as completed (or 0 to cancel): ");
        }
        return scanner.nextInt();
    }
}
