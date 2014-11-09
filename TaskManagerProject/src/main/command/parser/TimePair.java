package main.command.parser;

import java.time.LocalTime;

//@author A0111862M
/**
 * Container class for a pair of times.
 */
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

    LocalTime getFirstTime() {
        return firstTime;
    }

    LocalTime getSecondTime() {
        return secondTime;
    }

    void setFirstTime(LocalTime firstTime) {
        this.firstTime = firstTime;
    }

    void setSecondTime(LocalTime secondTime) {
        this.secondTime = secondTime;
    }

    boolean hasFirstTime() {
        return firstTime != null;
    }

    boolean hasSecondTime() {
        return secondTime != null;
    }

    boolean areTimesSame() {
        return hasFirstTime() ? firstTime.equals(secondTime) : false;
    }

    boolean isFull() {
        return hasFirstTime() && hasSecondTime();
    }
}
