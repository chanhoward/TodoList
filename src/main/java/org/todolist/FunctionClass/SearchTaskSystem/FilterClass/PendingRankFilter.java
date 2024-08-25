package org.todolist.FunctionClass.SearchTaskSystem.FilterClass;

import org.todolist.FunctionClass.SearchTaskSystem.SearchMgr;

public class PendingRankFilter extends SearchMgr implements SearchStrategy {
    @Override
    public void executeFilter(String keyword) {
        String processedKeyword;
        switch (keyword) {
            case "1":
                processedKeyword = "High";
                break;
            case "2":
                processedKeyword = "Medium";
                break;
            case "3":
                processedKeyword = "Low";
                break;
            default:
                System.out.println("Invalid rank. Please enter 1, 2, or 3.");
                return;
        }

        tasksInData.stream()
                .filter(task -> task.getPendingRank().equalsIgnoreCase(processedKeyword))
                .forEach(filteredTasks::add);
    }
}
