package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

public class AuthorFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) {
        String processedKeyword;
        if (keyword.isEmpty()) {
            processedKeyword = "unknown";
        } else {
            processedKeyword = keyword;
        }
        tasksInData.stream()
                .filter(task -> task.getAuthor().contains(processedKeyword))
                .forEach(filteredTasks::add);
    }
}
