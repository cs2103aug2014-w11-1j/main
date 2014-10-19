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

        if (count == 0) {
            firstDate = d;
        } else if (firstTime == null) {
            secondDate = d;
            max = 3;
        } else if (secondTime == null) {
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

        if (count == 0) {
            firstTime = t;
        } else if (firstDate == null) {
            secondTime = t;
            max = 3;
        } else if (secondDate == null) {
            firstTime = t;
        } else {
            secondTime = t;
        }

        count++;
    }

    boolean isFull() {
        return count == max;
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
}
