package org.todolist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskClass {
    private final String content;
    private final String author;
    private final String time;
    private int taskID;
    private boolean taskCompleteStatus;

    @JsonCreator
    public TaskClass(@JsonProperty("taskID") int taskID,
                     @JsonProperty("content") String content,
                     @JsonProperty("author") String author,
                     @JsonProperty("time") String time,
                     @JsonProperty("taskCompleteStatus") boolean taskCompleteStatus) {
        this.taskID = taskID;
        this.content = content;
        this.author = author;
        this.time = time;
        this.taskCompleteStatus = taskCompleteStatus;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public boolean isTaskCompleteStatus() {
        return taskCompleteStatus;
    }

    public void setTaskCompleteStatus(boolean taskCompleteStatus) {
        this.taskCompleteStatus = taskCompleteStatus;
    }
}