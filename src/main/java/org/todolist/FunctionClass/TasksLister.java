package org.todolist.FunctionClass;

import org.todolist.TaskClass;

import java.util.List;

public class TasksLister extends TodoListManager {

    public static void listTasks(List<TaskClass> list) {
        if (tasksInData.isEmpty()) {
            System.out.println("There are currently no to-do tasks");
            return;
        }

        StringBuilder output = new StringBuilder();
        for (TaskClass task : list) {
            output.append("--------------------------------------------------------------------------\n")
                    .append(String.format("Due Date: %-18s Task ID: %-20d Rank: %-10s\n\n",
                            task.getTimeScore() == 999999999 ? "No due date" : task.getDueDate(),
                            task.getTaskId(),
                            task.getPendingRank()))
                    .append(String.format("Content: %-50s\n\n", task.getContent()))
                    .append(String.format("Status: %-21s By %-20s ( %-15s )\n",
                            task.isTaskCompleteStatus() ? "Completed" : "Pending",
                            task.getAuthor(),
                            task.getCreatedDate()))
                    .append("--------------------------------------------------------------------------\n");
        }

        System.out.print(output);
    }
}
