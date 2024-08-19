package org.todolist.FunctionClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.todolist.UserMessages.NO_TASK_TO_REMOVE_MSG;
import static org.todolist.UserMessages.TOTAL_REMOVED_MSG;

public class TaskAutoRemover extends TodoListManager {
    private static final Logger LOGGER = LogManager.getLogger(TaskAutoRemover.class);

    private static final int NUM_PARTITIONS = 4;
    private static final int DAYS_THRESHOLD = 3;
    private static final int MINUTES_IN_A_DAY = 24 * 60;
    private static final int TIME_THRESHOLD = DAYS_THRESHOLD * MINUTES_IN_A_DAY;

    /**
     * Removes expired tasks based on the current time and defined threshold.
     */
    public static void removeExpiredTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        int currentTimeScore = TimeClass.calculateTimeScore(
                currentTime.getYear(),
                currentTime.getMonthValue(),
                currentTime.getDayOfMonth(),
                currentTime.getHour(),
                currentTime.getMinute());

        List<List<TaskClass>> partitions = splitTasksIntoPartitions(tasksInData);

        ConcurrentMap<Integer, TaskClass> tasksToRemoveMap = partitions.parallelStream()
                .flatMap(partition -> filterTasksForRemoval(partition, currentTimeScore).stream())
                .collect(Collectors.toConcurrentMap(TaskClass::getTaskId, task -> task));

        List<TaskClass> filteredTasks = tasksInData.stream()
                .filter(task -> !tasksToRemoveMap.containsKey(task.getTaskId()))
                .collect(Collectors.toList());

        int removedTasksCount = tasksInData.size() - filteredTasks.size();

        if (removedTasksCount == 0) {
            System.out.println(NO_TASK_TO_REMOVE_MSG.getMessage());
        } else {
            tasksInData = filteredTasks;
            rearrangeTaskIds();
            try {
                FileAccess.writeDataFile(tasksInData);
            } catch (Exception e) {
                LOGGER.error("Error removing tasks from data file: ", e);
            }
            System.out.println(TOTAL_REMOVED_MSG.getMessage() + removedTasksCount);
        }
    }

    /**
     * Filters tasks for removal based on their completion status and time score.
     *
     * @param tasks            The list of tasks to filter.
     * @param currentTimeScore The current time score for comparison.
     * @return The list of tasks to be removed.
     */
    private static List<TaskClass> filterTasksForRemoval(List<TaskClass> tasks, int currentTimeScore) {
        return tasks.stream()
                .filter(task ->
                        task.getTimeScore() != 0 &&
                                task.checkTaskCompleteStatus() &&
                                task.getTimeScore() < currentTimeScore - TIME_THRESHOLD)
                .collect(Collectors.toList());
    }

    /**
     * Splits the tasks into partitions for parallel processing.
     *
     * @param tasks The list of tasks to split.
     * @return The list of partitions.
     */
    private static List<List<TaskClass>> splitTasksIntoPartitions(List<TaskClass> tasks) {
        int partitionSize = (int) Math.ceil((double) tasks.size() / NUM_PARTITIONS);
        List<List<TaskClass>> partitions = new ArrayList<>(4);

        for (int i = 0; i < tasks.size(); i += partitionSize) {
            partitions.add(tasks.subList(i, Math.min(i + partitionSize, tasks.size())));
        }

        return partitions;
    }

    /**
     * Rearranges the task IDs to ensure they are sequential.
     */
    private static void rearrangeTaskIds() {
        IntStream.range(0, tasksInData.size()).forEach(
                i -> tasksInData.get(i).setTaskId(i + 1)
        );
    }
}
