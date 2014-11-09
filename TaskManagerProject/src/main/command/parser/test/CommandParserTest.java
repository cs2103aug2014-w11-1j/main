package main.command.parser.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import main.command.parser.CommandParser;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;

//@author A0111862M
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
    private static final String PRI_MED = "mEdium";
    private static final String PRI_HIGH = "HIGH";

    private static final String STA_DONE = "done";
    private static final String STA_UNDONE = "undone";

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
        String test1 = mergeStrings(TERM_1, IGNORE+TERM_2+IGNORE, TAG+TERM_7,
                TERM_3, IGNORE, TAG+TERM_4, IGNORE, PRIORITY+PRI_HIGH,
                PRIORITY+TERM_5, TimeTest.ABS_12_HOURS_LONG_1.type,
                DateTest.ABS_DMMMY_1.type, STA_DONE);
        String name1 = CommandParser.parseName(test1);
        assertEquals(mergeStrings(TERM_1, TERM_2, TERM_3, TAG+TERM_4,
                PRIORITY+TERM_5), name1);
    }

    @Test
    public void testStripIgnoreSymbols() {
        String test1 = mergeStrings(TERM_1, IGNORE+TERM_2+IGNORE, TAG+TERM_7,
                TERM_3, IGNORE, TAG+TERM_4, IGNORE, PRIORITY+PRI_HIGH, IGNORE,
                PRIORITY+TERM_5, TimeTest.ABS_12_HOURS_LONG_1.type,
                DateTest.ABS_DMMMY_1.type, STA_DONE);
        test1 = CommandParser.stripIgnoreSymbols(test1);
        assertEquals(mergeStrings(TERM_1, TERM_2, TAG+TERM_7,
                TERM_3, " "+TAG+TERM_4+" ", PRIORITY+PRI_HIGH, IGNORE,
                PRIORITY+TERM_5, TimeTest.ABS_12_HOURS_LONG_1.type,
                DateTest.ABS_DMMMY_1.type, STA_DONE), test1);
    }

    @Test
    public void testParseTags() {
        String test1 = mergeStrings(TERM_1, TERM_2, PRI_MED, TAG);
        String test2 = mergeStrings(TERM_1, TERM_2, PRI_MED, TAG+TERM_6);
        String test3 = mergeStrings(TAG+TERM_3, TERM_7, PRI_HIGH, TERM_1);
        String test4 = mergeStrings(TERM_1, TAG+TERM_5, TAG+TERM_7, PRI_MED);

        // no tag, tag symbol on its own
        Tag[] tags = CommandParser.parseTags(test1);
        assertNull(tags);

        // single tag, at end
        tags = CommandParser.parseTags(test2);
        StringBuilder result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(TERM_6, result.toString().trim());

        // single tag, elsewhere
        tags = CommandParser.parseTags(test3);
        result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(TERM_3, result.toString().trim());

        // multiple tags
        tags = CommandParser.parseTags(test4);
        result = new StringBuilder();
        for (Tag t : tags) {
            result.append(t.toString()).append(' ');
        }
        assertEquals(TERM_5 + ' ' + TERM_7, result.toString().trim());
    }

    @Test
    public void testParsePriority() {
        String test1 = mergeStrings(TERM_1, TERM_2, PRIORITY+PRI_LOW, TAG+TERM_6);
        String test2 = mergeStrings(TAG+TERM_3, TERM_7, PRIORITY+PRI_HIGH, TERM_1);
        String test3 = mergeStrings(TERM_1, TAG+TERM_5, TAG+TERM_7, PRIORITY+PRI_MED);
        String test4 = mergeStrings(TERM_1, TAG+TERM_7, TERM_3);
        String test5 = mergeStrings(TERM_1, PRIORITY+PRI_HIGH, TAG+TERM_7, PRIORITY+PRI_MED);
        String test6 = mergeStrings(IGNORE+PRIORITY+PRI_HIGH+IGNORE, TAG+TERM_7, PRIORITY+PRI_MED);

        // low priority
        Priority priority = CommandParser.parsePriority(test1);
        assertEquals(Priority.LOW, priority);

        // high priority
        priority = CommandParser.parsePriority(test2);
        assertEquals(Priority.HIGH, priority);

        // med priority
        priority = CommandParser.parsePriority(test3);
        assertEquals(Priority.MEDIUM, priority);

        // no priority - take default (null for now)
        priority = CommandParser.parsePriority(test4);
        assertNull(priority);

        // multiple priority - should take the first
        priority = CommandParser.parsePriority(test5);
        assertEquals(Priority.HIGH, priority);

        // two priorities with ignore on first - take second only
        priority = CommandParser.parsePriority(test6);
        assertEquals(Priority.MEDIUM, priority);
    }

    @Test
    public void testParseStatus() {
        String test1 = mergeStrings(TERM_1, TERM_2, TAG+TERM_6);
        String test2 = mergeStrings(TAG+TERM_3, TERM_7, STA_DONE, TERM_1);
        String test3 = mergeStrings(TERM_1, STA_UNDONE, TAG+TERM_7, STA_DONE);

        // no status
        Status[] statuses = CommandParser.parseStatuses(test1);
        assertNull(statuses);

        // single status
        statuses = CommandParser.parseStatuses(test2);
        assertEquals(Status.DONE, statuses[0]);

        // multiple statuses
        statuses = CommandParser.parseStatuses(test3);
        assertEquals(Status.UNDONE, statuses[0]);
        assertEquals(Status.DONE, statuses[1]);
    }

    private String mergeStrings(String... strings) {
        StringBuilder sB = new StringBuilder();
        for (String s : strings) {
            sB.append(s).append(' ');
        }
        return sB.toString().trim();
    }

}
