package org.todolist.FuncitonClass.SearchTaskSystem;

import org.todolist.FuncitonClass.ListTasks;
import org.todolist.FuncitonClass.SearchTaskSystem.FilterClass.TaskFilteringUtils;
import org.todolist.FuncitonClass.TodoListManager;

import java.util.Scanner;

public class SearchingSystemManager extends TodoListManager {

    public static void filterProcessingManager() {

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

            ListTasks.listTasks();
            String keyword = getInputKeyword(searchType);

            filterTypeDistribution(searchType, keyword);

            TaskFilteringUtils.printFilteredTasks();
        }

    }

    private static void displayMenu() {
        String menu = """

                0. Exit
                1. Search by content
                2. Search by author
                3. Search by created date (XXXX-XX-XX)
                4. Search by task Rank (High/Medium/Low) [1/2/3]
                5. Search by completed status (y/n)
                6. Search by task ID
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

            if (inputKeyword.isEmpty() && searchType != 2) {
                System.out.println("Keyword cannot be empty.");
            }
        } while (inputKeyword.isEmpty() && searchType != 2);

        return inputKeyword;
    }

    private static void filterTypeDistribution(int searchType, String keyword) {
        switch (searchType) {
            case 1 -> TaskFilteringUtils.contentFilter(keyword);                    //content
            case 2 -> TaskFilteringUtils.authorFilter(keyword);                     //author
            case 3 -> TaskFilteringUtils.createdDateFilter(keyword);                //date
            case 4 -> TaskFilteringUtils.pendingRankFilter(keyword);                //rank
            case 5 -> TaskFilteringUtils.completedStatusFilter(keyword);            //status
            case 6 -> TaskFilteringUtils.taskIdFilter(keyword);                     //ID
            default -> System.out.println("Invalid search searchType.");
        }
    }

}
