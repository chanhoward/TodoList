package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class AuthorFilter implements TaskFilteringUtils {
    public static void authorFilter(String keyword) {
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
