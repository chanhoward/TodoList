package org.ToDoList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskClass {
    private final int taskID;
    private final String content;
    private final String author;
    private final String time;
    boolean isTaskCompleted;

    @JsonCreator
    public TaskClass(@JsonProperty("taskID") int taskID,
                     @JsonProperty("content") String content,
                     @JsonProperty("author") String author,
                     @JsonProperty("time") String time,
                     @JsonProperty("isCompleted") boolean isTaskCompleted) {
        this.taskID = taskID;
        this.content = content;
        this.author = author;
        this.time = time;
        this.isTaskCompleted = isTaskCompleted;
    }

    public int getTaskID() {
        return taskID;
    }

    public String getContent() {
        return content;
    }

    /*public void setContent(String content) {
        this.content = content;
    }*/

    public String getAuthor() {
        return author;
    }

    /*public void setAuthor(String author) {
        this.author = author;
    }*/

    public String getTime() {
        return time;
    }

    /*public void setTime(String time) {
        this.time = time;
    }*/

    public boolean getIsTaskCompleted() {
        return isTaskCompleted;
    }

    public void setTaskCompleted() {
        isTaskCompleted = true;
    }
}