package manager.datamanager.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import manager.datamanager.freetimemanager.FreeTimeSearchManager;
import manager.datamanager.freetimemanager.Interval;
import manager.result.FreeTimeResult;
import manager.result.Result;

import org.junit.Test;

import data.TaskData;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class FreeTimeManagerTest {
	
	TaskInfo task1 = createTask("apple", getTime(3,0), getDate(11,16),getTime(9, 0), getDate(11, 16));
	TaskInfo task2 = createTask("banana", getTime(5, 0), getDate(11, 16), getTime(12, 0), getDate(11, 17));
	TaskInfo task3 = createTask("cheese", getTime(13, 0), getDate(11,15), getTime(19, 0), getDate(11, 15));
	TaskInfo task4 = createTask("dip", null, null , getTime(11, 0), getDate(11, 16));
	TaskInfo task5 = createTask("egg", getTime(19, 0), getDate(10, 30), getTime(1, 20), getDate(11,16));
	TaskInfo task6 = createTask("fish", null, null, null, null);
	TaskInfo task7 = createTask("gum", getTime(10, 0), getDate(11, 8), getTime(16, 0), getDate(11, 10));
	TaskInfo task8 = createTask("ham", null, null, null, getDate(11, 3));

	/**
	 * Test simple task within a day
	 */
	@Test
	public void test1(){ 
		TaskData taskData = new TaskData();
		FreeTimeSearchManager manager = new FreeTimeSearchManager(taskData);
		Result result;

		taskData.add(task1);
		result = manager.searchFreeTimeSlot(getDate(11, 16));
		FreeTimeResult freeTimeResult = (FreeTimeResult) result;
		
		
		
		ArrayList<Interval> freeTimeList = new ArrayList<>();
        freeTimeList.add(makeInterval(0, 3));
        freeTimeList.add(makeInterval(10, 24));
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
	}
	
	/**
	 * Test task that last until next day
	 */
	@Test
	public void test2(){
		TaskData taskData = new TaskData();
		FreeTimeSearchManager manager = new FreeTimeSearchManager(taskData);
		Result result;
		
		taskData.add(task1);
		taskData.add(task2);
		result = manager.searchFreeTimeSlot(getDate(11, 16));
		FreeTimeResult freeTimeResult = (FreeTimeResult) result;
		
		
		ArrayList<Interval> freeTimeList = new ArrayList<>();
        freeTimeList.add(makeInterval(0, 3));
		
		
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
	}
	
	/**
	 * Test task with only end date and time
	 */
	@Test
	public void test3(){
		TaskData taskData = new TaskData();
		FreeTimeSearchManager manager = new FreeTimeSearchManager(taskData);
		Result result;
		
		taskData.add(task1);
		taskData.add(task3);
		taskData.add(task4);
		
		result = manager.searchFreeTimeSlot(getDate(11, 16));
		FreeTimeResult freeTimeResult = (FreeTimeResult) result;
		
				
		ArrayList<Interval> freeTimeList = new ArrayList<>();
        freeTimeList.add(makeInterval(0, 3));
        freeTimeList.add(makeInterval(12, 24));
		
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
	}
	
	/**
	 * Test task that start before specified date and last until that date
	 */
	@Test
	public void test4(){
		TaskData taskData = new TaskData();
		FreeTimeSearchManager manager = new FreeTimeSearchManager(taskData);
		Result result;
		
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task3);
		taskData.add(task4);
		taskData.add(task5);
		result = manager.searchFreeTimeSlot(getDate(11, 16));
		FreeTimeResult freeTimeResult = (FreeTimeResult) result;
		
		
		ArrayList<Interval> freeTimeList = new ArrayList<>();
        freeTimeList.add(makeInterval(2, 3));
		
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
	}
	
	/**
	 * Test totally free day with no task
	 */
	@Test
	public void test5(){
		TaskData taskData = new TaskData();
		FreeTimeSearchManager manager = new FreeTimeSearchManager(taskData);
		Result result;
		
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task3);
		taskData.add(task4);
		taskData.add(task5);
		
		result = manager.searchFreeTimeSlot(getDate(11, 20));
		FreeTimeResult freeTimeResult = (FreeTimeResult) result;
		
		
		ArrayList<Interval> freeTimeList = new ArrayList<>();
        freeTimeList.add(makeInterval(0, 24));
		
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
	}
	
	private Interval makeInterval(int startHour, int endHour) {
	    return new Interval(getTime(startHour, 0), getTime(endHour-1, 59), false);
	}
	
	private boolean assertResultEquals(ArrayList<Interval> list1, ArrayList<Interval> list2){
		boolean result = true;
		int size1 = list1.size();
		int size2 = list2.size();
		
		assertEquals(size1, size2);
		for (int i = 0; i < size1; i++){
			assertEquals(list1.get(i).getStartTime(), list2.get(i).getStartTime());
			assertEquals(list1.get(i).getEndTime(), list2.get(i).getEndTime());
		}
		return result;
	}
	
	private static TaskInfo createTask(String name,LocalTime startTime, LocalDate startDate,
			LocalTime endTime, LocalDate endDate){
		TaskInfo taskInfo = TaskInfo.create();
		taskInfo.name = name;
		taskInfo.startTime = startTime;
		taskInfo.startDate = startDate;
		taskInfo.endTime = endTime;
		taskInfo.endDate = endDate;
		return taskInfo;
	}
	
	private static LocalTime getTime(int hour, int minute){
		return LocalTime.of(hour, minute);
	}

	private static LocalDate getDate(int month, int day){
		return LocalDate.of(2014, month, day);
	}
}
