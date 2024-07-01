package org.ToDoList.FuncitonClass;

import org.ToDoList.FileAccess;
import org.ToDoList.TaskClass;

import java.util.List;

public class ListTask {
    public static void listTask() {
        List<TaskClass> lists = FileAccess.readFile();

        if (lists.isEmpty()) {
            System.out.println("There are currently no to-do task");
            return;
        }

        for (TaskClass taskClass : lists) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("Task ID: " + taskClass.getTaskId());
            System.out.printf("\t%s\n", taskClass.content());
            System.out.print("\tby " + taskClass.author());
            System.out.printf("\t(%s)\n", taskClass.time());
            System.out.println("-------------------------------------------------------------------------");
        }
    }
}
