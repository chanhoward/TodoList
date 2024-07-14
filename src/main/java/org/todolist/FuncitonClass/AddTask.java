package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.LocalTime;
import org.todolist.TaskClass;

import java.util.List;
import java.util.Scanner;

public class AddTask extends TodoListManager {
    private static final List<TaskClass> tasksInData = TodoListManager.tasksInData;

    public static void addTask() {

        String inputContent = inputContent();
        String inputAuthor = inputAuthor();

        LocalTime localTime = new LocalTime();
        String currentTime = localTime.getTime();

        int taskID = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;

        TaskClass newTask = new TaskClass(taskID, inputContent, inputAuthor, currentTime, false);
        addTaskToDataFile(newTask);

        System.out.println("Task added successfully!");
    }

    private static String inputContent() {
        Scanner input = new Scanner(System.in);
        String inputContent;
        do {
            System.out.println("Input content: ");
            inputContent = input.nextLine();
            if (inputContent.isEmpty()) {
                System.out.println("Content cannot be empty.");
            }
        } while (inputContent.isEmpty());

        return inputContent;
    }

    private static String inputAuthor() {
        System.out.println("Input author: ");
        String inputAuthor;
        Scanner input = new Scanner(System.in);
        inputAuthor = input.nextLine();
        if (inputAuthor.isEmpty()) {
            return "unknown";
        }

        return inputAuthor;
    }

    private static void addTaskToDataFile(TaskClass newTask) {
        List<TaskClass> updatedTasks = tasksInData;
        updatedTasks.add(newTask);
        FileAccess.writeDataFile(updatedTasks);
    }
}
