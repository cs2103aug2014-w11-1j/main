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

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public LocalDate getSecondDate() {
        return secondDate;
    }

    void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    void setSecondDate(LocalDate secondDate) {
        this.secondDate = secondDate;
    }

    public boolean hasSecondDate() {
        return secondDate != null;
    }

    public boolean hasFirstDate() {
        return firstDate != null;
    }

    public boolean areDatesSame() {
        return firstDate.equals(secondDate);
    }

    public boolean isFull() {
        return hasFirstDate() && hasSecondDate();
    }
}
