package manager.datamanager.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import manager.datamanager.FreeDaySearchManager;
import manager.result.FreeDayResult;
import manager.result.Result;

import org.junit.Test;

import data.TaskData;
import data.taskinfo.TaskInfo;

public class FreeDayManagerTest {

	TaskInfo task1 = createTask("apple", getTime(3,0), getDate(11,16),getTime(9, 0), getDate(11, 16));
	TaskInfo task2 = createTask("banana", getTime(5, 0), getDate(11, 16), getTime(12, 0), getDate(11, 17));
	TaskInfo task3 = createTask("cheese", getTime(13, 0), getDate(11,20), getTime(19, 0), getDate(11, 20));
	TaskInfo task4 = createTask("dip", null, null , getTime(10, 0), getDate(11, 19));
	TaskInfo task5 = createTask("egg", getTime(19, 0), getDate(10, 30), getTime(20, 0), getDate(10, 30));
	TaskInfo task6 = createTask("fish", null, null, null, null);
	TaskInfo task7 = createTask("gum", getTime(10, 0), getDate(11, 8), getTime(16, 0), getDate(11, 10));
	TaskInfo task8 = createTask("ham", null, null, null, getDate(11, 3));
	
	@Test
	public void testDate1() {
		TaskData taskData = new TaskData();
		
		// Tasks on 16, 17, 20 november
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task3);
		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getDate(10,9), getDate(11, 28));
		FreeDayResult finResult = (FreeDayResult) result;
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		freeDates.add(getDate(11, 18));
		freeDates.add(getDate(11, 19));
		LocalDate startDate = getDate(11, 16);
        LocalDate lastTaskEndDate = getDate(11, 20);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
	}
	
	
    @Test
    public void testDate2 () {
		
		TaskData taskData = new TaskData();

        // Tasks on 20, 16, 17, 3 november, (8-10) november
		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task8);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getDate(10,9), getDate(11, 18));
		FreeDayResult finResult = (FreeDayResult) result;
		
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		
		freeDates.add(getDate(11, 4));
		freeDates.add(getDate(11, 5));
		freeDates.add(getDate(11, 6));
		freeDates.add(getDate(11, 7));
		freeDates.add(getDate(11, 11));
		freeDates.add(getDate(11, 12));
		freeDates.add(getDate(11, 13));
		freeDates.add(getDate(11, 14));
		freeDates.add(getDate(11, 15));
		
		LocalDate startDate = getDate(11, 3);
		LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
		
//		result = manager.searchFreeTimeSlot(getTime(6, 0), getDate(10, 9), getTime(8, 0), getDate(10, 28));
//		FreeDayResult finResult = (FreeDayResult) result;
//		System.out.println(finResult.getFreeDate().size());
//		System.out.println(finResult.getFreeDate());
//		System.out.println(finResult.getStartDate());
//		System.out.println(finResult.getLastTaskEndDate());
		
	}

    @Test
    public void testDate3 () {
		
		TaskData taskData = new TaskData();

		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task5);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getDate(10,9), getDate(11, 18));
		FreeDayResult finResult = (FreeDayResult) result;
		
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		
		freeDates.add(getDate(10, 31));
		freeDates.add(getDate(11, 1));
		freeDates.add(getDate(11, 2));
		freeDates.add(getDate(11, 3));
		freeDates.add(getDate(11, 4));
		freeDates.add(getDate(11, 5));
		freeDates.add(getDate(11, 6));
		freeDates.add(getDate(11, 7));
		freeDates.add(getDate(11, 11));
		freeDates.add(getDate(11, 12));
		freeDates.add(getDate(11, 13));
		freeDates.add(getDate(11, 14));
		freeDates.add(getDate(11, 15));
		
		LocalDate startDate = getDate(10, 30);
		LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
    }
    
    @Test
    public void testDate4() {
		
		TaskData taskData = new TaskData();

		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task5);
		taskData.add(task6);
		taskData.add(task8);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getDate(10,9), getDate(11, 18));
		FreeDayResult finResult = (FreeDayResult) result;
		
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		
		freeDates.add(getDate(10, 31));
		freeDates.add(getDate(11, 1));
		freeDates.add(getDate(11, 2));
		freeDates.add(getDate(11, 4));
		freeDates.add(getDate(11, 5));
		freeDates.add(getDate(11, 6));
		freeDates.add(getDate(11, 7));
		freeDates.add(getDate(11, 11));
		freeDates.add(getDate(11, 12));
		freeDates.add(getDate(11, 13));
		freeDates.add(getDate(11, 14));
		freeDates.add(getDate(11, 15));
		
		LocalDate startDate = getDate(10, 30);
		LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
    }    
    
    @Test
    public void testTime1() {
    	
    	TaskData taskData = new TaskData();

        // Tasks on 20, 16, 17, 3 november, (8-10) november
		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task8);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;

        // Time matches:
        // Tasks on X20, y16, y17, y3 november, (8-10)->(9-10) november
        // range: 9 oct to 28 nov
		result = manager.searchFreeDay(getTime(6, 0), getDate(10, 9), getTime(8, 0), getDate(11, 28));
		FreeDayResult finResult = (FreeDayResult) result;
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();

        freeDates.add(getDate(11, 4));
        freeDates.add(getDate(11, 5));
        freeDates.add(getDate(11, 6));
        freeDates.add(getDate(11, 7));
        freeDates.add(getDate(11, 8));
        
		freeDates.add(getDate(11, 11));
		freeDates.add(getDate(11, 12));
		freeDates.add(getDate(11, 13));
		freeDates.add(getDate(11, 14));
		freeDates.add(getDate(11, 15));
		
		
		LocalDate startDate = getDate(11, 3);
        LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
    }
    
    @Test
    public void testTime2() {
    	
    	TaskData taskData = new TaskData();
    	
		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task8);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getTime(6, 0), getDate(11,14), getTime(8, 0), getDate(11, 28));
		FreeDayResult finResult = (FreeDayResult) result;
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		
		LocalDate startDate = getDate(11, 16);
        LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);

    }
    
    @Test
    public void testTime3 () {
		
		TaskData taskData = new TaskData();

		taskData.add(task3);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task7);
		taskData.add(task5);

		
		FreeDaySearchManager manager = new FreeDaySearchManager(taskData);
		Result result;
		
		result = manager.searchFreeDay(getTime(10, 0), getDate(10,9), getTime(11, 0), getDate(11, 18));
		FreeDayResult finResult = (FreeDayResult) result;
		
		
		ArrayList<LocalDate> freeDates = new ArrayList<>();
		
		
		freeDates.add(getDate(11, 11));
		freeDates.add(getDate(11, 12));
		freeDates.add(getDate(11, 13));
		freeDates.add(getDate(11, 14));
		freeDates.add(getDate(11, 15));
		
		LocalDate startDate = getDate(11, 8);
		LocalDate lastTaskEndDate = getDate(11, 17);
        FreeDayResult templateResult = new FreeDayResult(freeDates, startDate, lastTaskEndDate,null,null);
		
		assertResultEquals(templateResult, finResult);
    }
    
    private void assertResultEquals(FreeDayResult actual, FreeDayResult output) {
        assertEquals(actual.getFirstBusyDate(), output.getFirstBusyDate());
        assertEquals(actual.getLastBusyDate(), output.getLastBusyDate());
        assertEquals(actual.getFreeDateList().size(), output.getFreeDateList().size());
        assertEquals(actual.getFreeDateList(), output.getFreeDateList());
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
