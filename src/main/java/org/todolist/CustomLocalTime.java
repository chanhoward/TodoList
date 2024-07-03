package org.todolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalTime {
    private final String formatterTime;

    public CustomLocalTime() {
        // 格式化日期和時間
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.formatterTime = time.format(formatter);
    }

    public final String getTime() {
        return formatterTime;
    }
}
