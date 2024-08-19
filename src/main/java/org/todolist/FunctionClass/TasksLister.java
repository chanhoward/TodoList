package org.todolist.FunctionClass;

import org.todolist.TaskClass;

import java.util.List;

import static org.todolist.UserMessages.NO_TASK_MSG;

public class TasksLister extends TodoListManager {

    /**
     * Lists the tasks in the specified list.
     *
     * @param list The list of tasks to display.
     */
    public static void listTasks(List<TaskClass> list) {
        if (tasksInData.isEmpty()) {
            System.out.println(NO_TASK_MSG.getMessage());
            return;
        }

        StringBuilder output = new StringBuilder(10000);
        for (TaskClass task : list) {
            output.append("----------------------------------------------------------------------------------------------------\n")
                    .append(String.format("Due Date: %-30s Task ID: %-32d Rank: %-10s\n\n",
                            task.getTimeScore() == 0 ? "No due date" : task.getDueDate(),
                            task.getTaskId(),
                            task.getPendingRank()))
                    .append(String.format("Content: %-50s\n\n", task.getContent()))
                    .append(String.format("Status: %-30s By %-35s ( %-15s )\n",
                            task.checkTaskCompleteStatus() ? "Completed" : "Pending",
                            task.getAuthor(),
                            task.getCreatedDate()))
                    .append("----------------------------------------------------------------------------------------------------\n");
        }

        System.out.print(output);
    }
}
