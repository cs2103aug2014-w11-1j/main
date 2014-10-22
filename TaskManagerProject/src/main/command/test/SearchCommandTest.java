package main.command.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import main.command.SearchCommand;
import manager.datamanager.searchfilter.DateTimeFilter;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.KeywordFilter;

import org.junit.Test;

import data.taskinfo.TaskInfo;

public class SearchCommandTest {
    @Test
    public void testKeywordParsing() {
        KeywordFilter f;

        // empty name
        String emptyTestString = "";
        f = getKeywordFilter(emptyTestString);

        assertNull(f);

        // normal name
        String[] testStringArr = {"ab", "CDF"};
        String testString = mergeStrings(testStringArr);
        f = getKeywordFilter(testString);
        assertNotNull(f);
        assertTrue(areArraysEqual(testStringArr, f.getKeywords()));
    }

    @Test
    public void testDateTimeParsing() {
        DateTimeFilter f;

        // String naming: d|t[...d|t]

        LocalDate d1 = LocalDate.of(2014, 10, 17);
        LocalDate d2 = LocalDate.of(2014, 10, 18);
        LocalDate dNow = LocalDate.now();
        LocalTime t1 = LocalTime.of(15, 0);
        LocalTime t2 = LocalTime.of(18, 0);
        LocalTime tMin = LocalTime.MIN;
        LocalTime tMax = LocalTime.MAX;

        String none = "";
        f = getDateTimeFilter(none);
        assertNull(f);

        String t = "3 PM";
        f = getDateTimeFilter(t);
        assertDateTimeFilter(f, dNow, t1, dNow, t1);

        String tt = "3 PM 6 PM";
        f = getDateTimeFilter(tt);
        assertDateTimeFilter(f, dNow, t1, dNow, t2);

        String d = "17/10/14";
        f = getDateTimeFilter(d);
        assertDateTimeFilter(f, d1, tMin, d1, tMax);

        String dt = "17/10/14 3 PM";
        f = getDateTimeFilter(dt);
        assertDateTimeFilter(f, d1, t1, d1, t1);

        String dtt = "17/10/14 3 PM 6 PM";
        f = getDateTimeFilter(dtt);
        assertDateTimeFilter(f, d1, t1, d1, t2);

        String tdt = "3 PM 17/10/14 6 PM";
        f = getDateTimeFilter(tdt);
        assertDateTimeFilter(f, d1, t1, d1, t2);

        String ttd = "3 PM 6 PM 17/10/14";
        f = getDateTimeFilter(ttd);
        assertDateTimeFilter(f, d1, t1, d1, t2);

        String dd = "17/10/14 18/10/14";
        f = getDateTimeFilter(dd);
        assertDateTimeFilter(f, d1, tMin, d2, tMax);

        String tdd = "3 PM 17/10/14 18/10/14";
        f = getDateTimeFilter(tdd);
        assertDateTimeFilter(f, d1, t1, d2, tMax);

        String dtd = "17/10/14  3 PM 18/10/14";
        f = getDateTimeFilter(dtd);
        assertDateTimeFilter(f, d1, t1, d2, tMax);

        String ddt = "17/10/14 18/10/14 6 PM";
        f = getDateTimeFilter(ddt);
        assertDateTimeFilter(f, d1, tMin, d2, t2);

        String tdtd = "3 PM 17/10/14 6 PM 18/10/14";
        f = getDateTimeFilter(tdtd);
        assertDateTimeFilter(f, d1, t1, d2, t2);
    }

    /**
     * Checks through asserts if the given DateTimeFilter is valid within
     * the dates and times passed in through boundary checking.
     *
     * @param f
     *            is the given DateTimeFilter.
     * @param minDate
     *            is the minimum valid date.
     * @param minTime
     *            is the minimum valid time.
     * @param maxDate
     *            is the maximum valid date.
     * @param maxTime
     *            is the maximum valid time.
     */
    private void assertDateTimeFilter(DateTimeFilter f, LocalDate minDate,
            LocalTime minTime, LocalDate maxDate, LocalTime maxTime) {
        assertNotNull(f);

        TaskInfo task = TaskInfo.create();

        // boundary case; partition: valid, lower bound
        task.endDate = minDate;
        task.endTime = minTime;

        assertTrue(f.filter(task));

        // boundary case; partition: valid, upper bound
        task.endDate = maxDate;
        task.endTime = maxTime;

        assertTrue(f.filter(task));

        // boundary case; partition: invalid, lower bound
        task.endDate = LocalDateTime.of(minDate, minTime)
                .minusNanos(1).toLocalDate();
        task.endTime = LocalDateTime.of(minDate, minTime)
                .minusNanos(1).toLocalTime();

        assertFalse(f.filter(task));

        // boundary case; partition: invalid, upper bound
        task.endDate = LocalDateTime.of(maxDate, maxTime)
                .plusNanos(1).toLocalDate();
        task.endTime = LocalDateTime.of(maxDate, maxTime)
                .plusNanos(1).toLocalTime();

        assertFalse(f.filter(task));
    }

    private KeywordFilter getKeywordFilter(String test) {
        Filter[] filters = getFilters(test);
        for (Filter f : filters) {
            if (f instanceof KeywordFilter) {
                return (KeywordFilter) f;
            }
        }
        return null;
    }

    private DateTimeFilter getDateTimeFilter(String test) {
        Filter[] filters = getFilters(test);
        for (Filter f : filters) {
            if (f instanceof DateTimeFilter) {
                return (DateTimeFilter) f;
            }
        }
        return null;
    }

    private Filter[] getFilters(String test) {
        StubManagerHolder mHolder = new StubManagerHolder();
        SearchCommand cmd = new SearchCommand(test, mHolder);
        cmd.execute();
        return mHolder.getSearchManager().filters;
    }

    /**
     * Checks if two arrays are equals, regardless of ordering of elements.
     *
     * @param arr1
     *            is the first array.
     * @param arr2
     *            is the second array.
     * @return true if the arrays are equal without considering ordering.
     */
    private boolean areArraysEqual(Object[] arr1, Object[] arr2) {
        arr1 = arr1.clone();
        arr2 = arr2.clone();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }

    private String mergeStrings(String[] strings) {
        StringBuilder sB = new StringBuilder();
        for (String s : strings) {
            sB.append(s).append(' ');
        }
        return sB.toString().trim();
    }
}
