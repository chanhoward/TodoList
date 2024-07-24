package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.FunctionClass.SearchTaskSystem.SearchingSystemManager;
import org.todolist.TaskClass;

import java.util.List;

public abstract class TodoListManager {
    public static final int TASK_COUNT_LIMIT = 100000;
    public static List<TaskClass> tasksInData;
    public static boolean isTasksFull = false;
    public static boolean isAccessFail;

    static {
        tasksInData = FileAccess.readDataFile();
        isAccessFail = FileAccess.isAccessFail;
    }

    public static void taskAdder() {
        TaskAdder.addTask();
    }

    public static void tasksLister(List<TaskClass> list) {
        TasksLister.listTasks(list);
    }

    public static void taskCompletedMarker() {
        TaskAsCompletedMarker.markTaskCompleted();
    }

    public static void searchTypeManager() {
        SearchingSystemManager.filterProcessingManager();
    }

    public static void removeTask() {
        RemoveTask.inputAndDeleteTask();
    }

    public static void taskAutoRemover() {
        TaskAutoRemover.removeExpiredTasks();
    }

}
