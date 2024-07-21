package org.todolist.FuncitonClass.SearchTaskSystem.FilterClass;

public class PendingRankFilter extends TaskFilteringUtils {
    public static void filterByPendingRank(String keyword) {
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

        toBeFilteredTask.stream()
                .filter(task -> task.getPendingRank().equalsIgnoreCase(processedKeyword))
                .forEach(filteredTask::add);
    }
}
