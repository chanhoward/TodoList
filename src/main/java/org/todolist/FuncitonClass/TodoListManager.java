package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.FuncitonClass.SearchTaskSystem.SearchingSystemManager;
import org.todolist.TaskClass;

import java.util.List;

public abstract class TodoListManager {
    public static List<TaskClass> tasksInData;

    static {
        tasksInData = FileAccess.readDataFile();
    }

    public static void addTask() {
        AddTask.addTask();
    }

    public static void listTasks() {
        ListTasks.listTasks();
    }

    public static void markTaskCompleted() {
        MarkTaskAsCompleted.markTaskCompleted();
    }

    public static void searchTypeManager() {
        SearchingSystemManager.filterProcessingManager();
    }

    public static void removeTask() {
        RemoveTask.removeTask();
    }

}
