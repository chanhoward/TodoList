package org.todolist.FunctionClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.DataIO;
import org.todolist.FuncMenuMgr;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

import static org.todolist.UserMessages.*;

/**
 * TaskAdder class provides functionalities to add tasks to the to-do list.
 */
public class TaskAdder extends FuncMenuMgr {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LogManager.getLogger(TaskAdder.class);
    private static final int MAX_AUTHOR_LENGTH = 30;

    /**
     * Adds a new task to the to-do list.
     * If the task list is full, it prints a message and does not add the task.
     */
    public static void addTask() {
        if (isTasksFull) {
            return;
        }

        String pendingRank = inputRank();
        String content = inputContent();
        String author = inputAuthor();
        TimeClass dueTimeClass = inputDueTime();

        if (dueTimeClass.getTimeScore() == Integer.MAX_VALUE) {
            System.out.println(NO_DUE_TIME_MSG.getMessage());
        }

        String dueTime = formatTime(dueTimeClass);
        String currentTime = new TimeClass().getCurrentTime();
        int taskId = generateTaskId();

        TaskClass newTask = new TaskClass(
                taskId,
                pendingRank,
                content,
                dueTime,
                author,
                currentTime,
                dueTimeClass.getTimeScore(),
                false);

        addTaskToDataFile(newTask);
        System.out.println(TASK_ADDED_MSG.getMessage());
    }

    /**
     * Formats the given TimeClass object into a string representation of the time.
     *
     * @param timeClass The TimeClass object to format.
     * @return A string representation of the time.
     */
    private static String formatTime(TimeClass timeClass) {
        return String.format("%d-%02d-%02d %02d:%02d",
                timeClass.getYear(),
                timeClass.getMonth(),
                timeClass.getDay(),
                timeClass.getHour(),
                timeClass.getMinute());
    }

    /**
     * Generates a new task ID.
     *
     * @return A new task ID.
     */
    private static int generateTaskId() {
        return tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;
    }

    /**
     * Prompts the user to input the rank of the task.
     *
     * @return The rank of the task.
     */
    private static String inputRank() {
        int inputRank;

        do {
            System.out.print(PROMPT_RANK_MSG.getMessage());
            inputRank = readInteger();
            if (inputRank < 1 || inputRank > 3) {
                System.out.println(INVALID_RANK_MSG.getMessage());
            }
        } while (inputRank < 1 || inputRank > 3);

        return switch (inputRank) {
            case 1 -> "High";
            case 2 -> "Medium";
            case 3 -> "Low";
            default -> "Unknown"; // This should never be reached due to the loop condition
        };
    }

    /**
     * Prompts the user to input the content of the task.
     *
     * @return The content of the task.
     */
    private static String inputContent() {
        String content;
        do {
            System.out.print(PROMPT_CONTENT_MSG.getMessage());
            content = scanner.nextLine();
            if (content.isEmpty()) {
                System.out.println(EMPTY_CONTENT_MSG.getMessage());
            }
        } while (content.isEmpty());
        return content;
    }

    /**
     * Prompts the user to input the author of the task.
     * Ensures that the input does not exceed the maximum allowed length.
     * If the input is empty, a default value of "unknown" is used.
     *
     * @return The author of the task. If the input is empty, returns "unknown".
     */
    private static String inputAuthor() {
        Scanner scanner = new Scanner(System.in);
        String author;

        while (true) {
            System.out.printf(PROMPT_AUTHOR_MSG.getMessage(), MAX_AUTHOR_LENGTH);
            author = scanner.nextLine();

            if (author.length() > MAX_AUTHOR_LENGTH) {
                System.out.printf(INVALID_AUTHOR_MSG.getMessage(), MAX_AUTHOR_LENGTH);
            } else {
                break;
            }
        }

        return author.isEmpty() ? "unknown" : author;
    }


