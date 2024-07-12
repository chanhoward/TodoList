package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.List;

public class ListTasks implements TodoListManager {
    public static void listTasks() {
        List<TaskClass> tasks = FileAccess.readDataFile();

        if (tasks.isEmpty()) {
            System.out.println("There are currently no to-do task");
            return;
        }

        for (TaskClass taskClass : tasks) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("Task ID: " + taskClass.getTaskID());
            System.out.printf("\t%s\n", taskClass.getContent());
            System.out.print("\tby " + taskClass.getAuthor());
            System.out.printf("\t(%s)\n", taskClass.getTime());
            System.out.println("Status: " + (taskClass.isTaskCompleteStatus() ? "Completed" : "Pending"));
            System.out.println("-------------------------------------------------------------------------");
        }
    }
}
