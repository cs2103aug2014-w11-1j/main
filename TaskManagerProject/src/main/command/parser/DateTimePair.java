package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import main.command.parser.DateTimeParser.DateModifier;
import main.command.parser.ParsedDate.Frequency;

//@author A0111862M
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
        if (!hasFirstDate()) {
            return null;
        }

        modifyDates();

        return dates.getFirstDate();
    }

    public LocalDate getSecondDate() {
        if (!hasSecondDate()) {
            return null;
        }

        modifyDates();

        return dates.getSecondDate();
    }

    private void modifyDates() {
        if (hasFirstDate()) {
            LocalDate date = modifyDate(dates.getFirstDate(), LocalDate.now(),
                    dates.getFirstFrequency(), firstModifier);
            dates.setFirstDate(new ParsedDate(date));
        }

        if (hasSecondDate()) {
            LocalDate date = null;
            if (hasFirstDate() && secondModifier == null) {
                date = modifyDate(dates.getSecondDate(), dates.getFirstDate(),
                        dates.getSecondFrequency(), secondModifier);
            } else {
                date = modifyDate(dates.getSecondDate(), LocalDate.now(),
                        dates.getSecondFrequency(), secondModifier);
            }
            dates.setSecondDate(new ParsedDate(date));
        }
    }

    private static LocalDate modifyDate(LocalDate date, LocalDate fromDate,
            Frequency frequency, DateModifier modifier) {
        if (frequency == null) {
            return date; // can't modify it if it can only happen once.
        }

        if (modifier == null) {
            return getNextOccurrence(date, fromDate, frequency.getField());
        } else {
            return modifyDate(date, fromDate, frequency.getField(), modifier);
        }
    }

    private static LocalDate modifyDate(LocalDate date, LocalDate fromDate,
            ChronoField field, DateModifier modifier) {
        date = date.with(field, fromDate.get(field));
        switch (modifier) {
            case PREVIOUS :
                return date.minus(1, field.getBaseUnit());
            case THIS :
                return date;
            case NEXT :
                return date.plus(1, field.getBaseUnit());
        }
        return date;
    }

    private static LocalDate getNextOccurrence(LocalDate date, LocalDate fromDate,
            ChronoField field) {
        date = date.with(field, fromDate.get(field));
        if (fromDate.isBefore(date)) {
            return date;
        } else {
            do {
                date = date.plus(1, field.getBaseUnit());
            } while (!fromDate.isBefore(date));
            return date;
        }
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
