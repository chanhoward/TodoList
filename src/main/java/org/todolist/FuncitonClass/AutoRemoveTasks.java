package org.todolist.FuncitonClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AutoRemoveTasks extends TodoListManager {

    public static void autoRemoveTasks() {
        LocalDateTime time = LocalDateTime.now();
        int currentTimeScore = TimeClass.calculateTimeScore(time.getYear(), time.getMonthValue(), time.getDayOfMonth(), time.getHour(), time.getMinute());
        List<TaskClass> tasksToBeDelete = new ArrayList<>();

        boolean taskRemoved = false;
        for (TaskClass task : tasksInData) {
            if (task.isTaskCompleteStatus() && task.getTimeScore() < currentTimeScore) {
                tasksToBeDelete.add(task);
                taskRemoved = true;
            }
        }

        deleteTasksByList(tasksToBeDelete);
        System.out.println();

        if (!taskRemoved) {
            System.out.println("No tasks to remove.");
        } else {
            rearrangeTasksId();
            FileAccess.writeDataFile(tasksInData);
            System.out.println("Task removed successfully.");
        }
    }

    private static void deleteTasksByList(List<TaskClass> tasksToBeDelete) {
        int i = 0;
        for (TaskClass task : tasksToBeDelete) {
            tasksInData.remove(task);
            i++;
            printProgressBar(i, tasksToBeDelete.size());
        }

    }

    private static void printProgressBar(int current, int amount) {
        int progress = (int) ((double) current / amount * 50);
        StringBuilder progressBar = new StringBuilder("\r[");

        for (int i = 0; i < 50; i++) {
            progressBar.append(i < progress ? "=" : " ");
        }
        progressBar.append("] ")
                .append(current)
                .append("/")
                .append(amount)
                .append(" (")
                .append((int) ((double) current / amount * 100))
                .append("%)");

        System.out.print(progressBar);
    }

    private static void rearrangeTasksId() {
        for (int i = 0; i < tasksInData.size(); i++) {
            tasksInData.get(i).setTaskId(i + 1);
        }
    }
}
