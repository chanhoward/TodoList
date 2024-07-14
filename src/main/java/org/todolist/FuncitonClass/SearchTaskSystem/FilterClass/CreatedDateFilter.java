package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class CreatedDateFilter extends TaskFilteringUtils {
    public static void createdDateFilter(String keyword) { //ToDo use more easy input format
        toBeFilteredTask.stream()
                .filter(task -> task.getTime().contains(keyword))
                .forEach(filteredTask::add);
    }
}
