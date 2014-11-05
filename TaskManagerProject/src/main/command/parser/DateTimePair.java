package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;

import main.command.parser.DateTimeParser.DateModifier;
import main.command.parser.ParsedDate.Frequency;

public class DateTimePair {
    private DateModifier firstModifier;
    private DateModifier secondModifier;

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

    void add(ParsedDate d) {
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

    public void add(DateModifier modifier) {
        if (secondModifier != null) {
            return;
        }

        if (hasSecondDate() || hasSecondTime()) {
            secondModifier = modifier;
        } else {
            firstModifier = modifier;
        }
    }

    boolean isFull() {
        return dates.isFull() && times.isFull();
    }

    public LocalDate getFirstDate() {
        return modifyDate(dates.getFirstDate(), LocalDate.now(),
                dates.getFirstFrequency(), firstModifier);
    }

    public LocalDate getSecondDate() {
        if (hasFirstDate()) {
            return modifyDate(dates.getSecondDate(), dates.getFirstDate(),
                    dates.getSecondFrequency(), secondModifier);
        } else {
            return modifyDate(dates.getSecondDate(), LocalDate.now(),
                    dates.getSecondFrequency(), secondModifier);
        }
    }

    private LocalDate modifyDate(LocalDate date, LocalDate fromDate,
            Frequency frequency, DateModifier modifier) {
        // TODO Modify date based on frequency and modfier
        return date;
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
