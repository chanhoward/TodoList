package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.FuncitonClass.SearchTaskSystem.SearchingSystemManager;
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

    public static void addTask() {
        AddTask.addTask();
    }

    public static void listTasks(List<TaskClass> list) {
        ListTasks.listTasks(list);
    }

    public static void markTaskCompleted() {
        MarkTaskAsCompleted.markTaskCompleted();
    }

    public static void searchTypeManager() {
        SearchingSystemManager.filterProcessingManager();
    }

    public static void removeTask() {
        RemoveTask.inputAndDeleteTask();
    }

    public static void autoRemoveTasks() {
        AutoRemoveTasks.autoRemoveTasks();
    }

}
