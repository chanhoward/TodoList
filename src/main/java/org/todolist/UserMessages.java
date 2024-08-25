package org.todolist;

public enum UserMessages {
    /*-----------------------------ERROR----------------------------------*/
    FAIL_TO_LOAD_MSG("Failed to load tasks from file."),
    SOME_THING_WRONG_MSG("Something went wrong. :("),
    INVALID_COMMAND_MSG("Invalid command. Please enter a number between 0 and 6."),
    INVALID_INTEGER_INPUT_MSG("Invalid input. Please enter an integer."),
    INVALID_YES_NO_INPUT_MSG("Invalid input. Please enter 'y' or 'n'."),
    INVALID_RANK_MSG("Invalid rank. Please enter a number between 1 and 3."),
    INVALID_AUTHOR_MSG("Author name is too long. Please enter up to %d characters.\n"),
    INVALID_DATE_MSG("Invalid date. Please enter a valid date."),
    INVALID_YEAR_MSG("Invalid year. Setting to current year."),
    INVALID_MONTH_MSG("Invalid month. Please enter a number between 1 and 12."),
    INVALID_DAY_MSG("Invalid day for the given month and year. Please enter a valid day."),
    INVALID_HOUR_MSG("Invalid hour. Please enter a number between 0 and 23."),
    INVALID_MINUTE_MSG("Invalid minute. Please enter a number between 0 and 59."),
    INVALID_ID_TO_MARK_COMPLETE_MSG("Invalid input. Please enter a valid task ID."),
    INVALID_SEARCH_TYPE_MSG("Invalid search type. Please enter a number between 1 and 7."),
    INVALID_ID_FORMAT_MSG("Invalid task ID format. Please enter a valid integer."),
    /*------------------------------PROMPT----------------------------------*/
    PROMPT_COMMAND_MSG("Input command (0-6): "),
    PROMPT_RANK_MSG("Input pending rank High/Medium/Low (1-3): "),
    PROMPT_CONTENT_MSG("Input task content: "),
    PROMPT_AUTHOR_MSG("Input author (max %d characters): "),
    PROMPT_DUE_DATE_MSG("Input due date"),
    PROMPT_YEAR_MSG("Input year: "),
    PROMPT_MONTH_MSG("Input month (1-12): "),
    PROMPT_DAY_MSG("Input day: "),
    PROMPT_HOUR_MSG("Input hour (0-23): "),
    PROMPT_MINUTE_MSG("Input minute (0-59): "),
    PROMPT_ID_TO_REMOVE_MSG("Input task ID to remove: "),
    PROMPT_ID_TO_MARK_COMPLETE_MSG("Input task ID to mark as completed: "),
    PROMPT_SEARCH_TYPE_MSG("Input search type (1-7): "),
    PROMPT_KEYWORD_MSG("Input keyword: "),
    RESET_CONFIRMATION_MSG("Are you sure you want to reset all data? (y/n): "),
    RESTART_TO_RESET_MSG("The program needs to restart to setup all things."),
    RESET_CONFIRM_MSG("Resetting..."),
    /*-----------------------------SUCCESS----------------------------------*/
    TASK_ADDED_MSG("Task added successfully!"),
    TASK_REMOVED_MSG("Task removed successfully!"),
    TASK_MARKED_COMPLETE_MSG("Task marked as completed successfully!"),
    RESET_SUCCESS_MSG("All data has been reset successfully!"),
    /*-----------------------------STATUS----------------------------------*/
    LOADING_MSG("Loading data..."),
    NO_TASK_MSG("There are currently no to-do tasks"),
    TASK_NOT_FOUND_MSG("Task not found."),
    SEARCH_NO_RESULT_MSG("No matching tasks found."),
    TASK_FULL_MSG("""
            
            Task count limit reached(100000). Please remove some tasks before adding new ones.
            Press 6 to auto remove tasks.
            """),
    EMPTY_CONTENT_MSG("Task content cannot be empty."),
    EMPTY_KEYWORD_MSG("Keyword cannot be empty."),
    NO_DUE_TIME_MSG("Task without due time."),
    NO_TASK_TO_REMOVE_MSG("No task to remove."),
    TASK_ALREADY_COMPLETED_MSG("Task is already marked as completed."),
    RESET_CANCEL_MSG("Reset canceled."),
    TOTAL_REMOVED_MSG("Total tasks removed: "),
    FOUND_MATCHING_TASKS_MSG("Found %d matching tasks."),
    /*-----------------------------OBJECT----------------------------------*/
    MAIN_MENU("""
            
            Welcome to your To-Do-List!
            0. Exit
            1. Add a task
            2. Mark task as completed
            3. Remove a task
            4. Search task
            5. List the tasks
            6. Auto remove tasks to clear space
            """),
    SEARCH_MENU("""
            
            0. Exit
            1. Search by content
            2. Search by task Rank (High/Medium/Low) [1/2/3]
            3. Search by due date (XXXX-XX-XX)
            4. Search by completed status (y/n)
            5. Search by author
            6. Search by created date (XXXX-XX-XX)
            7. Search by task ID
            """);

    private final String msg;

    UserMessages(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
