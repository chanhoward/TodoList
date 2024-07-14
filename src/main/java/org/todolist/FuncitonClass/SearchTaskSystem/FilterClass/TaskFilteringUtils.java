package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

import org.todolist.FuncitonClass.SearchTaskSystem.SearchingSystemManager;
import org.todolist.TaskClass;

import java.util.ArrayList;
import java.util.List;

public abstract class TaskFilteringUtils extends SearchingSystemManager {

    static List<TaskClass> toBeFilteredTask = new ArrayList<>();
    static List<TaskClass> filteredTask = new ArrayList<>();

    public static void updateToBeFilteredTasks() {
        toBeFilteredTask.clear();
        toBeFilteredTask.addAll(tasksInData);
    }

    public static void resetFilteredTasks() {
        filteredTask.clear();
    }

    public static void printFilteredTasks() {
        if (filteredTask.isEmpty()) {
            System.out.println("No task found for the given search criteria.");
            return;
        }

        StringBuilder output = new StringBuilder();
        for (TaskClass task : filteredTask) {
            output.append("-------------------------------------------------------------------------\n")
                    .append("Task ID: ").append(task.getTaskId()).append("\n")
                    .append("\t").append(task.getContent()).append("\n")
                    .append("\tby ").append(task.getAuthor()).append("\n")
                    .append("\t(").append(task.getTime()).append(")\n")
                    .append("Status: ").append(task.isTaskCompleteStatus() ? "Completed" : "Pending").append("\n")
                    .append("-------------------------------------------------------------------------\n");
        }

        System.out.print(output);

        int foundTaskCount = filteredTask.size();
        System.out.println("Found " + foundTaskCount + " matching tasks.");

    }

    public static void contentFilter(String keyword) {
        ContentFilter.contentFilter(keyword);
    }

    public static void authorFilter(String keyword) {
        AuthorFilter.authorFilter(keyword);
    }

    public static void createdDateFilter(String keyword) {
        CreatedDateFilter.createdDateFilter(keyword);
    }

    public static void taskIDFilter(String keyword) {
        TaskIDFilter.taskIDFilter(keyword);
    }

    public static void completedStatusFilter(String keyword) {
        CompletedStatusFilter.completedStatusFilter(keyword);
    }

}