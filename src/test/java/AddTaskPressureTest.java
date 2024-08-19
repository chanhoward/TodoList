import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.FileAccess;
import org.todolist.ResetAllFile;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddTaskPressureTest {
    private static final Logger LOGGER = LogManager.getLogger(AddTaskPressureTest.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<TaskClass> TASKS = new ArrayList<>(100000);
    private static final TimeClass TIME_CLASS = new TimeClass();
    private static final String currentlyTime = TIME_CLASS.getCurrentTime();
    private static final String pendingRank = "Low";
    private static final String dueTime = null;
    private static final int dueYear = 2024;
    private static final int dueMonth = 7;
    private static final int dueDay = 22;
    private static final int dueTimeScore = dueYear * 365 * 24 * 60 + dueMonth * 30 * 24 * 60 + dueDay * 24 * 60;
    private static final boolean isDone = true;
    private static int amount;
    private static List<TaskClass> tasksInData;

    static {
        try {
            tasksInData = FileAccess.readDataFile();
        } catch (Exception e) {
            LOGGER.error("Error occurred while reading data file: ", e);
            ResetAllFile.resetAllFile();
        }

        try {
            System.out.print("Enter the amount of tasks to add: ");
            amount = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            LOGGER.error("Invalid input for amount: ", e);
            amount = 0;
        }
    }

    public static void main(String[] args) {
        if (amount == 0) {
            return;
        }

        int task = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;

        for (int i = 1; i <= amount; i++) {
            String content = "Task " + task;
            String author = "Author " + task;

            TaskClass newTask = new TaskClass(
                    task,
                    pendingRank,
                    content,
                    dueTime,
                    author,
                    currentlyTime,
                    dueTimeScore,
                    isDone);
            TASKS.add(newTask);
            task++;

            if (i % 10 == 0 || i == amount) {
                printProgressBar(i);
            }

        }
        System.out.println();
        addTaskToDataFile();
        System.out.println("Data written successfully.");

    }

    private static void printProgressBar(int current) {
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

    private static void addTaskToDataFile() {
        List<TaskClass> updatedTasks = tasksInData;
        updatedTasks.addAll(TASKS);
        try {
            FileAccess.writeDataFile(updatedTasks);
        } catch (Exception e) {
            LOGGER.error("Error occurred while writing data file: ", e);
        }
        TASKS.clear();
    }

}