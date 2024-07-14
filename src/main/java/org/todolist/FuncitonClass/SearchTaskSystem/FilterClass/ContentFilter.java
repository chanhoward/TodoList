package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class ContentFilter extends TaskFilteringUtils {
    public static void contentFilter(String keyword) {
        toBeFilteredTask.stream()
                .filter(task -> task.getContent().contains(keyword))
                .forEach(filteredTask::add);
    }
}
