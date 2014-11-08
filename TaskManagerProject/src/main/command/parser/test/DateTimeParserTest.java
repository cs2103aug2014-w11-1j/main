package main.command.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

import main.command.parser.DateTimePair;
import main.command.parser.DateTimeParser;

import org.junit.Test;

//@author A0111862M
public class DateTimeParserTest {
    @Test
    public void testParseDateTimePairsSequence() {
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
        String testTDTDT = "3 PM 17/10/14 6 PM 18/10/14 9 PM";

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

        dtPair = DateTimeParser.parseDateTimesInSequence(testTDTDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());
    }

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
        String testTDTDT = "3 PM 17/10/14 6 PM 18/10/14 9 PM";

        DateTimePair dtPair = DateTimeParser.parseDateTimes(testNull);
        assertTrue(dtPair.isEmpty());

        dtPair = DateTimeParser.parseDateTimes(testT);
        assertEquals(null, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTT);
        assertEquals(null, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(null, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testDTT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTTD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(null, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testDD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(null, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTDD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testDTD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testDDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t2, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(null, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTDTD);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());

        dtPair = DateTimeParser.parseDateTimes(testTDTDT);
        assertEquals(d1, dtPair.getFirstDate());
        assertEquals(t1, dtPair.getFirstTime());
        assertEquals(d2, dtPair.getSecondDate());
        assertEquals(t2, dtPair.getSecondTime());
    }

    @Test
    public void testIsDateTime() {
        String date = "3 Nov";
        String time = "11:00";
        String preposition = "on ";
        String modifier = "this ";

        assertTrue(DateTimeParser.isDate(date));
        assertFalse(DateTimeParser.isDate(time));

        assertTrue(DateTimeParser.isTime(time));
        assertFalse(DateTimeParser.isTime(date));

        assertTrue(DateTimeParser.isDate(preposition + date));
        assertTrue(DateTimeParser.isTime(modifier + time));
        assertTrue(DateTimeParser.isDate(preposition + modifier + date));
    }

    @Test
    public void testDateModifiers() {
        String testPrevious = "last 3 Nov";
        String testThis = "this 3 Nov";
        String testNext = "next 3 Nov";

        int year = LocalDate.now().getYear();

        DateTimePair dtPair = DateTimeParser.parseDateTimes(testPrevious);
        assertEquals(year - 1, dtPair.getFirstDate().getYear());

        dtPair = DateTimeParser.parseDateTimes(testThis);
        assertEquals(year, dtPair.getFirstDate().getYear());

        dtPair = DateTimeParser.parseDateTimes(testNext);
        assertEquals(year + 1, dtPair.getFirstDate().getYear());
    }

    @Test
    public void testPrepositions() {
        String test1 = "on 3 Nov";
        String test2 = "from about around 3 Nov";
        LocalDate expectedDate = LocalDate.of(LocalDate.now().getYear(), 11, 3);
        if (LocalDate.now().isAfter(expectedDate)) {
            expectedDate = expectedDate.plusYears(1);
        }

        // single preposition
        assertDate(expectedDate, test1);

        // multiple preposition
        assertDate(expectedDate, test2);
    }

    @Test
    public void testRelativeDates() {
        LocalDate today = LocalDate.now();

        String testDayOfWeek = "wed";
        String testDayOfWeekFull = "wednesday";
        LocalDate expectedDate = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        assertDate(expectedDate, testDayOfWeek);
        assertDate(expectedDate, testDayOfWeekFull);

        String testOccasion = "christmas";
        expectedDate = LocalDate.of(today.getYear(), 12, 25);
        if (today.isAfter(expectedDate)) {
            expectedDate = expectedDate.plusYears(1);
        }
        assertDate(expectedDate, testOccasion);

        String testPlus = "+5d";
        expectedDate = today.plusDays(5);
        assertDate(expectedDate, testPlus);

        String testIncompletePlus = "+5";
        assertDate(null, testIncompletePlus);

        String testMinus = "-5d";
        expectedDate = today.minusDays(5);
        assertDate(expectedDate, testMinus);

        String testToday = "today";
        assertDate(today, testToday);

        String testYesterday = "yesterday";
        assertDate(today.minusDays(1), testYesterday);

        String testTomorrow = "tomorrow";
        assertDate(today.plusDays(1), testTomorrow);
    }

    private static void assertDate(LocalDate expectedDate, String testDate) {
        DateTimePair dtPair = DateTimeParser.parseDateTimes(testDate);
        assertEquals(expectedDate, dtPair.getFirstDate());
    }
}
