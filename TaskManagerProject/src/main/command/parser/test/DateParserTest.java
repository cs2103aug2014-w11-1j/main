package main.command.parser.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import main.command.parser.DateParser;

import org.junit.Test;

public class DateParserTest {

    @Test
    public void testParseDateTime() {
        LocalDate d1 = LocalDate.of(2014, 10, 17);
        LocalDate d2 = LocalDate.of(2014, 10, 18);
        LocalDate dNow = LocalDate.now();
        LocalTime t1 = LocalTime.of(15, 0);
        LocalTime t2 = LocalTime.of(18, 0);
        LocalTime tMin = LocalTime.MIN;
        LocalTime tMax = LocalTime.MAX;

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
        String testDDT = "17/10/14 18/10/14 3 PM";
        String testTTDD = "3 PM 17/10/14 6 PM 18/10/14";

        List<LocalDateTime> dtList = DateParser.parseDateTime(testNull);
        assertEquals(dtList, null);

        dtList = DateParser.parseDateTime(testT);
        assertEquals(dtList.get(0), LocalDateTime.of(dNow, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(dNow, t1));

        dtList = DateParser.parseDateTime(testTT);
        assertEquals(dtList.get(0), LocalDateTime.of(dNow, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(dNow, t2));

        dtList = DateParser.parseDateTime(testD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, tMin));
        assertEquals(dtList.get(1), LocalDateTime.of(d1, tMax));

        dtList = DateParser.parseDateTime(testDT);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d1, t1));

        dtList = DateParser.parseDateTime(testDTT);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d1, t2));

        dtList = DateParser.parseDateTime(testTDT);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d1, t2));

        dtList = DateParser.parseDateTime(testTTD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d1, t2));

        dtList = DateParser.parseDateTime(testDD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, tMin));
        assertEquals(dtList.get(1), LocalDateTime.of(d2, tMax));

        dtList = DateParser.parseDateTime(testTDD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d2, tMax));

        dtList = DateParser.parseDateTime(testDTD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d2, tMax));

        dtList = DateParser.parseDateTime(testDDT);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, tMin));
        assertEquals(dtList.get(1), LocalDateTime.of(d2, t1));

        dtList = DateParser.parseDateTime(testTTDD);
        assertEquals(dtList.get(0), LocalDateTime.of(d1, t1));
        assertEquals(dtList.get(1), LocalDateTime.of(d2, t2));
    }

}
