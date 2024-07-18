package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.LocalTime;
import org.todolist.TaskClass;

import java.util.List;
import java.util.Scanner;

public class AddTask extends TodoListManager {

    public static void addTask() {
        if (isTasksFull) {
            return;
        }

        String pendingRank = inputRank();
        String inputContent = inputContent();
        String inputAuthor = inputAuthor();

        LocalTime localTime = new LocalTime();
        String currentTime = localTime.getTime();

        int taskID = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;

        TaskClass newTask = new TaskClass(taskID, pendingRank, inputContent, inputAuthor, currentTime, false);
        addTaskToDataFile(newTask);

        System.out.println("Task added successfully!");
    }

    private static String inputRank() {
        Scanner scanner = new Scanner(System.in);
        int inputRank;

        do {
            System.out.println("Input rank High/Medium/Low (1-3): ");
            inputRank = scanner.nextInt();
            if (inputRank < 1 || inputRank > 3) {
                System.out.println("Rank must be between 1 and 3.");
            }
        } while (inputRank < 1 || inputRank > 3);

        return switch (inputRank) {
            case 1 -> "High";
            case 2 -> "Medium";
            case 3 -> "Low";
            default -> "Unknown";
        };

    }

    private static String inputContent() {
        Scanner scanner = new Scanner(System.in);
        String inputContent;

        //Deal with empty input
        do {
            System.out.println("Input content: ");
            inputContent = scanner.nextLine();
            if (inputContent.isEmpty()) {
                System.out.println("Content cannot be empty.");
            }
        } while (inputContent.isEmpty());

        return inputContent;
    }

    private static String inputAuthor() {
        Scanner scanner = new Scanner(System.in);
        String inputAuthor;

        System.out.println("Input author: ");
        inputAuthor = scanner.nextLine();
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
