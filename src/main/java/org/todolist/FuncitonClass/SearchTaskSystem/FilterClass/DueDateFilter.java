package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class DueDateFilter extends TaskFilteringUtils {
    public static void filterByDueDate(String keyword) { //ToDo use more easy input format
        String processedKeyword;
        if (keyword.isEmpty()) {
            processedKeyword = "null";
        } else {
            processedKeyword = keyword;
        }

        toBeFilteredTask.stream()
                .filter(task -> task.getDueDate().contains(processedKeyword))
                .forEach(filteredTask::add);
    }
}
