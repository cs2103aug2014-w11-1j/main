package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimePair {
    private LocalDate firstDate;
    private LocalTime firstTime;
    private LocalDate secondDate;
    private LocalTime secondTime;
    private int count;
    private int max;

    DateTimePair() {
        count = 0;
        max = 4;
    }

    void add(LocalDate d) {
        if (d == null || isFull()) {
            return;
        }

        // two dates in a row
        if (hasFirstDate() && !hasFirstTime()) {
            max = 3;
        }

        if (!hasFirstDate() && !hasSecondTime()) {
            firstDate = d;
        } else {
            secondDate = d;
        }

        count++;
    }

    void add(LocalTime t) {
        if (t == null || isFull()) {
            return;
        }

        // two times in a row
        if (hasFirstTime() && !hasFirstDate()) {
            max = 3;
        }

        if (!hasFirstTime() && !hasSecondDate()) {
            firstTime = t;
        } else {
            secondTime = t;
        }

        count++;
    }

    boolean isFull() {
        return count == max;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalDate getSecondDate() {
        return secondDate;
    }

    public LocalTime getSecondTime() {
        return secondTime;
    }

    public int getNumOfDates() {
        int first = firstDate == null ? 0 : 1;
        int second = secondDate == null ? 0 : 1;
        return first + second;
    }

    public int getNumOfTimes() {
        int first = firstTime == null ? 0 : 1;
        int second = secondTime == null ? 0 : 1;
        return first + second;
    }

    public boolean hasFirstDate() {
        return firstDate != null;
    }

    public boolean hasFirstTime() {
        return firstTime != null;
    }

    public boolean hasSecondDate() {
        return secondDate != null;
    }

    public boolean hasSecondTime() {
        return secondTime != null;
    }

    public boolean isEmpty() {
        return firstDate == null && firstTime == null;
    }
}
