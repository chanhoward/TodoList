package org.todolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeClass {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final LocalDateTime currentTime = LocalDateTime.now();
    private final int year, month, day, hour, minute;
    private final int timeScore;

    // Constructor to initialize with specific values
    public TimeClass(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.timeScore = calculateTimeScore(year, month, day, hour, minute);
    }

    // Default constructor initializing with current time
    public TimeClass() {
        this.year = currentTime.getYear();
        this.month = currentTime.getMonthValue();
        this.day = currentTime.getDayOfMonth();
        this.hour = currentTime.getHour();
        this.minute = currentTime.getMinute();
        this.timeScore = calculateTimeScore(year, month, day, hour, minute);
    }

    /**
     * Calculates a time score based on the provided date and time.
     *
     * @param year   the year
     * @param month  the month (1-12)
     * @param day    the day of the month
     * @param hour   the hour of the day (0-23)
     * @param minute the minute of the hour (0-59)
     * @return the calculated time score
     */
    public static int calculateTimeScore(int year, int month, int day, int hour, int minute) {
        int minutesInHour = 60;
        int minutesInDay = 24 * minutesInHour;
        int minutesInMonth = 30 * minutesInDay; // Approximate
        int minutesInYear = 365 * minutesInDay; // Approximate

        return minute +
                hour * minutesInHour +
                day * minutesInDay +
                month * minutesInMonth +
                year * minutesInYear;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public String getCurrentTime() {
        return currentTime.format(FORMATTER);
    }

    public int getCurrentYear() {
        return currentTime.getYear();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
