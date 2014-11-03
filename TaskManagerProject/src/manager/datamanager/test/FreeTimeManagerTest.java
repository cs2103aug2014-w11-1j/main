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
		freeTimeList.add(new Interval(getTime(0, 0), getTime(0, 59),false));
		freeTimeList.add(new Interval(getTime(1, 0), getTime(1, 59),false));
		freeTimeList.add(new Interval(getTime(2, 0), getTime(2, 59),false));
		freeTimeList.add(new Interval(getTime(10, 0), getTime(10, 59),false));
		freeTimeList.add(new Interval(getTime(11, 0), getTime(11, 59),false));
		freeTimeList.add(new Interval(getTime(12, 0), getTime(12, 59),false));
		freeTimeList.add(new Interval(getTime(13, 0), getTime(13, 59),false));
		freeTimeList.add(new Interval(getTime(14, 0), getTime(14, 59),false));
		freeTimeList.add(new Interval(getTime(15, 0), getTime(15, 59),false));
		freeTimeList.add(new Interval(getTime(16, 0), getTime(16, 59),false));
		freeTimeList.add(new Interval(getTime(17, 0), getTime(17, 59),false));
		freeTimeList.add(new Interval(getTime(18, 0), getTime(18, 59),false));
		freeTimeList.add(new Interval(getTime(19, 0), getTime(19, 59),false));
		freeTimeList.add(new Interval(getTime(20, 0), getTime(20, 59),false));
		freeTimeList.add(new Interval(getTime(21, 0), getTime(21, 59),false));
		freeTimeList.add(new Interval(getTime(22, 0), getTime(22, 59),false));
		freeTimeList.add(new Interval(getTime(23, 0), getTime(23, 59),false));
		
		
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
		freeTimeList.add(new Interval(getTime(0, 0), getTime(0, 59),false));
		freeTimeList.add(new Interval(getTime(1, 0), getTime(1, 59),false));
		freeTimeList.add(new Interval(getTime(2, 0), getTime(2, 59),false));
		
		
		
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
		freeTimeList.add(new Interval(getTime(0, 0), getTime(0, 59),false));
		freeTimeList.add(new Interval(getTime(1, 0), getTime(1, 59),false));
		freeTimeList.add(new Interval(getTime(2, 0), getTime(2, 59),false));
		freeTimeList.add(new Interval(getTime(12, 0), getTime(12, 59),false));
		freeTimeList.add(new Interval(getTime(13, 0), getTime(13, 59),false));
		freeTimeList.add(new Interval(getTime(14, 0), getTime(14, 59),false));
		freeTimeList.add(new Interval(getTime(15, 0), getTime(15, 59),false));
		freeTimeList.add(new Interval(getTime(16, 0), getTime(16, 59),false));
		freeTimeList.add(new Interval(getTime(17, 0), getTime(17, 59),false));
		freeTimeList.add(new Interval(getTime(18, 0), getTime(18, 59),false));
		freeTimeList.add(new Interval(getTime(19, 0), getTime(19, 59),false));
		freeTimeList.add(new Interval(getTime(20, 0), getTime(20, 59),false));
		freeTimeList.add(new Interval(getTime(21, 0), getTime(21, 59),false));
		freeTimeList.add(new Interval(getTime(22, 0), getTime(22, 59),false));
		freeTimeList.add(new Interval(getTime(23, 0), getTime(23, 59),false));
		
		
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
		freeTimeList.add(new Interval(getTime(2, 0), getTime(2, 59),false));
		
		
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
		freeTimeList.add(new Interval(getTime(0, 0), getTime(0, 59),false));
		freeTimeList.add(new Interval(getTime(1, 0), getTime(1, 59),false));
		freeTimeList.add(new Interval(getTime(2, 0), getTime(2, 59),false));
		freeTimeList.add(new Interval(getTime(3, 0), getTime(3, 59),false));
		freeTimeList.add(new Interval(getTime(4, 0), getTime(4, 59),false));
		freeTimeList.add(new Interval(getTime(5, 0), getTime(5, 59),false));
		freeTimeList.add(new Interval(getTime(6, 0), getTime(6, 59),false));
		freeTimeList.add(new Interval(getTime(7, 0), getTime(7, 59),false));
		freeTimeList.add(new Interval(getTime(8, 0), getTime(8, 59),false));
		freeTimeList.add(new Interval(getTime(9, 0), getTime(9, 59),false));
		freeTimeList.add(new Interval(getTime(10, 0), getTime(10, 59),false));
		freeTimeList.add(new Interval(getTime(11, 0), getTime(11, 59),false));
		freeTimeList.add(new Interval(getTime(12, 0), getTime(12, 59),false));
		freeTimeList.add(new Interval(getTime(13, 0), getTime(13, 59),false));
		freeTimeList.add(new Interval(getTime(14, 0), getTime(14, 59),false));
		freeTimeList.add(new Interval(getTime(15, 0), getTime(15, 59),false));
		freeTimeList.add(new Interval(getTime(16, 0), getTime(16, 59),false));
		freeTimeList.add(new Interval(getTime(17, 0), getTime(17, 59),false));
		freeTimeList.add(new Interval(getTime(18, 0), getTime(18, 59),false));
		freeTimeList.add(new Interval(getTime(19, 0), getTime(19, 59),false));
		freeTimeList.add(new Interval(getTime(20, 0), getTime(20, 59),false));
		freeTimeList.add(new Interval(getTime(21, 0), getTime(21, 59),false));
		freeTimeList.add(new Interval(getTime(22, 0), getTime(22, 59),false));
		freeTimeList.add(new Interval(getTime(23, 0), getTime(23, 59),false));
		
		
		assertResultEquals(freeTimeList, freeTimeResult.getFreeTimeList());
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