    /**
     * Prompts the user to input the due time of the task.
     *
     * @return A TimeClass object representing the due time of the task.
     */
    private static TimeClass inputDueTime() {
        TimeClass timeClass = null;
        boolean validDate = false;

        System.out.println(PROMPT_DUE_DATE_MSG.getMessage());
        while (!validDate) {
            try {
                int year = inputYear();
                if (year == 0) {
                    return new TimeClass(0, 0, 0, 0, 0); // Infinity
                }

                int month = inputMonth();
                int day = inputDay(year, month);
                int hour = inputHour();
                int minute = inputMinute();
                timeClass = new TimeClass(year, month, day, hour, minute);
                validDate = true;
            } catch (DateTimeException e) {
                System.out.println(INVALID_DATE_MSG.getMessage());
            }
        }

        return timeClass;
    }

    /**
     * Prompts the user to input the year.
     *
     * @return The input year.
     */
    private static int inputYear() {
        TimeClass timeClass = new TimeClass();
        int year;
        while (true) {
            System.out.print(PROMPT_YEAR_MSG.getMessage());
            year = readInteger();
            if (year == 0 || year >= timeClass.getCurrentYear()) {
                return year;
            }
            System.out.println(INVALID_YEAR_MSG.getMessage());
        }
    }

    /**
     * Prompts the user to input the month.
     *
     * @return The input month.
     */
    private static int inputMonth() {
        int month;
        while (true) {
            System.out.print(PROMPT_MONTH_MSG.getMessage());
            month = readInteger();
            if (month >= 1 && month <= 12) {
                return month;
            }
            System.out.print(INVALID_MONTH_MSG.getMessage());
        }
    }

    /**
     * Prompts the user to input the day.
     *
     * @param year  The year for validation.
     * @param month The month for validation.
     * @return The input day.
     */
    private static int inputDay(int year, int month) {
        int day;
        while (true) {
            System.out.print(PROMPT_DAY_MSG.getMessage());
            day = readInteger();
            if (isValidDay(year, month, day)) {
                return day;
            }
            System.out.print(INVALID_DAY_MSG.getMessage());
        }
    }

    /**
     * Prompts the user to input the hour.
     *
     * @return The input hour.
     */
    private static int inputHour() {
        int hour;
        while (true) {
            System.out.print(PROMPT_HOUR_MSG.getMessage());
            hour = readInteger();
            if (hour >= 0 && hour <= 23) {
                return hour;
            }
            System.out.print(INVALID_HOUR_MSG.getMessage());
        }
    }

    /**
     * Prompts the user to input the minute.
     *
     * @return The input minute.
     */
    private static int inputMinute() {
        int minute;
        while (true) {
            System.out.print(PROMPT_MINUTE_MSG.getMessage());
            minute = readInteger();
            if (minute >= 0 && minute <= 59) {
                return minute;
            }
            System.out.print(INVALID_MINUTE_MSG.getMessage());
        }
    }

    /**
     * Reads an integer from the user input, handling invalid inputs.
     *
     * @return The input integer.
     */
    private static int readInteger() {
        while (!scanner.hasNextInt()) {
            System.out.println(INVALID_INTEGER_INPUT_MSG.getMessage());
            scanner.next(); // Clear the invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Clear the newline
        return value;
    }

    /**
     * Validates if the given day is valid for the specified year and month.
     *
     * @param year  The year to validate.
     * @param month The month to validate.
     * @param day   The day to validate.
     * @return True if the day is valid, false otherwise.
     */
    private static boolean isValidDay(int year, int month, int day) {
        try {
            LocalDate.of(year, month, day); // Validate the day
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    /**
     * Adds the new task to the data file.
     *
     * @param newTask The task to add.
     */
    private static void addTaskToDataFile(TaskClass newTask) {
        tasksInData.add(newTask);
        try {
            DataIO.writeDataFile(tasksInData);
        } catch (Exception e) {
            LOGGER.error("Error adding task to data file: ", e);
        }
    }
}
