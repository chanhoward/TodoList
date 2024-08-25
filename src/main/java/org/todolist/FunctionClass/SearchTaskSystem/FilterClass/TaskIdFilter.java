package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

import static org.todolist.UserMessages.INVALID_ID_FORMAT_MSG;

public class TaskIdFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) {
        try {
            tasksInData.stream()
                    .filter(task -> task.getTaskId() == Integer.parseInt(keyword))
                    .forEach(filteredTasks::add);
        } catch (NumberFormatException e) {
            System.out.println(INVALID_ID_FORMAT_MSG.getMessage());
        }
    }
}
