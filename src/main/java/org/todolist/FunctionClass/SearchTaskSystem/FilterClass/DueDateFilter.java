package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

public class DueDateFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) { //ToDo use more easy input format
        String processedKeyword;
        if (keyword.isEmpty()) {
            processedKeyword = "null";
        } else {
            processedKeyword = keyword;
        }

        tasksInData.stream()
                .filter(task -> task.getDueDate().contains(processedKeyword))
                .forEach(filteredTasks::add);
    }
}
