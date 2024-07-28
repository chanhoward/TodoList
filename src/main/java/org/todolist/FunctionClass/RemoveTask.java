package org.todolist.FunctionClass;

import org.todolist.FileAccess;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RemoveTask extends TodoListManager {

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
                System.out.println("Invalid taskID. No task removed.");
                continue;
            }

            deleteTaskById(index);

            System.out.println("Task removed successfully.");
            break;
        }
    }

    private static int inputIndex() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            tasksLister(tasksInData);
            System.out.print("Enter your taskID: ");

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

    public static void deleteTaskById(int index) {
        tasksInData.removeIf(task -> task.getTaskId() == index);
        rearrangeTasksId();
        FileAccess.writeDataFile(tasksInData);
    }

    private static void rearrangeTasksId() {
        IntStream.range(0, tasksInData.size()).forEach(
                i -> tasksInData.get(i).setTaskId(i + 1)
        );
    }
}
