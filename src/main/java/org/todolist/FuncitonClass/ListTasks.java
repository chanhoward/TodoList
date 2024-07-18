package org.todolist.FuncitonClass;

import org.todolist.TaskClass;

public class ListTasks extends TodoListManager {

    public static void listTasks() {

        if (tasksInData.isEmpty()) {
            System.out.println("There are currently no to-do tasks");
            return;
        }

        StringBuilder output = new StringBuilder();

        for (TaskClass task : tasksInData) {
            output.append("-------------------------------------------------------------------------\n")
                    .append("Task ID: ").append(task.getTaskId()).append("\n")
                    .append("Rank: ").append(task.getPendingRank()).append("\n")
                    .append("\t").append(task.getContent()).append("\n")
                    .append("\tby ").append(task.getAuthor()).append("\t(").append(task.getTime()).append(")\n")
                    .append("Status: ").append(task.isTaskCompleteStatus() ? "Completed" : "Pending").append("\n")
                    .append("-------------------------------------------------------------------------\n");
        }

        System.out.print(output);
    }
}
