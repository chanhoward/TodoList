package org.todolist.FunctionClass.SearchTaskSystem;

import org.todolist.FunctionClass.SearchTaskSystem.FilterClass.TaskFilteringUtils;
import org.todolist.FunctionClass.TodoListManager;

import java.util.Scanner;

/**
 * This class manages the task searching system, allowing users to search for tasks based on various criteria.
 */
public class SearchingSystemManager extends TodoListManager {

    /**
     * Manages the filtering process for searching tasks.
     * Displays a menu to the user, handles input, and performs the corresponding filtering operation.
     */
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
            if (searchType > 7) {
                System.out.println("Invalid search type.");
                continue;
            }

            tasksLister(tasksInData);
            String keyword = getInputKeyword(searchType);

            filterTypeDistribution(searchType, keyword);

            TaskFilteringUtils.printFilteredTasks();
        }

    }

    /**
     * Displays the search type menu to the user.
     */
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

    /**
     * Prompts the user to enter the search type and returns the input.
     *
     * @return The search type entered by the user.
     */
    private static int getUserSearchType() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your search type: ");

            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                input.next();
            }
        }
    }

    /**
     * Prompts the user to enter a keyword for the search and returns the input.
     *
     * @param searchType The type of search being performed.
     * @return The keyword entered by the user.
     */
    private static String getInputKeyword(int searchType) {
        Scanner input = new Scanner(System.in);
        String inputKeyword;

        // Deal with empty keyword
        do {
            System.out.print("Input keyword: ");
            inputKeyword = input.nextLine();

            if (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5)) {
                System.out.println("Keyword cannot be empty.");
            }
        } while (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5));

        return inputKeyword;
    }

    /**
     * Distributes the search type to the appropriate filtering method.
     *
     * @param searchType The type of search being performed.
     * @param keyword    The keyword to be used for filtering.
     */
    private static void filterTypeDistribution(int searchType, String keyword) {
        switch (searchType) {
            case 1 -> TaskFilteringUtils.filterByContent(keyword);                    // content
            case 2 -> TaskFilteringUtils.filterByPendingRank(keyword);                // rank
            case 3 -> TaskFilteringUtils.filterByDueDate(keyword);                    // due date
            case 4 -> TaskFilteringUtils.filterByCompletedStatus(keyword);            // status
            case 5 -> TaskFilteringUtils.filterByAuthor(keyword);                     // author
            case 6 -> TaskFilteringUtils.filterByCreatedDate(keyword);                // created date
            case 7 -> TaskFilteringUtils.filterByTaskId(keyword);                     // ID
            default -> System.out.println("Invalid search type.");
        }
    }
}
