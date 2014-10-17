package manager.datamanager.test;


import java.time.LocalDate;
import java.time.LocalTime;

import manager.datamanager.FreeTimeSlotManager;
import manager.result.FreeDayResult;
import manager.result.Result;
import data.TaskData;
import data.taskinfo.TaskInfo;

public class FreeTimeSlotManagerTest {
	
	public static void main(String[] args){
		
		TaskData taskData = new TaskData();
		
		TaskInfo task1 = createTask("apple", getTime(3,0), getDate(11,16),getTime(9, 0), getDate(11, 16));
		TaskInfo task2 = createTask("banana", getTime(5, 0), getDate(11, 16), getTime(12, 0), getDate(11, 17));
		TaskInfo task3 = createTask("cheese", getTime(13, 0), getDate(11,20), getTime(19, 0), getDate(11, 20));
		TaskInfo task4 = createTask("dip", null, null , getTime(10, 0), getDate(11, 19));
		TaskInfo task5 = createTask("egg", getTime(19, 0), getDate(10, 30), getTime(20, 0), getDate(10, 30));
		TaskInfo task6 = createTask("fish", null, null, null, null);
		
		taskData.add(task3);
		taskData.add(task5);
		taskData.add(task1);
		taskData.add(task2);
		taskData.add(task4);
		taskData.add(task6);

		
		FreeTimeSlotManager manager = new FreeTimeSlotManager(taskData);
		System.out.println(manager.taskList.size());
		Result result;
		
//		result = manager.searchFreeDay(getDate(10,9), getDate(10, 28));
//		FreeDayResult finResult = (FreeDayResult) result;
//		System.out.println(finResult.getFreeDate().size());
//		System.out.println(finResult.getFreeDate());
//		System.out.println(finResult.getStartDate());
//		System.out.println(finResult.getLastTaskEndDate());
		
		result = manager.searchFreeTimeSlot(getTime(5, 0), getDate(10, 9), getTime(8, 0), getDate(10, 28));
		
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
