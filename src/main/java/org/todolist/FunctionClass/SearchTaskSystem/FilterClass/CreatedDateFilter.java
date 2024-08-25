package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

public class CreatedDateFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) { //ToDo use more easy input format
        tasksInData.stream()
                .filter(task -> task.getCreatedDate().contains(keyword))
                .forEach(filteredTasks::add);
    }
}
