package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class TaskIDFilter extends TaskFilteringUtils {
    public static void taskIDFilter(String keyword) {
        try {
            toBeFilteredTask.stream()
                    .filter(task -> task.getTaskId() == Integer.parseInt(keyword))
                    .forEach(filteredTask::add);
        } catch (NumberFormatException e) {
            System.out.println("Invalid task ID format. Please enter a valid integer.");
        }
    }
}
