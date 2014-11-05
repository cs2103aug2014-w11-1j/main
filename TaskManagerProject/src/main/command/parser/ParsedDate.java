package main.command.parser;

import java.time.LocalDate;
import java.time.Period;

public class ParsedDate {
    enum Frequency {
        DAY(Period.ofDays(1)),
        WEEK(Period.ofWeeks(1)),
        MONTH(Period.ofMonths(1)),
        YEAR(Period.ofYears(1));

        Period p;

        Frequency(Period p) {
            this.p = p;
        }

        Period getPeriod(){
            return p;
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
