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

        listTasks(filteredTask);
        System.out.println("Found " + filteredTask.size() + " matching tasks.");

    }

    public static void filterByContent(String keyword) {
        ContentFilter.filterByContent(keyword);
    }

    public static void filterByPendingRank(String keyword) {
        PendingRankFilter.filterByPendingRank(keyword);
    }

    public static void filterByDueDate(String keyword) {
        DueDateFilter.filterByDueDate(keyword);
    }

    public static void filterByCompletedStatus(String keyword) {
        CompletedStatusFilter.filterByCompletedStatus(keyword);
    }

    public static void filterByAuthor(String keyword) {
        AuthorFilter.filterByAuthor(keyword);
    }

    public static void filterByCreatedDate(String keyword) {
        CreatedDateFilter.filterByCreatedDate(keyword);
    }

    public static void filterByTaskId(String keyword) {
        TaskIdFilter.filterByTaskId(keyword);
    }

}