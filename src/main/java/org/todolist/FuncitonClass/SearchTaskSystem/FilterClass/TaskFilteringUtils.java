package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.ArrayList;
import java.util.List;

public interface TaskFilteringUtils {

    List<TaskClass> toBeFilteredTask = new ArrayList<>();
    List<TaskClass> filteredTask = new ArrayList<>();

    static void updateToBeFilteredTasks() {
        toBeFilteredTask.clear();
        toBeFilteredTask.addAll(FileAccess.readDataFile());
    }

    static void resetFilteredTasks() {
        filteredTask.clear();
    }

    static void printFilteredTasks() {
        int foundTaskCount = filteredTask.size();
        if (filteredTask.isEmpty()) {
            System.out.println("No tasks found for the given search criteria.");
            return;
        }

        for (TaskClass taskClass : filteredTask) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("Task ID: " + taskClass.getTaskID());
            System.out.printf("\t%s\n", taskClass.getContent());
            System.out.printf("\tby " + taskClass.getAuthor());
            System.out.printf("\t(%s)\n", taskClass.getTime());
            System.out.println("Status: " + (taskClass.isTaskCompleteStatus() ? "Completed" : "Pending"));
            System.out.println("-------------------------------------------------------------------------");
        }
        System.out.println("Found " + foundTaskCount + " matching tasks.");

    }

    static void contentFilter(String keyword) {
        ContentFilter.contentFilter(keyword);
    }

    static void authorFilter(String keyword) {
        AuthorFilter.authorFilter(keyword);
    }

    static void createdDateFilter(String keyword) {
        CreatedDateFilter.createdDateFilter(keyword);
    }

    static void taskIDFilter(String keyword) {
        TaskIDFilter.taskIDFilter(keyword);
    }

    static void completedStatusFilter(String keyword) {
        CompletedStatusFilter.completedStatusFilter(keyword);
    }

}