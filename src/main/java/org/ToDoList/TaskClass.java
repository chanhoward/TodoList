package org.ToDoList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskClass(String content, String author, String time) {
    @JsonCreator
    public TaskClass(@JsonProperty("content") String content, @JsonProperty("author") String author, @JsonProperty("time") String time) {
        this.content = content;
        this.author = author;
        this.time = time;
    }
}