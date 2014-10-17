package manager.datamanager.test;

import java.time.LocalDate;
import java.time.LocalTime;

import manager.datamanager.FreeTimeSlotManager;
import manager.result.AddResult;
import manager.result.FreeDayResult;
import manager.result.Result;

import org.junit.Test;

import data.TaskData;
import data.taskinfo.TaskInfo;

public class FreeTimeSlotManagerTest {


    @Test
    public void test() {
		
		TaskData taskData = new TaskData();
		
		TaskInfo task1 = createTask("apple", getTime(3,0), getDate(11,16),getTime(9, 0), getDate(11, 16));
		TaskInfo task2 = createTask("banana", getTime(5, 0), getDate(11, 16), getTime(12, 0), getDate(11, 17));
		TaskInfo task3 = createTask("cheese", getTime(13, 0), getDate(11,20), getTime(19, 0), getDate(11, 20));
		TaskInfo task4 = createTask("dip", null, null , getTime(10, 0), getDate(11, 19));
		TaskInfo task5 = createTask("egg", getTime(19, 0), getDate(10, 30), getTime(20, 0), getDate(10, 30));
		TaskInfo task6 = createTask("fish", null, null, null, null);
		TaskInfo task7 = createTask("gum", getTime(10, 0), getDate(11, 8), getTime(16, 0), getDate(11, 10));
		TaskInfo task8 = createTask("ham", null, null, null, getDate(11, 3));
		
		
		taskData.add(task3);
		taskData.add(task5);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task4);
		taskData.add(task6);
		taskData.add(task7);
		taskData.add(task8);

		
		FreeTimeSlotManager manager = new FreeTimeSlotManager(taskData);
		System.out.println(manager.taskList.size());
		Result result;
		
		result = manager.searchFreeDay(getDate(10,9), getDate(10, 28));
		FreeDayResult finResult = (FreeDayResult) result;
		System.out.println(finResult.getFreeDate().size());
		System.out.println(finResult.getFreeDate());
		System.out.println(finResult.getStartDate());
		System.out.println(finResult.getLastTaskEndDate());
		
//		result = manager.searchFreeTimeSlot(getTime(6, 0), getDate(10, 9), getTime(8, 0), getDate(10, 28));
//		FreeDayResult finResult = (FreeDayResult) result;
//		System.out.println(finResult.getFreeDate().size());
//		System.out.println(finResult.getFreeDate());
//		System.out.println(finResult.getStartDate());
//		System.out.println(finResult.getLastTaskEndDate());
		
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
