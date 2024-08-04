package org.todolist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class TaskClass implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String pendingRank;
    private final String content;
    private final String dueDate;
    private final String author;
    private final String createdDate;
    private final int timeScore;
    private int taskId;
    private boolean taskCompleteStatus;

    /**
     * Creates a new task.
     *
     * @param taskId             the task ID
     * @param pendingRank        the pending rank (e.g., High, Medium, Low)
     * @param content            the content of the task
     * @param dueDate            the due date of the task
     * @param author             the author of the task
     * @param createdDate        the creation date of the task
     * @param timeScore          the time score representing the due time
     * @param taskCompleteStatus the completion status of the task
     */
    @JsonCreator
    public TaskClass(@JsonProperty("taskId") int taskId,
                     @JsonProperty("pendingRank") String pendingRank,
                     @JsonProperty("content") String content,
                     @JsonProperty("dueDate") String dueDate,
                     @JsonProperty("author") String author,
                     @JsonProperty("createdDate") String createdDate,
                     @JsonProperty("dueTimeScore") int timeScore,
                     @JsonProperty("taskCompleteStatus") boolean taskCompleteStatus) {
        this.taskId = taskId;
        this.pendingRank = pendingRank;
        this.content = content;
        this.dueDate = dueDate;
        this.author = author;
        this.createdDate = createdDate;
        this.timeScore = timeScore;
        this.taskCompleteStatus = taskCompleteStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public String getPendingRank() {
        return pendingRank;
    }

    public String getContent() {
        return content;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean isTaskCompleteStatus() {
        return taskCompleteStatus;
    }

    public void setTaskCompleteStatus(boolean taskCompleteStatus) {
        this.taskCompleteStatus = taskCompleteStatus;
    }
}