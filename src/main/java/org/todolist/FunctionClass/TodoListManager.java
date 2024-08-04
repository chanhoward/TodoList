package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.FunctionClass.SearchTaskSystem.SearchingSystemManager;
import org.todolist.TaskClass;

import java.util.List;

/**
 * Manages operations related to the todolist.
 */
public abstract class TodoListManager {
    public static final int TASK_COUNT_LIMIT = 100000;
    public static List<TaskClass> tasksInData;
    public static boolean isTasksFull = false;
    public static boolean isAccessFail;

    static {
        tasksInData = FileAccess.readDataFile();
        isAccessFail = FileAccess.isAccessFail;
    }

    /**
     * Adds a new task to the list.
     */
    public static void taskAdder() {
        TaskAdder.addTask();
    }

    /**
     * Lists tasks from the provided list.
     *
     * @param list The list of tasks to be listed.
     */
    public static void tasksLister(List<TaskClass> list) {
        TasksLister.listTasks(list);
    }

    /**
     * Marks a task as completed.
     */
    public static void taskCompletedMarker() {
        TaskAsCompletedMarker.markTaskCompleted();
    }

    /**
     * Manages search operations for tasks.
     */
    public static void searchTypeManager() {
        SearchingSystemManager.filterProcessingManager();
    }

    /**
     * Removes a task from the list.
     */
    public static void removeTask() {
        RemoveTask.inputAndDeleteTask();
    }

    /**
     * Removes expired tasks from the list.
     */
    public static void taskAutoRemover() {
        TaskAutoRemover.removeExpiredTasks();
    }
}
