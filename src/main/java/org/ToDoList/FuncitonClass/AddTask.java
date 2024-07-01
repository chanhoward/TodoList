package org.ToDoList.FuncitonClass;

import org.ToDoList.CustomLocalTime;
import org.ToDoList.FileAccess;
import org.ToDoList.TaskClass;

import java.util.List;
import java.util.Scanner;

public class AddTask {
    public static void addTask() {
        Scanner input = new Scanner(System.in);
        System.out.println("Input content: ");
        String inputContent = input.nextLine();

        System.out.println("Input author: ");
        String inputAuthor = input.nextLine();

        CustomLocalTime customLocalTime = new CustomLocalTime();
        String currentTime = customLocalTime.getTime();

        List<TaskClass> tasks = FileAccess.readFile();
        int taskId = tasks.isEmpty() ? 1 : tasks.size() + 1;

        TaskClass taskClass = new TaskClass(taskId, inputContent, inputAuthor, currentTime);
        FileAccess.addTaskToFile(taskClass);

        System.out.println("Task added successfully!");
    }
}
