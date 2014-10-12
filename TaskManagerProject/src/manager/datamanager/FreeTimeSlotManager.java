package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import manager.result.FreeDayResult;
import manager.result.Result;
import manager.result.Result.Type;
import data.Task;
import data.TaskData;
import data.taskinfo.TaskInfo;

public class FreeTimeSlotManager extends AbstractManager{
	
	private ArrayList<Task> taskList;
	private int size;

	public FreeTimeSlotManager(TaskData taskData) {
		super(taskData);
		taskList = TaskListWithDate();
		sortTask();
		size = taskList.size();
	}

	private ArrayList<Task> TaskListWithDate() {
		ArrayList<Task> taskList = taskData.getTaskList();
		ArrayList<Task> taskListWithDate = new ArrayList<Task>();
		for ( Task task : taskList){
			if (task.getEndDate() != null){
				taskListWithDate.add(task);
			}
		}
		return taskListWithDate;
	}

	public Result searchFreeTimeSlot(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate EndDate){
		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>(); 
		ArrayList<Task> taskListContainingTimeSlot = findTaskContainingTimeSlot(startTime, endTime);
		
		for (Task task : taskListContainingTimeSlot){
			if (lastContainingDate(task, startTime, endTime).isAfter(LocalDate.now())){
				if (checkDate == null){
					checkDate = lastContainingDate(task, startTime, endTime);
				}else{
					if (firstContainingDate(task, startTime, endTime).isAfter(checkDate)){
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(firstContainingDate(task, startTime, endTime))){
							freeDays.add(tempDate);
							tempDate = tempDate.minusDays(-1);
						}
						checkDate = lastContainingDate(task, startTime, endTime).isBefore(checkDate) ? checkDate : lastContainingDate(task, startTime, endTime);
					}
				}
			}
		}
		
		return new FreeDayResult(Type.FREE_DAY, freeDays, checkDate);
	}
	
	public Result searchFreeDay(LocalDate startDate, LocalDate endDate){
		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>(); 
		
		for (Task task : taskList){
			if (task.getTaskInfo().getEndDate().isAfter(LocalDate.now())){  // search start from today, for those tasks end in future
				if (checkDate == null){         // first task initialization
					checkDate = task.getTaskInfo().getEndDate();
				}else{							// if new task's start date < checkDate, update freeDay, else update checkDate
					if (task.getTaskInfo().getStartDate().isAfter(checkDate)){
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(task.getTaskInfo().getStartDate())){
							freeDays.add(tempDate);
							tempDate = tempDate.minusDays(-1);
					}
					checkDate = task.getTaskInfo().getEndDate().isBefore(checkDate) ? checkDate :  task.getTaskInfo().getEndDate();
					}
				}
			}
		}
		
		return new FreeDayResult(Type.FREE_DAY, freeDays, checkDate);
	}
	
	private void sortTask(){
    Collections.sort(taskList, new Comparator<Task>() {
    	@Override
        public int compare(Task task1, Task task2) {
            
            if (task1.getTaskInfo().getStartDate().isBefore(task2.getTaskInfo().getStartDate())) {
                return -1;
            } else if (task1.getTaskInfo().getStartDate().isAfter(task2.getTaskInfo().getStartDate())) {
                return 1;
            } else {
                return 0;
            }
        }
    });
	}
	
	private boolean isContainTimeSlot(Task task, LocalTime startTime, LocalTime endTime){
		TaskInfo taskInfo = task.getTaskInfo();
		if (taskInfo.getStartDate() == taskInfo.getEndDate()){  // One day task
			if ((taskInfo.getStartTime().isAfter(endTime)) || (taskInfo.getEndTime().isBefore(startTime))){
				return false;
			} else{
				return true;
			}
		}else{		// Task that last more than 1 day
			if((taskInfo.getStartDate().minusDays(-1).isEqual(taskInfo.getEndDate())) 
					&& (taskInfo.getStartTime().isAfter(endTime)) 
					&& (taskInfo.getEndTime().isBefore(startTime))){
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<Task> findTaskContainingTimeSlot(LocalTime startTime, LocalTime endTime){
		ArrayList<Task> matchingTask = new ArrayList<Task>();
		for (Task task : taskList){
			if (isContainTimeSlot(task, startTime, endTime)){
				matchingTask.add(task);
			}
		}
		return matchingTask;
	}
	private LocalDate lastContainingDate(Task task, LocalTime startTime, LocalTime endTime){
		if (task.getTaskInfo().getEndTime().isBefore(startTime)){
			return task.getTaskInfo().getEndDate().minusDays(1);
		}else{
			return task.getTaskInfo().getEndDate();
		}
	}
	
	private LocalDate firstContainingDate(Task task, LocalTime startTime, LocalTime endTime){
		if (task.getTaskInfo().getStartTime().isAfter(endTime)){
			return task.getTaskInfo().getStartDate().minusDays(-1);
		}else{
			return task.getTaskInfo().getStartDate();
		}
	}
}
