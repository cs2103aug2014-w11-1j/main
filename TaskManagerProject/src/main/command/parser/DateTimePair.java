package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import main.command.parser.DateTimeParser.DateModifier;
import main.command.parser.ParsedDate.Frequency;

//@author A0111862M
/**
 * Container class for a DatePair and a TimePair, with DateModifiers for both
 * sets of date and time.
 */
public class DateTimePair {
    private DateModifier firstModifier;
    private DateModifier secondModifier;

    protected DatePair dates;
    protected TimePair times;


    DateTimePair() {
        dates = new DatePair();
        times = new TimePair();
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

    void add(DateModifier modifier) {
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

    /**
     * Modifies the two dates based on the two date modifiers, shifting them
     * backwards, forwards, etc, by a quantity determined by their frequencies.
     */
    private void modifyDates() {
        if (hasFirstDate()) {
            LocalDate date = modifyDate(dates.getFirstDate(), LocalDate.now(),
                    dates.getFirstFrequency(), firstModifier);
            dates.setFirstDate(new ParsedDate(date));
        }

        if (hasSecondDate()) {
            // base off first date only if second date has no (forced) modifier
            // (e.g. from next wed to thu)
            LocalDate fromDate = hasFirstDate() && secondModifier == null ?
                    dates.getFirstDate() : LocalDate.now();

            LocalDate date = modifyDate(dates.getSecondDate(), fromDate,
                    dates.getSecondFrequency(), secondModifier);
            dates.setSecondDate(new ParsedDate(date));
        }
    }

    /**
     * Modifies {@code date} based on a {@code frequency} and {@code modifier},
     * with {@code fromDate} as a reference point.
     *
     * @param date
     *            the date to modify
     * @param fromDate
     *            the date to modify from as a reference
     * @param frequency
     *            the frequency the date occurs at
     * @param modifier
     *            the modification to be applied to the date
     * @return the modified date
     */
    private static LocalDate modifyDate(LocalDate date, LocalDate fromDate,
            Frequency frequency, DateModifier modifier) {

        if (frequency == null) {
            return date; // can't modify it if it happens only once
        }

        if (modifier == null) {
            return getNextOccurrence(date, fromDate, frequency.getField());
        } else {
            return modifyDate(date, fromDate, frequency.getField(), modifier);
        }
    }

    private static LocalDate modifyDate(LocalDate date, LocalDate fromDate,
            ChronoField field, DateModifier modifier) {

        // synchronises the two dates first, assuming they share the same field
        date = date.with(field, fromDate.get(field));

        // then apply the modification
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

    /**
     * Gets the next occurrence of {@code date}, starting from {@code fromDate}.
     * The {@code field} is used to ensure a uniform increase.
     *
     * @param date
     *            the date to get the next occurrence of
     * @param fromDate
     *            the date to use as a starting point
     * @param field
     *            the frequency the date occurs at
     * @return the next occurrence of the date
     */
    private static LocalDate getNextOccurrence(LocalDate date,
            LocalDate fromDate, ChronoField field) {

        // synchronises the two dates first, assuming they share the same field
        date = date.with(field, fromDate.get(field));

        while (!fromDate.isBefore(date)) {
            date = date.plus(1, field.getBaseUnit());
        }

        return date;
    }

    public LocalTime getFirstTime() {
        return times.getFirstTime();
    }

    public LocalTime getSecondTime() {
        return times.getSecondTime();
    }

    /**
     * @return the number of dates filled in
     */
    public int getNumOfDates() {
        int first = hasFirstDate() ? 1 : 0;
        int second = hasSecondDate() ? 1 : 0;
        return first + second;
    }

    /**
     * @return the number of times filled in
     */
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
