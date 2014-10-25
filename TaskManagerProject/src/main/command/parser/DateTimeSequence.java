package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeSequence extends DateTimePair {
    private int count;
    private int max;

    DateTimeSequence() {
        super();
        count = 0;
        max = 4;
    }

    DateTimeSequence(DatePair dates, TimePair times) {
        super(dates, times);
        count = 0;
        max = 0;
    }

    @Override
    void add(LocalDate d) {
        if (d == null || isFull()) {
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
        if (t == null || isFull()) {
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
