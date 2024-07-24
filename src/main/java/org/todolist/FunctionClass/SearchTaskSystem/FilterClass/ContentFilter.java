package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

public class ContentFilter extends TaskFilteringUtils {
    public static void filterByContent(String keyword) {
        toBeFilteredTask.stream()
                .filter(task -> task.getContent().contains(keyword))
                .forEach(filteredTask::add);
    }
}
