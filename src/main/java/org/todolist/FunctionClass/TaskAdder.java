package org.todolist.FunctionClass;

import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class TaskAdder extends TodoListManager {

    private static final Scanner scanner = new Scanner(System.in);

    public static void addTask() {
        if (isTasksFull) {
            System.out.println("Task list is full. Cannot add more tasks.");
            return;
        }

        String pendingRank = inputRank();
        String content = inputContent();
        String author = inputAuthor();
        TimeClass dueTimeClass = inputDueTime();
        if (dueTimeClass.getTimeScore() == 999999999) {
            System.out.println("Task's due time is infinity");
        }

        String dueTime = dueTimeClass.getYear() +
                "-" +
                dueTimeClass.getMonth() +
                "-" +
                dueTimeClass.getDay() +
                " " +
                dueTimeClass.getHour() +
                ":" +
                dueTimeClass.getMinute();

        TimeClass currentTimeClass = new TimeClass();
        String currentTime = currentTimeClass.getCurrentTime();
        int taskId = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;

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
        System.out.println("Task added successfully!");
    }

    private static String inputRank() {
        int inputRank;
        do {
            System.out.println("Input pending rank High/Medium/Low (1-3): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.next();
            }
            inputRank = scanner.nextInt();
            scanner.nextLine(); // clear the newline
        } while (inputRank < 1 || inputRank > 3);

        return switch (inputRank) {
            case 1 -> "High";
            case 2 -> "Medium";
            case 3 -> "Low";
            default -> "Unknown";
        };
    }

    private static String inputContent() {
        String inputContent;
        do {
            System.out.println("Input content: ");
            inputContent = scanner.nextLine();
            if (inputContent.isEmpty()) {
                System.out.println("Content cannot be empty.");
            }
        } while (inputContent.isEmpty());
        return inputContent;
    }

    private static String inputAuthor() {
        System.out.println("Input author: ");
        String inputAuthor = scanner.nextLine();
        return inputAuthor.isEmpty() ? "unknown" : inputAuthor;
    }

    private static TimeClass inputDueTime() {
        TimeClass timeClass = null;
        boolean validDate = false;

        System.out.println("Input due date");
        while (!validDate) {
            try {
                int year = inputYear();
                if (year == 0) {
                    return new TimeClass(999999999, 0, 0, 0, 0, 0);
                }

                int month = inputMonth();
                int day = inputDay(year, month);
                int hour = inputHour();
                int minute = inputMinute();
                int dueTimeScore = TimeClass.calculateTimeScore(year, month, day, hour, minute);
                timeClass = new TimeClass(dueTimeScore, year, month, day, hour, minute);
                validDate = true;
            } catch (DateTimeException e) {
                System.out.println("Invalid date. Please enter a valid date.");
            }
        }

        return timeClass;
    }

    private static int inputYear() {
        TimeClass timeClass = new TimeClass();
        int year;
        do {
            System.out.print("Input year: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
            year = scanner.nextInt();
            scanner.nextLine(); // clear the newline
            if (year == 0 || year >= timeClass.getCurrentYear()) {
                return year;
            } else {
                System.out.println("Invalid year. Setting to current year.");
            }
        } while (true);
    }

    private static int inputMonth() {
        int month;
        do {
            System.out.print("Input month: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
            month = scanner.nextInt();
            scanner.nextLine(); // clear the newline
        } while (month < 1 || month > 12);
        return month;
    }

    private static int inputDay(int year, int month) {
        int day;
        do {
            System.out.print("Input day: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
            day = scanner.nextInt();
            scanner.nextLine(); // clear the newline
            try {
                LocalDate.of(year, month, day); // Validate the day
                break;
            } catch (DateTimeException e) {
                System.out.println("Invalid day for the given month and year. Please enter a valid day.");
            }
        } while (true);
        return day;
    }

    private static int inputHour() {
        int hour;
        do {
            System.out.println("Input hour (0-23): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
            hour = scanner.nextInt();
            scanner.nextLine();
        } while (hour < 0 || hour > 23);
        return hour;
    }

    private static int inputMinute() {
        int minute;
        do {
            System.out.println("Input minute (0-59): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next();
            }
            minute = scanner.nextInt();
            scanner.nextLine();
        } while (minute < 0 || minute > 59);
        return minute;
    }

    private static void addTaskToDataFile(TaskClass newTask) {
        tasksInData.add(newTask);
        FileAccess.writeDataFile(tasksInData);
    }
}
