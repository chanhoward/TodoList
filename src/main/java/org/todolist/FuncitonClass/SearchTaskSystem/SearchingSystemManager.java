package org.todolist.FuncitonClass.SearchTaskSystem;

import org.todolist.FuncitonClass.ListTasks;
import org.todolist.FuncitonClass.SearchTaskSystem.FilterClass.TaskFilteringUtils;
import org.todolist.FuncitonClass.TodoListManager;

import java.util.Scanner;

public class SearchingSystemManager implements TodoListManager {

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
        System.out.println();
        System.out.println("0. Exit");
        System.out.println("1. Search by content");
        System.out.println("2. Search by author");
        System.out.println("3. Search by created date (XXXX-XX-XX)");
        System.out.println("4. Search by task ID");
        System.out.println("5. Search by completed status (y/n)");
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
            case 4 -> TaskFilteringUtils.taskIDFilter(keyword);                     //ID
            case 5 -> TaskFilteringUtils.completedStatusFilter(keyword);            //status
            default -> System.out.println("Invalid search searchType.");
        }
    }

}
