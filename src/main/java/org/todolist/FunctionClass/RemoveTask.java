package org.todolist.FunctionClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FileAccess;

import java.util.Scanner;
import java.util.stream.IntStream;

import static org.todolist.UserMessages.*;

/**
 * This class is responsible for removing tasks from the to-do list.
 * It includes methods to input the task ID, delete the task, and rearrange task IDs.
 */
public class RemoveTask extends TodoListManager {
    private static final Logger LOGGER = LogManager.getLogger(RemoveTask.class);

    /**
     * Prompts the user to input a task ID and deletes the corresponding task.
     * Continues to prompt the user until a valid task ID is entered or the user chooses to exit.
     */
    public static void inputAndDeleteTask() {
        while (true) {
            if (tasksInData.isEmpty()) {
                System.out.println(NO_TASK_TO_REMOVE_MSG.getMessage());
                break;
            }

            int index = inputIndex();
            if (index == 0) {
                break;
            }

            if (index < 1 || index > tasksInData.size()) {
                System.out.println(TASK_NOT_FOUND_MSG.getMessage());
                continue;
            }

            deleteTaskById(index);
            System.out.println(TASK_REMOVED_MSG.getMessage());
            break;
        }
    }

    /**
     * Prompts the user to input the task ID to be removed.
     *
     * @return The task ID entered by the user.
     */
    private static int inputIndex() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            tasksLister(tasksInData);
            System.out.print(PROMPT_ID_TO_REMOVE_MSG.getMessage());

            if (scanner.hasNextInt()) {
                int command = scanner.nextInt();
                scanner.nextLine();
                return command;
            } else {
                System.out.println(INVALID_INTEGER_INPUT_MSG.getMessage());
                scanner.nextLine();
            }
        }
    }

    /**
     * Deletes the task with the specified task ID from the to-do list.
     *
     * @param index The task ID of the task to be deleted.
     */
    public static void deleteTaskById(int index) {
        tasksInData.removeIf(task -> task.getTaskId() == index);
        rearrangeTasksId();
        try {
            FileAccess.writeDataFile(tasksInData);
        } catch (Exception e) {
            LOGGER.error("Error occurred while deleting the task: ", e);
        }
    }

    /**
     * Rearranges the task IDs of the remaining tasks in the to-do list to maintain a continuous sequence.
     */
    private static void rearrangeTasksId() {
        IntStream.range(0, tasksInData.size()).forEach(
                i -> tasksInData.get(i).setTaskId(i + 1)
        );
    }
}
