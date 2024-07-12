package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class TaskIDFilter implements TaskFilteringUtils {
    public static void taskIDFilter(String keyword) {
        try {
            toBeFilteredTask.stream()
                    .filter(task -> task.getTaskID() == Integer.parseInt(keyword))
                    .forEach(filteredTask::add);
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID format. Please enter a valid integer.");
        }
    }
}
