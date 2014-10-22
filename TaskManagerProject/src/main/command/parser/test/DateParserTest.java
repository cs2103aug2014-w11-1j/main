package main.command.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import main.command.parser.DateParser;
import main.command.parser.DateTimePair;

import org.junit.Test;

public class DateParserTest {

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

        DateTimePair dtPair = DateParser.parseDateTimes(testNull);
        assertTrue(dtPair.isEmpty());

        dtPair = DateParser.parseDateTimes(testT);
        assertEquals(dtPair.getFirstDate(), null);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testTT);
        assertEquals(dtPair.getFirstDate(), null);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), t2);

        dtPair = DateParser.parseDateTimes(testD);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), null);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testDT);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testDTT);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), t2);

        dtPair = DateParser.parseDateTimes(testTDT);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), null);
        assertEquals(dtPair.getSecondTime(), t2);

        dtPair = DateParser.parseDateTimes(testTTD);
        assertEquals(dtPair.getFirstDate(), null);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), d1);
        assertEquals(dtPair.getSecondTime(), t2);

        dtPair = DateParser.parseDateTimes(testDD);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), null);
        assertEquals(dtPair.getSecondDate(), d2);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testTDD);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), d2);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testDTD);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), d2);
        assertEquals(dtPair.getSecondTime(), null);

        dtPair = DateParser.parseDateTimes(testDDT);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), null);
        assertEquals(dtPair.getSecondDate(), d2);
        assertEquals(dtPair.getSecondTime(), t2);

        dtPair = DateParser.parseDateTimes(testTDTD);
        assertEquals(dtPair.getFirstDate(), d1);
        assertEquals(dtPair.getFirstTime(), t1);
        assertEquals(dtPair.getSecondDate(), d2);
        assertEquals(dtPair.getSecondTime(), t2);
    }

}
