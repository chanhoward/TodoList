package org.todolist.FunctionClass;

import org.todolist.FileAccess;

import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * This class is responsible for removing tasks from the to-do list.
 * It includes methods to input the task ID, delete the task, and rearrange task IDs.
 */
public class RemoveTask extends TodoListManager {

    /**
     * Prompts the user to input a task ID and deletes the corresponding task.
     * Continues to prompt the user until a valid task ID is entered or the user chooses to exit.
     */
    public static void inputAndDeleteTask() {
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
                System.out.println("Invalid task ID. No task removed.");
                continue;
            }

            deleteTaskById(index);
            System.out.println("Task removed successfully.");
            break;
        }
    }

    /**
     * Prompts the user to input the task ID to be removed.
     *
     * @return The task ID entered by the user.
     */
    private static int inputIndex() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            tasksLister(tasksInData);
            System.out.print("Enter your task ID: ");

            if (scanner.hasNextInt()) {
                int command = scanner.nextInt();
                scanner.nextLine();
                return command;
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Deletes the task with the specified task ID from the to-do list.
     *
     * @param index The task ID of the task to be deleted.
     */
    public static void deleteTaskById(int index) {
        tasksInData.removeIf(task -> task.getTaskId() == index);
        rearrangeTasksId();
        FileAccess.writeDataFile(tasksInData);
    }

    /**
     * Rearranges the task IDs of the remaining tasks in the to-do list to maintain a continuous sequence.
     */
    private static void rearrangeTasksId() {
        IntStream.range(0, tasksInData.size()).forEach(
                i -> tasksInData.get(i).setTaskId(i + 1)
        );
    }
}
