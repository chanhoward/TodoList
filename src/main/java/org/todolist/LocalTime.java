package org.todolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalTime {
    private final String formatterTime;

    public LocalTime() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.formatterTime = time.format(formatter);
    }

    public final String getTime() {
        return formatterTime;
    }
}
