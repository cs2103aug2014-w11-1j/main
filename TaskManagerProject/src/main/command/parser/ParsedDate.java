package main.command.parser;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

//@author A0111862M
/**
 * Container class for a LocalDate and a Frequency.
 */
public class ParsedDate {
    // the frequency of a date (e.g. Christmas happens once a YEAR).
    enum Frequency {
        WEEK(ChronoField.ALIGNED_WEEK_OF_YEAR),
        YEAR(ChronoField.YEAR);

        private ChronoField field;

        Frequency(ChronoField field) {
            this.field = field;
        }

        ChronoField getField(){
            return field;
        }
    }

    private LocalDate date;
    private Frequency frequency;

    ParsedDate(LocalDate date) {
        this.date = date;
    }

    ParsedDate(LocalDate date, Frequency frequency) {
        this(date);
        this.frequency = frequency;
    }

    LocalDate getDate() {
        return date;
    }

    Frequency getFrequency() {
        return frequency;
    }
}
