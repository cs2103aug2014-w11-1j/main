package main.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParserTest {
    private static final String TERM_1 = "test";
    private static final String TERM_2 = "testytest";
    private static final String TERM_3 = "test-testy-test";
    private static final String TERM_4 = "blue";
    private static final String TERM_5 = "tag";
    private static final String TERM_6 = "yellow";
    private static final String TERM_7 = "tag#red";

    private static final String TERM_IGNORE_1 = "\"#blue\"";
    private static final String TERM_IGNORE_2 = "\"+high\"";
    private static final String TERM_IGNORE_3 = "\"#blue +high\"";
    private static final String TERM_IGNORE_4 = "\"3 PM\"";

    private static final String TAG = "#";

    private static final String PRI_LOW = "+low";
    private static final String PRI_MED = "+mEd";
    private static final String PRI_HIGH = "+HIGH";

    private enum TimeTest {
        ABS_12_HOURS_LONG  ("3:46 PM"),
        ABS_12_HOURS_SHORT ("3 PM"),
        ABS_24_HOURS_LONG  ("15:46"),
        REL_PLUS_HOURS     ("+3h"),
        REL_MINUS_HOURS    ("-3h"),
        REL_NOW            ("now");

        private final String type;
        TimeTest(String type) {
            this.type = type;
        }
    }

    private enum DateTest {
        ABS_D_MMMM        ("24 August"),
        ABS_DMMM          ("24Aug"),
        ABS_DMMMY         ("24Aug2014"),
        ABS_DMMMYY        ("24Aug14"),
        ABS_D_M_YY        ("24/8/14"),
        REL_WEEKDAY_SHORT ("tues"),
        REL_WEEKDAY_LONG  ("Tuesday"),
        REL_TODAY         ("today"),
        REL_YESTERDAY     ("Yesterday"),
        REL_TOMORROW      ("tomorROW"),
        REL_PLUS_DAYS     ("+3d"),
        REL_MINUS_DAYS    ("-3d"),
        REL_NOW           ("now");

        private final String type;
        DateTest(String type) {
            this.type = type;
        }
    }

    @Test
    public void testParseTask() {
        String test1 = mergeStrings(new String[]{TERM_1, TERM_IGNORE_1, TERM_2,
                TAG+TERM_7, TERM_3, TAG+TERM_4, PRI_HIGH,
                TimeTest.ABS_12_HOURS_LONG.type, DateTest.ABS_DMMMY.type});
        TaskInfo t = CommandParser.parseTask(test1);
        assertEquals(t.name, test1);
        assertEquals(t.endDate, LocalDate.of(2014, 8, 24));
        assertEquals(t.endTime, LocalTime.of(15, 46));
        assertEquals(t.duration, Duration.ZERO);
        assertEquals(t.priority, Priority.HIGH);
        StringBuilder result = new StringBuilder();
        for (Tag tag : t.tags) {
            result.append(tag.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_7 + " " + TERM_4);
    }

    @Test
    public void testParseDateTime() {
        //CommandParser
    }

    @Test
    public void testParseTime() {
        TaskInfo testTaskInfo = new TaskInfo();
        //CommandParser
        fail("Not yet implemented");
    }

    @Test
    public void testParseTags() {
        TaskInfo testTaskInfo = new TaskInfo();
        String test1 = mergeStrings(new String[]{TERM_1, TERM_2, PRI_MED, TAG+TERM_6});
        String test2 = mergeStrings(new String[]{TAG+TERM_3, TERM_7, PRI_HIGH, TERM_1});
        String test3 = mergeStrings(new String[]{TERM_1, TAG+TERM_5, TAG+TERM_7, PRI_MED});

        // single tag, at end
        CommandParser.parseTags(test1, testTaskInfo);
        StringBuilder result = new StringBuilder();
        for (Tag t : testTaskInfo.tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_6);

        // single tag, elsewhere
        CommandParser.parseTags(test2, testTaskInfo);
        result = new StringBuilder();
        for (Tag t : testTaskInfo.tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_3);

        // multiple tags
        CommandParser.parseTags(test3, testTaskInfo);
        result = new StringBuilder();
        for (Tag t : testTaskInfo.tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_5 + " " + TERM_7);
    }

    @Test
    public void testParsePriority() {
        TaskInfo testTaskInfo = new TaskInfo();
        String test1 = mergeStrings(new String[]{TERM_1, TERM_2, PRI_LOW, TAG+TERM_6});
        String test2 = mergeStrings(new String[]{TAG+TERM_3, TERM_7, PRI_HIGH, TERM_1});
        String test3 = mergeStrings(new String[]{TERM_1, TAG+TERM_5, TAG+TERM_7, PRI_MED});
        String test4 = mergeStrings(new String[]{TERM_1, TAG+TERM_7, TERM_3});
        String test5 = mergeStrings(new String[]{TERM_1, PRI_HIGH, TAG+TERM_7, PRI_MED});

        // low priority
        CommandParser.parsePriority(test1, testTaskInfo);
        assertEquals(testTaskInfo.priority, Priority.LOW);

        // high priority
        CommandParser.parsePriority(test2, testTaskInfo);
        assertEquals(testTaskInfo.priority, Priority.HIGH);

        // med priority
        CommandParser.parsePriority(test3, testTaskInfo);
        assertEquals(testTaskInfo.priority, Priority.MEDIUM);

        // no priority - take default (low for now)
        CommandParser.parsePriority(test4, testTaskInfo);
        assertEquals(testTaskInfo.priority, Priority.LOW);

        // multiple priority - should take the first
        CommandParser.parsePriority(test5, testTaskInfo);
        assertEquals(testTaskInfo.priority, Priority.HIGH);
    }

    private String mergeStrings(String[] strings) {
        StringBuilder sB = new StringBuilder();
        for (String s : strings) {
            sB.append(s).append(' ');
        }
        return sB.toString().trim();
    }

}
