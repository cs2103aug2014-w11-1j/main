package main.command.parser;

import java.time.LocalDate;

import main.command.parser.ParsedDate.Frequency;


public class DatePair {
    private LocalDate firstDate;
    private Frequency firstFrequency;
    private LocalDate secondDate;
    private Frequency secondFrequency;

    void add(ParsedDate d) {
        if (d == null || isFull()) {
            return;
        }

        if (!hasFirstDate()) {
            firstDate = d.getDate();
            firstFrequency = d.getFrequency();
        } else {
            secondDate = d.getDate();
            secondFrequency = d.getFrequency();
        }
    }

    LocalDate getFirstDate() {
        return firstDate;
    }

    LocalDate getSecondDate() {
        return secondDate;
    }

    Frequency getFirstFrequency() {
        return firstFrequency;
    }

    Frequency getSecondFrequency() {
        return secondFrequency;
    }

    void setFirstDate(ParsedDate d) {
        this.firstDate = d.getDate();
        this.firstFrequency = d.getFrequency();
    }

    void setSecondDate(ParsedDate d) {
        this.secondDate = d.getDate();
        this.secondFrequency = d.getFrequency();
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
