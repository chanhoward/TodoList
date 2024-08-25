package org.todolist.FunctionClass.SearchTaskSystem;

import org.todolist.FuncMenuMgr;
import org.todolist.FunctionClass.SearchTaskSystem.FilterClass.*;
import org.todolist.TaskClass;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

import static org.todolist.FunctionClass.TasksLister.listTasks;
import static org.todolist.UserMessages.*;

public class SearchMgr extends FuncMenuMgr {

    public static final Deque<TaskClass> filteredTasks = new ArrayDeque<>(1000);

    public static void filterController() {
        if (isLoadFail) {
            System.err.println(FAIL_TO_LOAD_MSG.getMessage());
            return;
        }

        while (true) {
            displayMenu();

            int searchType = getUserSearchType();
            if (searchType == 0) {
                break;
            }
            if (searchType < 1 || searchType > 7) {
                System.out.println(INVALID_SEARCH_TYPE_MSG.getMessage());
                continue;
            }

            listTasks(tasksInData);

            String keyword = getInputKeyword(searchType);
            SearchStrategy filterStrategy = getFilterStrategy(searchType);
            filterStrategy.executeFilter(keyword);

            printFilteredTasks();
            resetFilteredTasksList();
            System.gc();
        }
    }

    private static void displayMenu() {
        System.out.print(SEARCH_MENU.getMessage());
    }

    private static int getUserSearchType() {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print(PROMPT_SEARCH_TYPE_MSG.getMessage());

            if (input.hasNextInt()) {
                return input.nextInt();
            } else {
                System.out.println(INVALID_INTEGER_INPUT_MSG.getMessage());
                input.next();
            }
        }
    }

    private static String getInputKeyword(int searchType) {
        Scanner input = new Scanner(System.in);
        String inputKeyword;

        do {
            System.out.print(PROMPT_KEYWORD_MSG.getMessage());
            inputKeyword = input.nextLine();

            if (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5)) {
                System.out.println(EMPTY_KEYWORD_MSG.getMessage());
            }
        } while (inputKeyword.isEmpty() && (searchType != 3 && searchType != 5));

        return inputKeyword;
    }

    private static SearchStrategy getFilterStrategy(int searchType) {
        return switch (searchType) {
            case 1 -> new ContentFilter();
            case 2 -> new PendingRankFilter();
            case 3 -> new DueDateFilter();
            case 4 -> new CompFilter();
            case 5 -> new AuthorFilter();
            case 6 -> new CreatedDateFilter();
            case 7 -> new TaskIdFilter();
            default -> throw new IllegalArgumentException("Invalid search type");   //This should not happen
        };
    }

    private static void printFilteredTasks() {
        if (filteredTasks.isEmpty()) {
            System.out.println(SEARCH_NO_RESULT_MSG.getMessage());
        } else {
            listTasks(filteredTasks);
            System.out.printf(FOUND_MATCHING_TASKS_MSG.getMessage(), filteredTasks.size());
        }
    }

    private static void resetFilteredTasksList() {
        filteredTasks.clear();
    }
}