package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

import static org.todolist.UserMessages.INVALID_YES_NO_INPUT_MSG;

public class CompFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) {
        String processedKeyword;
        if (keyword.equalsIgnoreCase("y")) {
            processedKeyword = "true";
        } else if (keyword.equalsIgnoreCase("n")) {
            processedKeyword = "false";
        } else {
            System.out.println(INVALID_YES_NO_INPUT_MSG.getMessage());
            return;
        }

        tasksInData.stream()
                .filter(task -> String.valueOf(task.checkTaskCompleteStatus()).equalsIgnoreCase(processedKeyword))
                .forEach(filteredTasks::add);
    }
}
