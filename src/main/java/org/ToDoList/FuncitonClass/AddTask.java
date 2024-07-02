package org.ToDoList.FuncitonClass;

import org.ToDoList.CustomLocalTime;
import org.ToDoList.FileAccess;
import org.ToDoList.TaskClass;

import java.util.List;
import java.util.Scanner;

public class AddTask {
    public static void addTask() {
        // Input content and author
        String inputContent = inputContent();
        String inputAuthor = inputAuthor();

        CustomLocalTime customLocalTime = new CustomLocalTime();
        String currentTime = customLocalTime.getTime();

        List<TaskClass> tasks = FileAccess.readFile();
        int taskID = tasks.isEmpty() ? 1 : tasks.size() + 1;

        TaskClass taskClass = new TaskClass(taskID, inputContent, inputAuthor, currentTime, false);
        FileAccess.addTaskToFile(taskClass);

        System.out.println("Task added successfully!");
    }

    private static String inputContent() {
        Scanner input = new Scanner(System.in);
        String inputContent;
        do {
            System.out.println("Input content: ");
            inputContent = input.nextLine();
            if (inputContent.isBlank()) {
                System.out.println("Content cannot be blank!");
            }
        } while (inputContent.isBlank());

        return inputContent;
    }

    private static String inputAuthor() {
        System.out.println("Input author: ");
        Scanner input = new Scanner(System.in);
        String inputAuthor = input.nextLine();
        if (inputAuthor.isBlank()) {
            return "unknown";
        }

        return inputAuthor;
    }
}