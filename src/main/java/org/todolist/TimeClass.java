package org.todolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeClass {
    private final LocalDateTime currentTime = LocalDateTime.now();
    private final String formatterTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    private final int year, month, day, hour, minute;
    private int timeScore;

    public TimeClass(int timeScore, int year, int month, int day, int hour, int minute) {
        this.timeScore = timeScore;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public TimeClass() {
        this.year = currentTime.getYear();
        this.month = currentTime.getMonthValue();
        this.day = currentTime.getDayOfMonth();
        this.hour = currentTime.getHour();
        this.minute = currentTime.getMinute();
    }

    public static int calculateTimeScore(int year, int month, int day, int hour, int minute) {
        int minutesInHour = 60;
        int minutesInDay = 24 * minutesInHour;
        int minutesInMonth = 30 * minutesInDay;
        int minutesInYear = 365 * minutesInDay;

        return minute +
                hour * minutesInHour +
                day * minutesInDay +
                month * minutesInMonth +
                year * minutesInYear;
    }

    public final int getTimeScore() {
        return timeScore;
    }

    public final String getCurrentTime() {
        return formatterTime;
    }

    public final int getCurrentYear() {
        return currentTime.getYear();
    }

    public final int getYear() {
        return year;
    }

    public final int getMonth() {
        return month;
    }

    public final int getDay() {
        return day;
    }

    public final int getHour() {
        return hour;
    }

    public final int getMinute() {
        return minute;
    }

}
