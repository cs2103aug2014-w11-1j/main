package main.command.parser.test;

import static org.junit.Assert.assertEquals;
import main.command.parser.CommandParser;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Tag;

public class CommandParserTest {
    private static final String TERM_1 = "test";
    private static final String TERM_2 = "testytest";
    private static final String TERM_3 = "test-testy-test";
    private static final String TERM_4 = "blue";
    private static final String TERM_5 = "tag";
    private static final String TERM_6 = "yellow";
    private static final String TERM_7 = "tag#red";

    private static final String IGNORE = "\"";
    private static final String TAG = "#";
    private static final String PRIORITY = "+";

    private static final String PRI_LOW = "low";
    private static final String PRI_MED = "mEd";
    private static final String PRI_HIGH = "HIGH";

    private enum TimeTest {
        ABS_12_HOURS_LONG_1  ("9:46 AM"),
        ABS_12_HOURS_SHORT_1 ("9 AM"),
        ABS_24_HOURS_LONG_1  ("9:46"),
        ABS_12_HOURS_LONG_2  ("3:16 PM"),
        ABS_12_HOURS_SHORT_2 ("3 PM"),
        ABS_24_HOURS_LONG_2  ("15:16"),
        REL_PLUS_HOURS       ("+3h"),
        REL_MINUS_HOURS      ("-3h"),
        REL_NOW              ("now");

        private final String type;
        TimeTest(String type) {
            this.type = type;
        }
    }

    private enum DateTest {
        ABS_D_MMMM_1      ("24 August"),
        ABS_DMMM_1        ("24Aug"),
        ABS_DMMMY_1       ("24Aug2014"),
        ABS_DMMMYY_1      ("24Aug14"),
        ABS_D_M_YY_1      ("24/8/14"),
        ABS_D_MMMM_2      ("13 september"),
        ABS_DMMM_2        ("13sep"),
        ABS_DMMMY_2       ("13sep2014"),
        ABS_DMMMYY_2      ("13sep14"),
        ABS_D_M_YY_2      ("13/9/14"),
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
    public void testParseName() {
        String test1 = mergeStrings(new String[]{TERM_1, IGNORE+TERM_2+IGNORE,
                TAG+TERM_7, TERM_3, IGNORE, TAG+TERM_4, IGNORE, PRIORITY+PRI_HIGH,
                PRIORITY+TERM_5, TimeTest.ABS_12_HOURS_LONG_1.type,
                DateTest.ABS_DMMMY_1.type});
        String name1 = CommandParser.parseName(test1);
        assertEquals(name1, mergeStrings(new String[]{TERM_1, TERM_2, TERM_3,
                TAG+TERM_4, PRIORITY+TERM_5}));
    }

    @Test
    public void testParseTags() {
        String test1 = mergeStrings(new String[]{TERM_1, TERM_2, PRI_MED, TAG+TERM_6});
        String test2 = mergeStrings(new String[]{TAG+TERM_3, TERM_7, PRI_HIGH, TERM_1});
        String test3 = mergeStrings(new String[]{TERM_1, TAG+TERM_5, TAG+TERM_7, PRI_MED});

        // single tag, at end
        Tag[] tags = CommandParser.parseTags(test1);
        StringBuilder result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_6);

        // single tag, elsewhere
        tags = CommandParser.parseTags(test2);
        result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_3);

        // multiple tags
        tags = CommandParser.parseTags(test3);
        result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(result.toString().trim(), TERM_5 + " " + TERM_7);
    }

    @Test
    public void testParsePriority() {
        String test1 = mergeStrings(new String[]{TERM_1, TERM_2, PRIORITY+PRI_LOW, TAG+TERM_6});
        String test2 = mergeStrings(new String[]{TAG+TERM_3, TERM_7, PRIORITY+PRI_HIGH, TERM_1});
        String test3 = mergeStrings(new String[]{TERM_1, TAG+TERM_5, TAG+TERM_7, PRIORITY+PRI_MED});
        String test4 = mergeStrings(new String[]{TERM_1, TAG+TERM_7, TERM_3});
        String test5 = mergeStrings(new String[]{TERM_1, PRIORITY+PRI_HIGH, TAG+TERM_7, PRIORITY+PRI_MED});

        // low priority
        Priority priority = CommandParser.parsePriority(test1);
        assertEquals(priority, Priority.LOW);

        // high priority
        priority = CommandParser.parsePriority(test2);
        assertEquals(priority, Priority.HIGH);

        // med priority
        priority = CommandParser.parsePriority(test3);
        assertEquals(priority, Priority.MEDIUM);

        // no priority - take default (null for now)
        priority = CommandParser.parsePriority(test4);
        assertEquals(priority, null);

        // multiple priority - should take the first
        priority = CommandParser.parsePriority(test5);
        assertEquals(priority, Priority.HIGH);
    }

    private String mergeStrings(String[] strings) {
        StringBuilder sB = new StringBuilder();
        for (String s : strings) {
            sB.append(s).append(' ');
        }
        return sB.toString().trim();
    }

}
