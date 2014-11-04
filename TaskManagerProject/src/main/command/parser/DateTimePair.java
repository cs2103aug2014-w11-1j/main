package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimePair {


    protected DatePair dates;
    protected TimePair times;

    DateTimePair() {
        dates = new DatePair();
        times = new TimePair();
    }

    DateTimePair(DatePair dates, TimePair times) {
        this.dates = dates;
        this.times = times;
    }

    void add(LocalDate d) {
        if (isFull()) {
            return;
        }

        dates.add(d);
    }

    void add(LocalTime t) {
        if (isFull()) {
            return;
        }

        times.add(t);
    }

    boolean isFull() {
        return dates.isFull() && times.isFull();
    }

    public LocalDate getFirstDate() {
        return dates.getFirstDate();
    }

    public LocalDate getSecondDate() {
        return dates.getSecondDate();
    }

    public LocalTime getFirstTime() {
        return times.getFirstTime();
    }

    public LocalTime getSecondTime() {
        return times.getSecondTime();
    }

    public int getNumOfDates() {
        int first = hasFirstDate() ? 1 : 0;
        int second = hasSecondDate() ? 1 : 0;
        return first + second;
    }

    public int getNumOfTimes() {
        int first = hasFirstTime() ? 1 : 0;
        int second = hasSecondTime() ? 1 : 0;
        return first + second;
    }

    public boolean hasFirstDate() {
        return dates.hasFirstDate();
    }

    public boolean hasSecondDate() {
        return dates.hasSecondDate();
    }

    public boolean hasFirstTime() {
        return times.hasFirstTime();
    }

    public boolean hasSecondTime() {
        return times.hasSecondTime();
    }

    public boolean isEmpty() {
        return !hasFirstDate() && !hasFirstTime();
    }
}
