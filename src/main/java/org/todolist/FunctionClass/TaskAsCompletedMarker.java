package org.todolist.FunctionClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FileAccess;
import org.todolist.TaskClass;

import java.util.Scanner;

import static org.todolist.UserMessages.*;

public class TaskAsCompletedMarker extends TodoListManager {

    private static final Logger LOGGER = LogManager.getLogger(TaskAsCompletedMarker.class);
    private static final Scanner scanner = new Scanner(System.in);

    public static void markTaskCompleted() {
        if (tasksInData.isEmpty()) {
            System.out.println(NO_TASK_MSG.getMessage());
            return;
        }

        tasksLister(tasksInData);

        while (true) {

            int inputTaskId = inputTaskId();
            if (inputTaskId == 0) {
                break; // Exit if user chooses to cancel
            }
            TaskClass task = findTaskById(inputTaskId);
            String taskState = taskStateSet(task);
            if (taskState == null) {
                return;
            }

            tasksLister(tasksInData);

            taskStateDisplay(taskState);
        }
    }

    /**
     * Finds a task by its ID.
     *
     * @param taskId The ID of the task to find.
     * @return The task with the specified ID, or null if not found.
     */
    private static TaskClass findTaskById(int taskId) {
        for (TaskClass task : tasksInData) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private static int inputTaskId() {
        System.out.print(PROMPT_ID_TO_MARK_COMPLETE_MSG.getMessage());

        while (!scanner.hasNextInt()) {
            System.out.println(INVALID_ID_TO_MARK_COMPLETE_MSG.getMessage());
            scanner.next(); // clear invalid input
            System.out.print(PROMPT_ID_TO_MARK_COMPLETE_MSG.getMessage());
        }
        return scanner.nextInt();
    }

    private static String taskStateSet(TaskClass task) {
        if (task == null) {
            return "not found";
        } else if (task.checkTaskCompleteStatus()) {
            return "already completed";
        } else {
            task.setTaskCompleteStatus(true);
            try {
                FileAccess.writeDataFile(tasksInData);
            } catch (Exception e) {
                LOGGER.error("Error marking task as completed: ", e);
                return null;
            }
            return "marked as completed";
        }
    }

    private static void taskStateDisplay(String taskState) {
        switch (taskState) {
            case "not found":
                System.out.println(TASK_NOT_FOUND_MSG.getMessage());
                break;
            case "already completed":
                System.out.println(TASK_ALREADY_COMPLETED_MSG.getMessage());
                break;
            case "marked as completed":
                System.out.println(TASK_MARKED_COMPLETE_MSG.getMessage());
                break;
        }
    }
}
