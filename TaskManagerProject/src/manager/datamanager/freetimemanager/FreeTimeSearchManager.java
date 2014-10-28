package manager.datamanager.freetimemanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import manager.datamanager.AbstractManager;
import data.TaskData;
import data.taskinfo.TaskInfo;

public class FreeTimeSearchManager extends AbstractManager {
	
	
	private ArrayList<TaskInfo> taskList;
	private ArrayList<Interval> freeTimeList;
	//private final LocalTime DAY_START_TIME
	
	public FreeTimeSearchManager(TaskData taskData) {
		super(taskData);
	}
	
	private Interval getTaskTimeOnDate(TaskInfo taskInfo, LocalDate date){
		
		
		if (taskInfo.getEndDate() == null){ // floating task
			return null;
		} else if (taskInfo.getEndTime() == null){ // no time task
			return null;
		} else if (taskInfo.getEndDate().isBefore(date)){ // past task
			return null;
		} else if (taskInfo.getStartDate() != null){
			if (taskInfo.getStartDate().isAfter(date)){ // future task
				return null;
			}
		} 
		if (taskInfo.getEndDate().isEqual(date)){ // end that day
			if (taskInfo.getStartDate() == null){
				//
			} else if (taskInfo.getStartDate().equals(date)){
				return new Interval(taskInfo.getStartTime(), taskInfo.getEndTime());
			} else if (taskInfo.getStartDate().isBefore(date)){
			//	return new Interval(startTime, endTime)
			}
		}
	}
}
