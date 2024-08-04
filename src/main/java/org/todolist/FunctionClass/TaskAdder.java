package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * TaskAdder class provides functionalities to add tasks to the to-do list.
 */
public class TaskAdder extends TodoListManager {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int MAX_AUTHOR_LENGTH = 30;

    private static final String PROMPT_YEAR = "Input year: ";
    private static final String PROMPT_MONTH = "Input month (1-12): ";
    private static final String PROMPT_DAY = "Input day: ";
    private static final String PROMPT_HOUR = "Input hour (0-23): ";
    private static final String PROMPT_MINUTE = "Input minute (0-59): ";
    private static final String INVALID_INPUT_MSG = "Invalid input. Please enter an integer.";
    private static final String INVALID_YEAR_MSG = "Invalid year. Setting to current year.";
    private static final String INVALID_MONTH_MSG = "Invalid month. Please enter a number between 1 and 12.";
    private static final String INVALID_DAY_MSG = "Invalid day for the given month and year. Please enter a valid day.";
    private static final String INVALID_HOUR_MSG = "Invalid hour. Please enter a number between 0 and 23.";
    private static final String INVALID_MINUTE_MSG = "Invalid minute. Please enter a number between 0 and 59.";
    private static final String NO_DUE_TIME_MSG = "Task without due time";
    private static final String TASK_ADDED_MSG = "Task added successfully!";

    /**
     * Adds a new task to the to-do list.
     * If the task list is full, it prints a message and does not add the task.
     */
    public static void addTask() {
        if (isTasksFull) {
            System.out.println("Task list is full. Cannot add more tasks.");
            return;
        }

        String pendingRank = inputRank();
        String content = inputContent();
        String author = inputAuthor();
        TimeClass dueTimeClass = inputDueTime();

        if (dueTimeClass.getTimeScore() == Integer.MAX_VALUE) {
            System.out.println(NO_DUE_TIME_MSG);
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
                false
        );

        addTaskToDataFile(newTask);
        System.out.println(TASK_ADDED_MSG);
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
        final String promptMessage = "Input pending rank High/Medium/Low (1-3): ";
        final String invalidInputMessage = "Invalid input. Please enter a number between 1 and 3.";
        int inputRank;

        do {
            System.out.print(promptMessage);
            inputRank = readInteger(invalidInputMessage);
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
            System.out.print("Input content: ");
            content = scanner.nextLine();
            if (content.isEmpty()) {
                System.out.println("Content cannot be empty.");
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
            System.out.print("Input author (max " + MAX_AUTHOR_LENGTH + " characters): ");
            author = scanner.nextLine();

            if (author.length() > MAX_AUTHOR_LENGTH) {
                System.out.println("Author name is too long. Please enter up to " + MAX_AUTHOR_LENGTH + " characters.");
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

        System.out.println("Input due date");
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
                System.out.println("Invalid date. Please enter a valid date.");
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
            System.out.print(PROMPT_YEAR);
            year = readInteger(INVALID_INPUT_MSG);
            if (year == 0 || year >= timeClass.getCurrentYear()) {
                return year;
            }
            System.out.println(INVALID_YEAR_MSG);
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
            System.out.print(PROMPT_MONTH);
            month = readInteger(INVALID_INPUT_MSG);
            if (month >= 1 && month <= 12) {
                return month;
            }
            System.out.println(INVALID_MONTH_MSG);
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
            System.out.print(PROMPT_DAY);
            day = readInteger(INVALID_INPUT_MSG);
            if (isValidDay(year, month, day)) {
                return day;
            }
            System.out.println(INVALID_DAY_MSG);
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
            System.out.print(PROMPT_HOUR);
            hour = readInteger(INVALID_INPUT_MSG);
            if (hour >= 0 && hour <= 23) {
                return hour;
            }
            System.out.println(INVALID_HOUR_MSG);
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
            System.out.print(PROMPT_MINUTE);
            minute = readInteger(INVALID_INPUT_MSG);
            if (minute >= 0 && minute <= 59) {
                return minute;
            }
            System.out.println(INVALID_MINUTE_MSG);
        }
    }

    /**
     * Reads an integer from the user input, handling invalid inputs.
     *
     * @param errorMessage The error message to display in case of invalid input.
     * @return The input integer.
     */
    private static int readInteger(String errorMessage) {
        while (!scanner.hasNextInt()) {
            System.out.println(errorMessage);
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
        FileAccess.writeDataFile(tasksInData);
    }
}
