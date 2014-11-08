package main.command.parser;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

//@author A0111862M
public class ParsedDate {
    enum Frequency {
        WEEK(ChronoField.ALIGNED_WEEK_OF_YEAR),
        YEAR(ChronoField.YEAR);

        ChronoField field;

        Frequency(ChronoField field) {
            this.field = field;
        }

        ChronoField getField(){
            return field;
        }
    }

    LocalDate date;
    Frequency frequency;

    public ParsedDate(LocalDate date) {
        this.date = date;
    }

    public ParsedDate(LocalDate date, Frequency frequency) {
        this(date);
        this.frequency = frequency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }
}
