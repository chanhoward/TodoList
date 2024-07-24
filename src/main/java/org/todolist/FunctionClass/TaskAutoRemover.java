package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class TaskAutoRemover extends TodoListManager {

    private static final int NUM_PARTITIONS = 4;

    public static void removeExpiredTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        int currentTimeScore = TimeClass.calculateTimeScore(
                currentTime.getYear(),
                currentTime.getMonthValue(),
                currentTime.getDayOfMonth(),
                currentTime.getHour(),
                currentTime.getMinute());

        List<List<TaskClass>> partitions = splitTasksIntoPartitions(tasksInData);

        ConcurrentMap<Integer, TaskClass> tasksToRemoveMap = new ConcurrentHashMap<>();

        partitions.parallelStream().forEach(partition -> {
            List<TaskClass> tasksToRemove = filterTasksForRemoval(partition, currentTimeScore);
            tasksToRemove.forEach(task -> tasksToRemoveMap.put(task.getTaskId(), task));
        });

        List<TaskClass> filteredTasks = tasksInData.stream()
                .filter(task -> !tasksToRemoveMap.containsKey(task.getTaskId()))
                .collect(Collectors.toList());

        int totalTasks = tasksInData.size();
        int removedTasksCount = totalTasks - filteredTasks.size();

        if (removedTasksCount == 0) {
            System.out.println("No tasks to remove.");
        } else {
            tasksInData = filteredTasks;
            rearrangeTaskIds();
            FileAccess.writeDataFile(tasksInData);
            System.out.println("Tasks removed successfully. Total removed: " + removedTasksCount);
        }
    }

    private static List<TaskClass> filterTasksForRemoval(List<TaskClass> tasks, int currentTimeScore) {
        return tasks.stream()
                .filter(task -> task.isTaskCompleteStatus() && task.getTimeScore() < currentTimeScore)
                .collect(Collectors.toList());
    }

    private static List<List<TaskClass>> splitTasksIntoPartitions(List<TaskClass> tasks) {
        int partitionSize = (int) Math.ceil((double) tasks.size() / NUM_PARTITIONS);
        List<List<TaskClass>> partitions = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i += partitionSize) {
            partitions.add(tasks.subList(i, Math.min(i + partitionSize, tasks.size())));
        }

        return partitions;
    }

    private static void rearrangeTaskIds() {
        for (int i = 0; i < tasksInData.size(); i++) {
            tasksInData.get(i).setTaskId(i + 1);
        }
    }
}
