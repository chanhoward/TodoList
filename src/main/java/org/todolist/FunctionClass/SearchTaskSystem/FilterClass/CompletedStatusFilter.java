package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import static org.todolist.UserMessages.INVALID_YES_NO_INPUT_MSG;

public class CompletedStatusFilter extends TaskFilteringUtils {
    public static void filterByCompletedStatus(String keyword) {
        String processedKeyword;
        if (keyword.equalsIgnoreCase("y")) {
            processedKeyword = "true";
        } else if (keyword.equalsIgnoreCase("n")) {
            processedKeyword = "false";
        } else {
            System.out.println(INVALID_YES_NO_INPUT_MSG.getMessage());
            return;
        }

        toBeFilteredTask.stream()
                .filter(task -> String.valueOf(task.checkTaskCompleteStatus()).equalsIgnoreCase(processedKeyword))
                .forEach(filteredTask::add);
    }
}
