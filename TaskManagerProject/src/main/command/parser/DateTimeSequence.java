package main.command.parser;

import java.time.LocalTime;

//@author A0111862M
/**
 * Container class for a DatePair and a TimePair, with DateModifiers for both
 * sets of date and time.
 * <p>
 * Also adds dates and times in sequence, so adding 2 dates, and then a time
 * will allocate the time to the second slot instead of the first.
 */
public class DateTimeSequence extends DateTimePair {
    private int count;
    private int max;

    DateTimeSequence() {
        super();
        count = 0;
        max = 4;
    }

    @Override
    void add(ParsedDate d) {
        if (isFull()) {
            return;
        }

        // two dates in a row
        if (hasFirstDate() && !hasFirstTime()) {
            max = 3;
        }

        if (!hasFirstDate() && !hasSecondTime()) {
            dates.setFirstDate(d);
        } else {
            dates.setSecondDate(d);
        }

        count++;

        avoidDuplicates();
    }

    @Override
    void add(LocalTime t) {
        if (isFull()) {
            return;
        }

        // two times in a row
        if (hasFirstTime() && !hasFirstDate()) {
            max = 3;
        }

        if (!hasFirstTime() && !hasSecondDate()) {
            times.setFirstTime(t);
        } else {
            times.setSecondTime(t);
        }

        count++;

        avoidDuplicates();
    }

    @Override
    boolean isFull() {
        return count == max;
    }

    private boolean arePairsSame() {
        boolean areDatesSame = dates.areDatesSame();
        boolean areTimesSame = times.areTimesSame();

        return areDatesSame && areTimesSame;
    }

    private void avoidDuplicates() {
        // avoid duplicate start / end datetimes
        if (isFull() && arePairsSame()) {
            dates.setSecondDate(null);
            times.setSecondTime(null);
        }
    }
}
