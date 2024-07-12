package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RemoveTask implements TodoListManager {
    private static final List<TaskClass> toBeReviseTasks = FileAccess.readDataFile();
    private static final List<TaskClass> revisedTasks = new ArrayList<>();

    public static void removeTask() {

        while (true) {
            if (toBeReviseTasks.isEmpty()) {
                System.out.println("No task to remove.");
                break;
            }

            int index = inputIndex();
            if (index == 0) {
                break;
            }

            if (index < 0 || index > toBeReviseTasks.size()) {
                System.out.println("Invalid taskID. No task removed.");
                continue;
            }

            toBeReviseTasks.stream()
                    .filter(task -> task.getTaskID() != index)
                    .forEach(revisedTasks::add);
            rearrangeTasksID();
            FileAccess.writeToDataFile(revisedTasks);

            updateToBeReviseTasks();
            clearRevisedTasks();

            System.out.println("Task removed successfully.");
            break;

        }

    }

    private static int inputIndex() {
        Scanner inputIndex = new Scanner(System.in);

        while (true) {
            ListTasks.listTasks();
            System.out.print("Enter your taskID: ");

            if (inputIndex.hasNextInt()) {
                return inputIndex.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a integer.");
                inputIndex.next();
            }
        }
    }

    private static void rearrangeTasksID() {
        for (int i = 0; i < revisedTasks.size(); i++) {
            revisedTasks.get(i).setTaskID(i + 1);
        }

    }

    private static void updateToBeReviseTasks() {
        toBeReviseTasks.clear();
        toBeReviseTasks.addAll(FileAccess.readDataFile());
    }

    private static void clearRevisedTasks() {
        revisedTasks.clear();
    }
}
