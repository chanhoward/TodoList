import org.todolist.FileAccess;
import org.todolist.LocalTime;
import org.todolist.TaskClass;

import java.util.ArrayList;
import java.util.List;

public class AddTaskPressureTest {
    private static final int AMOUNT = 5000000;
    private static final List<TaskClass> TASKS = new ArrayList<>();
    private static final List<TaskClass> tasksInData = FileAccess.readDataFile();
    private static final LocalTime localTime = new LocalTime();
    private static final String currentlyTime = localTime.getTime();

    public static void main(String[] args) {

        int task = tasksInData.isEmpty() ? 1 : tasksInData.size() + 1;
        int taskID;
        for (int i = 1; i <= AMOUNT; i++) {
            String inputContent = "Task " + task;
            String inputAuthor = "Author " + task;
            task++;

            taskID = tasksInData.isEmpty() ? i : tasksInData.size() + i;

            TaskClass newTask = new TaskClass(taskID, inputContent, inputAuthor, currentlyTime, false);
            TASKS.add(newTask);

            printProgressBar(i);
        }

        addTaskToDataFile();
        System.out.println("\n" + AMOUNT + " tasks added successfully!");

        cleanUpTaskList();
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
        FileAccess.writeDataFile(TASKS);
    }

    private static void cleanUpTaskList() {
        TASKS.clear();
    }
}