package manager.datamanager.searchfilter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import manager.datamanager.searchfilter.DateTimeFilter;

import org.junit.Test;

import data.taskinfo.TaskInfo;

//@author A0113011L
public class DateTimeFilterTest {

	@Test
	public void testFloating() {
		LocalDateTime min = LocalDateTime.parse("2014-11-28T03:05:40");
		LocalDateTime max = LocalDateTime.parse("2014-11-29T01:02:03");
		DateTimeFilter filter = new DateTimeFilter(min, max);
		
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.endDate = null;
		taskInfo.endTime = null;
        taskInfo.startDate = null;
        taskInfo.startTime = null;
		
		assertFalse(filter.filter(taskInfo));
	}
	
	@Test
	public void testEndOnlyTrue() {
		LocalDateTime min = LocalDateTime.parse("2014-11-28T03:05:40");
		LocalDateTime max = LocalDateTime.parse("2014-11-29T01:02:03");
		DateTimeFilter filter = new DateTimeFilter(min, max);
		
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.endDate = LocalDate.parse("2014-11-28");
		taskInfo.endTime = LocalTime.parse("12:00:00");
		
		assertTrue(filter.filter(taskInfo));
	}
	
	@Test
	public void testEndOnlyFalse() {
		LocalDateTime min = LocalDateTime.parse("2014-11-28T03:05:40");
		LocalDateTime max = LocalDateTime.parse("2014-11-29T01:02:03");
		DateTimeFilter filter = new DateTimeFilter(min, max);
		
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.endDate = LocalDate.parse("2014-11-29");
		taskInfo.endTime = LocalTime.parse("02:00:00");
		
		assertFalse(filter.filter(taskInfo));		
	}
	
	@Test
	public void testEndOnlyExactMax() {
		LocalDateTime min = LocalDateTime.parse("2014-11-28T01:05:00");
		LocalDateTime max = LocalDateTime.parse("2014-11-28T03:05:00");
		DateTimeFilter filter = new DateTimeFilter(min, max);
		
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.endDate = LocalDate.parse("2014-11-28");
		taskInfo.endTime = LocalTime.parse("03:05:00");
		
		assertTrue(filter.filter(taskInfo));		
	}
	
	@Test
	public void testEndOnlyExactMin() {
		LocalDateTime min = LocalDateTime.parse("2014-11-28T01:05:00");
		LocalDateTime max = LocalDateTime.parse("2014-11-28T03:05:00");
		DateTimeFilter filter = new DateTimeFilter(min, max);
		
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.endDate = LocalDate.parse("2014-11-28");
		taskInfo.endTime = LocalTime.parse("01:05:00");
		
		assertTrue(filter.filter(taskInfo));		
	}
}
