package org.ToDoList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskClass(int taskId, String content, String author, String time) {
    @JsonCreator
    public TaskClass(@JsonProperty("taskId") int taskId,
                     @JsonProperty("content") String content,
                     @JsonProperty("author") String author,
                     @JsonProperty("time") String time) {
        this.taskId = taskId;
        this.content = content;
        this.author = author;
        this.time = time;
    }

    public int getTaskId() {
        return taskId;
    }
}