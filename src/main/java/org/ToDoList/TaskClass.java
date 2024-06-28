package org.ToDoList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public record TaskClass(String time, String content, String author) {
    @JsonCreator
    public TaskClass(@JsonProperty("time") String time, @JsonProperty("content") String content, @JsonProperty("author") String author) {
        this.time = time;
        this.content = content;
        this.author = author;
    }
}