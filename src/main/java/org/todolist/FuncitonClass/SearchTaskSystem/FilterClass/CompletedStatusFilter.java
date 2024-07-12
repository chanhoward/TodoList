package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class CompletedStatusFilter implements TaskFilteringUtils {
    public static void completedStatusFilter(String keyword) {
        String processedKeyword;
        if (keyword.equalsIgnoreCase("y")) {
            processedKeyword = "true";
        } else if (keyword.equalsIgnoreCase("n")) {
            processedKeyword = "false";
        } else {
            processedKeyword = "";
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }

        toBeFilteredTask.stream()
                .filter(task -> String.valueOf(task.isTaskCompleteStatus()).equalsIgnoreCase(processedKeyword))
                .forEach(filteredTask::add);
    }
}
