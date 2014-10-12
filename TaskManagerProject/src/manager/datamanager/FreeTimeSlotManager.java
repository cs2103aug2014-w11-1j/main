package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import manager.result.FreeDayResult;
import manager.result.Result;
import manager.result.Result.Type;
import data.Task;
import data.TaskData;
import data.TaskId;

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
		
		
		
		return null;
	}
	
	public Result searchFreeDay(LocalDate startDate, LocalDate endDate){
		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDate = new ArrayList<LocalDate>(); 
		
		for (Task task : taskList){
			if (task.getTaskInfo().getEndDate().isAfter(LocalDate.now())){  // search start from today, for those tasks end in future
				if (checkDate == null){         // first task initialization
					checkDate = task.getTaskInfo().getEndDate();
				}else{							// if new task's start date < checkDate, update freeDay, else update checkDate
					if (task.getTaskInfo().getStartDate().isAfter(checkDate)){
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(task.getTaskInfo().getStartDate())){
							freeDate.add(tempDate);
							tempDate = tempDate.minusDays(-1);
					}
					checkDate = task.getTaskInfo().getEndDate().isBefore(checkDate) ? checkDate :  task.getTaskInfo().getEndDate();
					}
				}
			}
		}
		
		return new FreeDayResult(Type.FREE_DAY, freeDate, checkDate);
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
	
}
