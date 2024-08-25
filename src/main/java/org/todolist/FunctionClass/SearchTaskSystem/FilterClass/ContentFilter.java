package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

public class ContentFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) {
        tasksInData.stream()
                .filter(task -> task.getContent().contains(keyword))
                .forEach(filteredTasks::add);
    }
}
