package org.todolist;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskClass {
    private final String content;
    private final String author;
    private final String time;
    private int taskId;
    private boolean taskCompleteStatus;

    @JsonCreator
    public TaskClass(@JsonProperty("taskId") int taskId,
                     @JsonProperty("content") String content,
                     @JsonProperty("author") String author,
                     @JsonProperty("time") String time,
                     @JsonProperty("taskCompleteStatus") boolean taskCompleteStatus) {
        this.taskId = taskId;
        this.content = content;
        this.author = author;
        this.time = time;
        this.taskCompleteStatus = taskCompleteStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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