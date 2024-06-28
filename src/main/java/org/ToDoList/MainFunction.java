package org.ToDoList;

import java.util.List;
import java.util.Scanner;

public class MainFunction {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Menu.commandMain();
    }

    static void addTask() {
        System.out.println("Input content: ");
        String inputContent = input.nextLine();

        System.out.println("Input author: ");
        String inputAuthor = input.nextLine();

        CustomLocalTime customLocalTime = new CustomLocalTime();
        String currentTime = customLocalTime.getTime();

        TaskClass taskClass = new TaskClass(currentTime, inputContent, inputAuthor);
        FileAccess.accessFile(taskClass);

        System.out.println("Task added successfully!");
    }

    static void listTask() {
        List<TaskClass> lists = FileAccess.readFile();

        if (lists.isEmpty()) {
            System.out.println("There are currently no to-do items");
            return;
        }

        for (TaskClass taskClass : lists) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println(taskClass.content());
            System.out.print("by " + taskClass.author());
            System.out.printf("\t(%s)\n", taskClass.time());
            System.out.println("-------------------------------------------------------------------------");
        }
    }
}