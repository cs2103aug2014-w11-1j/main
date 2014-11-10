package io.test;

import static org.junit.Assert.*;
import io.json.JsonItemParser;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

//@author A0065475X
public class JsonItemParserTest {

    @Test
    public void testDateTimeConversion() {
        Duration duration;
        String durationString;

        duration = Duration.ofHours(15);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));
        
        duration = Duration.ofDays(10000);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(3);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(1200);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));
        
        duration = Duration.ofHours(0);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));
        
        duration = Duration.ofHours(24);
        durationString = JsonItemParser.durationToString(duration);
        assertEquals(duration, JsonItemParser.stringToDuration(durationString));

        durationString = "test";
        assertEquals(null, JsonItemParser.stringToDuration(durationString));

        LocalTime time;
        String timeString;
        
        time = LocalTime.of(5, 4, 3);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 1, 1);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 0, 1);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 1, 0);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 0, 0);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(23, 59, 59);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));
        
        time = LocalTime.of(11, 10, 3);
        timeString = JsonItemParser.localTimeToString(time);
        assertEquals(time, JsonItemParser.stringToLocalTime(timeString));

        LocalDate date;
        String dateString;
        
        date = LocalDate.of(2013, 12, 31);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));
        
        date = LocalDate.of(1990, 2, 2);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));
        
        date = LocalDate.of(2012, 2, 29);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));
        
        date = LocalDate.of(1, 1, 1);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));

        date = LocalDate.of(3012, 7, 4);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));

        date = LocalDate.of(1000, 7, 7);
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));

        date = LocalDate.now();
        dateString = JsonItemParser.localDateToString(date);
        assertEquals(date, JsonItemParser.stringToLocalDate(dateString));
    }

}
