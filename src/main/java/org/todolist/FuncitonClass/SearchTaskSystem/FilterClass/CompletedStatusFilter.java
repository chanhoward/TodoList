package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class CompletedStatusFilter extends TaskFilteringUtils {
    public static void filterByCompletedStatus(String keyword) {
        String processedKeyword;
        if (keyword.equalsIgnoreCase("y")) {
            processedKeyword = "true";
        } else if (keyword.equalsIgnoreCase("n")) {
            processedKeyword = "false";
        } else {
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
            return;
        }

        toBeFilteredTask.stream()
                .filter(task -> String.valueOf(task.isTaskCompleteStatus()).equalsIgnoreCase(processedKeyword))
                .forEach(filteredTask::add);
    }
}
