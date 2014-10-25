package main.command.parser;

import java.time.LocalDate;

public class DatePair {
    private LocalDate firstDate;
    private LocalDate secondDate;

    void add(LocalDate d) {
        if (d == null || isFull()) {
            return;
        }

        if (!hasFirstDate()) {
            firstDate = d;
        } else {
            secondDate = d;
        }
    }

    LocalDate getFirstDate() {
        return firstDate;
    }

    LocalDate getSecondDate() {
        return secondDate;
    }

    void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    void setSecondDate(LocalDate secondDate) {
        this.secondDate = secondDate;
    }

    boolean hasSecondDate() {
        return secondDate != null;
    }

    boolean hasFirstDate() {
        return firstDate != null;
    }

    boolean areDatesSame() {
        return hasFirstDate() ? firstDate.equals(secondDate) : false;
    }

    boolean isFull() {
        return hasFirstDate() && hasSecondDate();
    }
}
