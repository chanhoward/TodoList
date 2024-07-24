package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

public class AuthorFilter extends TaskFilteringUtils {
    public static void filterByAuthor(String keyword) {
        String processedKeyword;
        if (keyword.isEmpty()) {
            processedKeyword = "unknown";
        } else {
            processedKeyword = keyword;
        }
        toBeFilteredTask.stream()
                .filter(task -> task.getAuthor().contains(processedKeyword))
                .forEach(filteredTask::add);
    }
}
