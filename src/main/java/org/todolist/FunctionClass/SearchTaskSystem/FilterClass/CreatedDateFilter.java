package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

public class CreatedDateFilter extends TaskFilteringUtils {
    public static void filterByCreatedDate(String keyword) { //ToDo use more easy input format
        toBeFilteredTask.stream()
                .filter(task -> task.getCreatedDate().contains(keyword))
                .forEach(filteredTask::add);
    }
}
