package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchingSystemManager;
import org.todolist.TaskClass;

import java.util.ArrayList;
import java.util.List;

import static org.todolist.UserMessages.FOUND_MATCHING_TASKS_MSG;
import static org.todolist.UserMessages.SEARCH_NO_RESULT_MSG;

public abstract class TaskFilteringUtils extends SearchingSystemManager {

    static List<TaskClass> toBeFilteredTask = new ArrayList<>(1000);
    static List<TaskClass> filteredTask = new ArrayList<>(50);

    public static void updateToBeFilteredTasks() {
        toBeFilteredTask.clear();
        toBeFilteredTask.addAll(tasksInData);
    }

    public static void resetFilteredTasks() {
        filteredTask.clear();
    }

    public static void printFilteredTasks() {
        if (filteredTask.isEmpty()) {
            System.out.println(SEARCH_NO_RESULT_MSG.getMessage());
            return;
        }

        tasksLister(filteredTask);
        System.out.printf(FOUND_MATCHING_TASKS_MSG.getMessage(), filteredTask.size());

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