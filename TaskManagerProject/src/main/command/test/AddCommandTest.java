package main.command.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import main.command.AddCommand;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class AddCommandTest {

    private static final String TIME_STR_EARLY = "9:46 AM";
    private static final String TIME_STR_LATE = "3:16 PM";

    private static final LocalTime TIME_EARLY = LocalTime.of(9, 46);
    private static final LocalTime TIME_LATE = LocalTime.of(15, 16);

    private static final String DATE_STR_EARLY = "24/8/14";
    private static final String DATE_STR_LATE = "13/9/14";

    private static final LocalDate DATE_EARLY = LocalDate.of(2014, 8, 24);
    private static final LocalDate DATE_LATE = LocalDate.of(2014, 9, 13);

    private static final String TAG = "#";
    private static final String IGNORE = "\"";
    private static final String PRIORITY = "+";
    private static final String PRI_HIGH = "HIGH";
    private static final String PRI_LOW = "low";

    @Test
    public void testParseTask() {
        // terms, ignored terms (1 tag, 1 priority), 2 tags, 1 priority, 1 date, 1 time
        String termA = "a";
        String termB = "bc";
        String termC = "dEF";

        String test = mergeStrings(new String[]{TAG+termA, IGNORE + termB + IGNORE,
                TAG+termC, termC, IGNORE, TAG+termA, PRIORITY+PRI_HIGH, IGNORE,
                PRIORITY+termA, PRIORITY+PRI_LOW, TIME_STR_EARLY, DATE_STR_EARLY});
        TaskInfo task = getTaskInfo(test);

        assertEquals(mergeStrings(new String[]{termB, termC, TAG+termA,
                PRIORITY+PRI_HIGH, PRIORITY+termA}), task.name);

        assertEquals(null, task.startDate);
        assertEquals(DATE_EARLY, task.endDate);
        assertEquals(null, task.startTime);
        assertEquals(TIME_EARLY, task.endTime);

        StringBuilder tagString = new StringBuilder();
        for (Tag tag : task.tags) {
            tagString.append(tag.toString()).append(' ');
        }

        assertEquals(mergeStrings(new String[]{termA, termC}),
                tagString.toString().trim());

        assertEquals(Priority.LOW, task.priority);
    }

    /**
     * Test for AddCommand-specific parsing of dates and times.
     *
     * Tests for validity of conversion from String to a pair of
     * LocalDate and LocalTime are handled in DateParserTest instead.
     */
    @Test
    public void testDateTimeParsing() {
        TaskInfo task;
        LocalTime tNow = LocalTime.now();
        LocalDate dNow = LocalDate.now();

        // String naming: [d|t[...[d|t]]][A|B|C... type]

        // boundary case; partition: empty string
        String none = "";
        task = getTaskInfoWithoutName(none);

        assertEquals(null, task.startDate);
        assertEquals(null, task.endDate);
        assertEquals(null, task.startTime);
        assertEquals(null, task.endTime);

        // boundary case; partition: more dates than times
        String d = DATE_STR_EARLY;
        task = getTaskInfoWithoutName(d);

        assertEquals(null, task.startDate);
        assertEquals(DATE_EARLY, task.endDate);
        assertEquals(null, task.startTime);
        assertEquals(null, task.endTime);

        // boundary case; partition: no date, one time
        String t = TIME_STR_EARLY;
        task = getTaskInfoWithoutName(t);

        assertEquals(null, task.startDate);
        if (tNow.isBefore(TIME_EARLY)) {
            assertEquals(dNow, task.endDate);
        } else {
            assertEquals(dNow.plusDays(1), task.endDate);
        }
        assertEquals(null, task.startTime);
        assertEquals(TIME_EARLY, task.endTime);

        // boundary case; partition: no date, two times, EARLY -> LATE
        String ttA = mergeStrings(new String[]{
                TIME_STR_EARLY, TIME_STR_LATE});
        task = getTaskInfoWithoutName(ttA);

        if (tNow.isBefore(TIME_EARLY)) {
            assertEquals(dNow, task.startDate);
            assertEquals(dNow, task.endDate);
        } else {
            assertEquals(dNow.plusDays(1), task.startDate);
            assertEquals(dNow.plusDays(1), task.endDate);
        }
        assertEquals(TIME_EARLY, task.startTime);
        assertEquals(TIME_LATE, task.endTime);

        // boundary case; partition: no date, two times, LATE -> EARLY
        String ttB = mergeStrings(new String[]{
                TIME_STR_LATE, TIME_STR_EARLY});
        task = getTaskInfoWithoutName(ttB);

        if (tNow.isBefore(TIME_LATE)) {
            assertEquals(dNow, task.startDate);
            assertEquals(dNow.plusDays(1), task.endDate);
        } else {
            assertEquals(dNow.plusDays(1), task.startDate);
            assertEquals(dNow.plusDays(2), task.endDate);
        }
        assertEquals(TIME_LATE, task.startTime);
        assertEquals(TIME_EARLY, task.endTime);

        // boundary case; partition: one date, one time
        String dt = mergeStrings(new String[]{
                DATE_STR_EARLY, TIME_STR_EARLY});

        task = getTaskInfoWithoutName(dt);

        assertEquals(null, task.startDate);
        assertEquals(DATE_EARLY, task.endDate);
        assertEquals(null, task.startTime);
        assertEquals(TIME_EARLY, task.endTime);

        // boundary case; partition: one date, two times
        String dtt = mergeStrings(new String[]{
                DATE_STR_EARLY, TIME_STR_EARLY, TIME_STR_LATE});

        task = getTaskInfoWithoutName(dtt);

        assertEquals(DATE_EARLY, task.startDate);
        assertEquals(DATE_EARLY, task.endDate);
        assertEquals(TIME_EARLY, task.startTime);
        assertEquals(TIME_LATE, task.endTime);

        // boundary case; partition: two date, two times
        String dtdt = mergeStrings(new String[]{
                DATE_STR_EARLY, TIME_STR_EARLY,
                DATE_STR_LATE, TIME_STR_LATE});

        task = getTaskInfoWithoutName(dtdt);

        assertEquals(DATE_EARLY, task.startDate);
        assertEquals(DATE_LATE, task.endDate);
        assertEquals(TIME_EARLY, task.startTime);
        assertEquals(TIME_LATE, task.endTime);
    }

    /**
     * Test for AddCommand-specific parsing of priority.
     *
     * Tests for validity of conversion from String to a priority
     * are handled in CommandParserTest instead.
     */
    @Test
    public void testPriorityParsing() {
        TaskInfo task;

        // boundary case; partition: has priority
        String yesP = PRIORITY + PRI_HIGH;
        task = getTaskInfoWithoutName(yesP);

        assertEquals(Priority.HIGH, task.priority);

        //// boundary case; partition: has no priority
        String noP = "";
        task = getTaskInfoWithoutName(noP);
        assertEquals(task.priority, Priority.NONE);
    }

    private TaskInfo getTaskInfo(String test) {
        StubManagerHolder mHolder = new StubManagerHolder();
        AddCommand cmd = new AddCommand(test, mHolder);
        cmd.execute();
        return mHolder.getAddManager().taskInfo;
    }

    private TaskInfo getTaskInfoWithoutName(String test) {
        StubManagerHolder mHolder = new StubManagerHolder();
        AddCommand cmd = new AddCommand(". " + test, mHolder);
        cmd.execute();
        return mHolder.getAddManager().taskInfo;
    }

    private String mergeStrings(String[] strings) {
        StringBuilder sB = new StringBuilder();
        for (String s : strings) {
            sB.append(s).append(' ');
        }
        return sB.toString().trim();
    }
}