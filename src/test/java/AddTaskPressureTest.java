import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.todolist.DataIO;
import org.todolist.ResetAllFile;
import org.todolist.TaskClass;
import org.todolist.TimeClass;

import java.util.*;

public class AddTaskPressureTest {
    private static final Logger LOGGER = LogManager.getLogger(AddTaskPressureTest.class);
    private static final Deque<TaskClass> TASKS = new ArrayDeque<>(10000);
    private static final TimeClass TIME_CLASS = new TimeClass();
    private static final String currentlyTime = TIME_CLASS.getCurrentTime();
    private static final String pendingRank = "Low";
    private static final String dueTime = null;
    private static final int dueYear = 2024;
    private static final int dueMonth = 7;
    private static final int dueDay = 22;
    private static final int dueTimeScore = dueYear * 365 * 24 * 60 + dueMonth * 30 * 24 * 60 + dueDay * 24 * 60;
    private static final boolean isDone = true;
    private static final int NUM_PARTITIONS = 4;
    private static int amount;
    private static Deque<TaskClass> tasksInData;

    static {
        try {
            tasksInData = DataIO.readDataFile();
        } catch (Exception e) {
            LOGGER.error("Error occurred while reading data file: ", e);
            ResetAllFile.resetAllFile();
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the amount of tasks to add: ");
            amount = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            LOGGER.error("Invalid input for amount: ", e);
            amount = 0;
        }
    }

    public static void main(String[] args) {
        LOGGER.info("AddTaskPressureTest started");
        try {
            if (amount == 0) {
                return;
            }

            int partitionSize = (int) Math.ceil((double) amount / NUM_PARTITIONS);

            List<Deque<TaskClass>> partitions = new ArrayList<>(NUM_PARTITIONS);

            for (int i = 0; i < NUM_PARTITIONS; i++) {
                partitions.add(new ArrayDeque<>(NUM_PARTITIONS));
            }

            int taskId = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;
            int partitionIndex = 0;

            for (int i = 1; i <= amount; i++) {
                String content = "Task " + taskId;
                String author = "Author " + taskId;

                TaskClass newTask = new TaskClass(
                        taskId,
                        pendingRank,
                        content,
                        dueTime,
                        author,
                        currentlyTime,
                        dueTimeScore,
                        isDone);

                partitions.get(partitionIndex).addLast(newTask);
                taskId++;
                if (i % 10 == 0) {
                    printProgressBar(i);
                }
                if (i % partitionSize == 0 || i == amount) {
                    partitionIndex++;
                }
            }
            System.out.println();

            for (Deque<TaskClass> partition : partitions) {
                TASKS.addAll(partition);
                partition.clear();
            }

            addTaskToDataFile();
        } catch (Exception e) {
            LOGGER.error("Error occurred during shutdown: ", e);
        }

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
        System.out.flush();
    }

    private static void addTaskToDataFile() {
        Deque<TaskClass> updatedTasks = tasksInData;
        updatedTasks.addAll(TASKS);
        try {
            DataIO.writeDataFile(updatedTasks);
        } catch (Exception e) {
            LOGGER.error("Error occurred while writing data file: ", e);
        } finally {
            tasksInData.clear();
            updatedTasks.clear();
            TASKS.clear();
            System.gc();
        }
    }

}