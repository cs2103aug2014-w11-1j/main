package main.command.parser;

import java.time.LocalTime;

public class TimePair {
    private LocalTime firstTime;
    private LocalTime secondTime;

    void add(LocalTime t) {
        if (t == null || isFull()) {
            return;
        }

        if (!hasFirstTime()) {
            firstTime = t;
        } else {
            secondTime = t;
        }
    }

    public LocalTime getFirstTime() {
        return firstTime;
    }

    public LocalTime getSecondTime() {
        return secondTime;
    }

    void setFirstTime(LocalTime firstTime) {
        this.firstTime = firstTime;
    }

    void setSecondTime(LocalTime secondTime) {
        this.secondTime = secondTime;
    }

    public boolean hasFirstTime() {
        return firstTime != null;
    }

    public boolean hasSecondTime() {
        return secondTime != null;
    }

    public boolean areTimesSame() {
        return firstTime.equals(secondTime);
    }

    public boolean isFull() {
        return hasFirstTime() && hasSecondTime();
    }
}
