package org.todolist.FuncitonClass;

import org.todolist.FuncitonClass.SearchTaskSystem.SearchingSystemManager;

public interface TodoListManager {

    static void addTask() {
        AddTask.addTask();
    }

    static void listTasks() {
        ListTasks.listTasks();
    }

    static void markTaskCompleted() {
        MarkTaskAsCompleted.markTaskCompleted();
    }

    static void searchTypeManager() {
        SearchingSystemManager.filterProcessingManager();
    }

    static void removeTask() {
        RemoveTask.removeTask();
    }

}
