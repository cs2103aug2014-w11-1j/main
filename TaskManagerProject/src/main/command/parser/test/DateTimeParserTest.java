package main.command.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import main.command.parser.DateTimeParser;
import main.command.parser.DateTimePair;

import org.junit.Test;

public class DateTimeParserTest {

    @Test
    public void testParseDateTimePairs() {
        LocalDate d1 = LocalDate.of(2014, 10, 17);
        LocalDate d2 = LocalDate.of(2014, 10, 18);
        LocalTime t1 = LocalTime.of(15, 0);
        LocalTime t2 = LocalTime.of(18, 0);

        String testNull = "";
        String testT = "3 PM";
        String testTT = "3 PM 6 PM";
        String testD = "17/10/14";
        String testDT = "17/10/14 3 PM";
        String testDTT = "17/10/14 3 PM 6 PM";
        String testTDT = "3 PM 17/10/14 6 PM";
        String testTTD = "3 PM 6 PM 17/10/14";
        String testDD = "17/10/14 18/10/14";
        String testTDD = "3 PM 17/10/14 18/10/14";
        String testDTD = "17/10/14  3 PM 18/10/14";
        String testDDT = "17/10/14 18/10/14 6 PM";
        String testTDTD = "3 PM 17/10/14 6 PM 18/10/14";

        DateTimePair dtPair = DateTimeParser.parseDateTimesInSequence(testNull);
        assertTrue(dtPair.isEmpty());

        dtPair = DateTimeParser.parseDateTimesInSequence(testT);
        assertEquals(null, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testTT);
        assertEquals(null, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(null, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testDTT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testTDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testTTD);
        assertEquals(null, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d1, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testDD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(null, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testTDD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testDTD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testDDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(null, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimesInSequence(testTDTD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());
    }

}
