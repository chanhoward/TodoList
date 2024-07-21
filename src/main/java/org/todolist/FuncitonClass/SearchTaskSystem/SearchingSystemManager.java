package org.todolist.FuncitonClass.SearchTaskSystem;

import org.todolist.FuncitonClass.SearchTaskSystem.FilterClass.TaskFilteringUtils;
import org.todolist.FuncitonClass.TodoListManager;

import java.util.Scanner;

public class SearchingSystemManager extends TodoListManager {

    public static void filterProcessingManager() {
        if (isAccessFail) {
            System.err.println("Failed to access data file.");
            return;
        }

        while (true) {
            TaskFilteringUtils.updateToBeFilteredTasks();
            TaskFilteringUtils.resetFilteredTasks();

            displayMenu();

            int searchType = getUserSearchType();
            if (searchType == 0) {
                break;
            }
            if (searchType > 5) {
                System.out.println("Invalid search type.");
                continue;
            }

            listTasks(tasksInData);
            String keyword = getInputKeyword(searchType);

            filterTypeDistribution(searchType, keyword);

            TaskFilteringUtils.printFilteredTasks();
        }

    }

    private static void displayMenu() {
        String menu = """

                0. Exit
                1. Search by content
                2. Search by task Rank (High/Medium/Low) [1/2/3]
                3. Search by due date (XXXX-XX-XX)
                4. Search by completed status (y/n)
                5. Search by author
                6. Search by created date (XXXX-XX-XX)
                7. Search by task ID
                """;

        System.out.print(menu);
    }

    private static int getUserSearchType() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your search type: ");

            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.println("Invalid input. Please enter a integer.");
                input.next();
            }
        }
    }

    private static String getInputKeyword(int searchType) {
        Scanner input = new Scanner(System.in);
        String inputKeyword;

        //Deal with empty keyword
        do {
            System.out.print("Input keyword: ");
            inputKeyword = input.nextLine();

            if (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5)) {
                System.out.println("Keyword cannot be empty.");
            }
        } while (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5));

        return inputKeyword;
    }

    private static void filterTypeDistribution(int searchType, String keyword) {
        switch (searchType) {
            case 1 -> TaskFilteringUtils.filterByContent(keyword);                    //content
            case 2 -> TaskFilteringUtils.filterByPendingRank(keyword);                //rank
            case 3 -> TaskFilteringUtils.filterByDueDate(keyword);                    //due date
            case 4 -> TaskFilteringUtils.filterByCompletedStatus(keyword);            //status
            case 5 -> TaskFilteringUtils.filterByAuthor(keyword);                     //author
            case 6 -> TaskFilteringUtils.filterByCreatedDate(keyword);                //created date
            case 7 -> TaskFilteringUtils.filterByTaskId(keyword);                     //ID
            default -> System.out.println("Invalid search searchType.");
        }
    }

}
