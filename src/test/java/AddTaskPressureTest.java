import org.todolist.FileAccess;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddTaskPressureTest {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int AMOUNT = scanner.nextInt();
    private static final List<TaskClass> TASKS = new ArrayList<>();
    private static final List<TaskClass> tasksInData = FileAccess.readDataFile();
    private static final TimeClass TIME_CLASS = new TimeClass();
    private static final String currentlyTime = TIME_CLASS.getCurrentTime();
    private static final String pendingRank = "Low";
    private static final String dueTime = null;
    private static final int DUE_TIME_SCORE = 1064147650;
    private static final boolean isDone = true;

    public static void main(String[] args) {
        if (AMOUNT == 0) {
            return;
        }

        int task = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;

        for (int i = 1; i <= AMOUNT; i++) {
            String content = "Task " + task;
            String author = "Author " + task;

            TaskClass newTask = new TaskClass(task, pendingRank, content, dueTime, author, currentlyTime, DUE_TIME_SCORE, isDone);
            TASKS.add(newTask);
            task++;

            if (i % 10 == 0 || i == AMOUNT) {
                printProgressBar(i);
            }

        }
        System.out.println();
        addTaskToDataFile();
        System.out.println("Data written successfully.");

    }

    private static void printProgressBar(int current) {
        int progress = (int) ((double) current / AMOUNT * 50);
        StringBuilder progressBar = new StringBuilder("\r[");

        for (int i = 0; i < 50; i++) {
            progressBar.append(i < progress ? "=" : " ");
        }
        progressBar.append("] ")
                .append(current)
                .append("/")
                .append(AMOUNT)
                .append(" (")
                .append((int) ((double) current / AMOUNT * 100))
                .append("%)");

        System.out.print(progressBar);
    }

    private static void addTaskToDataFile() {
        List<TaskClass> updatedTasks = tasksInData;
        updatedTasks.addAll(TASKS);
        FileAccess.writeDataFile(updatedTasks);
        TASKS.clear();
    }

}