package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import manager.result.FreeDayResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;


//@author A0119432L
public class FreeDaySearchManager extends AbstractManager {

	private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);
    private static final LocalTime START_OF_DAY = LocalTime.of(0, 0);
    
    private ArrayList<TaskInfo> taskList;
	private int size;

	public FreeDaySearchManager(TaskData taskData) {
		super(taskData);
	}

	private ArrayList<TaskInfo> TaskListWithDate() {
		ArrayList<TaskInfo> taskListWithDate = new ArrayList<TaskInfo>();
		TaskId taskId = taskData.getFirst();
		TaskInfo task;
		while (taskId.isValid()) {
			task = taskData.getTaskInfo(taskId);
			if (task.getEndDate() != null) {
				taskListWithDate.add(task);
			}
			taskId = taskData.getNext(taskId);
		}
		return taskListWithDate;
	}

	private void updateTaskList() {
		taskList = TaskListWithDate();
		sortTask();
		size = taskList.size();
	}

	private void clearMemory() {
		taskList.clear();
		taskList = null;   
	}
	
	public Result searchFreeDay(LocalTime startTime, LocalDate startDate,
			LocalTime endTime, LocalDate endDate) {
	    
	    assert startBeforeEnd(startTime, startDate, endTime, endDate);
	    
		updateTaskList();

		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>();
		ArrayList<TaskInfo> taskListContainingTimeSlot = findTaskContainingTimeSlot(
				startTime, endTime);
		LocalDate firstStartDate = null;

		if (taskListContainingTimeSlot.size() == 0){
			return new FreeDayResult(null,null,null,startDate, endDate);
		}
		
		for (TaskInfo task : taskListContainingTimeSlot) {
			if (isMatchTimeSlot(startTime, startDate, endTime, endDate, task)) {
                LocalDate taskStartDate = getTaskStartDate(task);
                
                if (firstStartDate == null || 
                        taskStartDate.isBefore(firstStartDate)) {
                    firstStartDate = taskStartDate;
                }
                
				if (checkDate == null) {
					checkDate = lastContainingDate(task, startTime, endTime);
				} else {
					uodateFreeDay(startTime, startDate, endTime, endDate,
							checkDate, freeDays, task);
					checkDate = lastContainingDate(task, startTime, endTime)
							.isBefore(checkDate) ? checkDate : lastContainingDate(task, startTime, endTime);

				}
			}
		}
        assert (firstStartDate == null) == (checkDate == null);

		clearMemory();
		return new FreeDayResult(freeDays, firstStartDate, checkDate, startDate, endDate);
	}

	private boolean startBeforeEnd(LocalTime startTime, LocalDate startDate,
            LocalTime endTime, LocalDate endDate) {
        return !endDate.isBefore(startDate) && !endTime.isBefore(startTime);
    }

    private void uodateFreeDay(LocalTime startTime, LocalDate startDate,
			LocalTime endTime, LocalDate endDate, LocalDate checkDate,
			ArrayList<LocalDate> freeDays, TaskInfo task) {
		if (firstContainingDate(task, startTime, endTime).isAfter(
				checkDate)) {
			LocalDate tempDate = checkDate.minusDays(-1);
			while (tempDate.isBefore(firstContainingDate(task,
					startTime, endTime))) {
				
				if (tempDate.isAfter(startDate.minusDays(1))
						&& (tempDate.isBefore(endDate.minusDays(-1)))){
					freeDays.add(tempDate);
				}
				tempDate = tempDate.minusDays(-1);
			}
		}
	}

	private boolean isMatchTimeSlot(LocalTime startTime, LocalDate startDate,
			LocalTime endTime, LocalDate endDate, TaskInfo task) {
	    
        LocalDateTime myStart = LocalDateTime.of(startDate, startTime);
        LocalDateTime myEnd = LocalDateTime.of(endDate, endTime);
        
        LocalDateTime taskStart = LocalDateTime.of(getTaskStartDate(task),
                getTaskStartTime(task));
        LocalDateTime taskEnd = LocalDateTime.of(task.getEndDate(),
                task.getEndTime());

        return (beforeOrEqual(myStart, taskEnd) &&
                beforeOrEqual(taskStart, myEnd));
	}
    
    private boolean beforeOrEqual(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return (!dateTime1.isAfter(dateTime2));
    }

	
	public Result searchFreeDay(LocalDate startDate, LocalDate endDate) {
        LocalTime startTime = START_OF_DAY;
        LocalTime endTime = END_OF_DAY;
	    
	    return searchFreeDay(startTime, startDate, endTime, endDate) ;
	}

	private boolean isMatchDay(LocalDate startDate, LocalDate endDate,
			TaskInfo task) {
		return task.getEndDate().isAfter(LocalDate.now()) 
				&& (task.getEndDate().isAfter(startDate))
				&& (getTaskStartDate(task).isBefore(endDate));
	}

	private void sortTask() {
		Collections.sort(taskList, new Comparator<TaskInfo>() {
			@Override
			public int compare(TaskInfo task1, TaskInfo task2) {

				if (getTaskStartDate(task1).isBefore(getTaskStartDate(task2))) {
					return -1;
				} else if (getTaskStartDate(task1).isAfter(
						getTaskStartDate(task2))) {
					return 1;
				} else {
					return 0;
				}
			}
		});
	}

	private boolean isContainTimeSlot(TaskInfo task, LocalTime startTime,
			LocalTime endTime) {

		if (task.endTime == null){	  // no time task, not count
			return false;
		}

		if (getTaskStartDate(task).equals(task.getEndDate())) { // One day task
			if ((getTaskStartTime(task).isAfter(endTime))
					|| (task.getEndTime().isBefore(startTime))) {
				return false;
			}
		} else if (getTaskStartDate(task) != task.getEndDate()) { // Task that
			// last more
			// than 1
			// day
			if ((getTaskStartDate(task).minusDays(-1)
					.isEqual(task.getEndDate()))
					&& (getTaskStartTime(task).isAfter(endTime))
					&& (task.getEndTime().isBefore(startTime))) {
				return false;
			}
		}
		return true;
	}

	private ArrayList<TaskInfo> findTaskContainingTimeSlot(LocalTime startTime,
			LocalTime endTime) {
		ArrayList<TaskInfo> matchingTask = new ArrayList<TaskInfo>();
		for (TaskInfo task : taskList) {
		    TaskInfo currentTaskInfo = new TaskInfo(task);
		    if (dateWithoutTime(currentTaskInfo)) {
		        makeIntoWholeDayTask(currentTaskInfo);
		    }
		    
			if (isContainTimeSlot(currentTaskInfo, startTime, endTime)) {
				matchingTask.add(currentTaskInfo);

			}
		}
		return matchingTask;
	}
	
	private boolean dateWithoutTime(TaskInfo taskInfo) {
	    return (taskInfo.endTime == null && taskInfo.endDate != null);
	}
	
	private void makeIntoWholeDayTask(TaskInfo taskInfo) {
	    if (taskInfo.startDate == null) {
	        taskInfo.startDate = taskInfo.endDate;
	    }
        taskInfo.startTime = START_OF_DAY;
        taskInfo.endTime = END_OF_DAY;
	}
	

	private LocalDate lastContainingDate(TaskInfo task, LocalTime startTime,
			LocalTime endTime) {
		if (task.getEndTime().isBefore(startTime)) {
			return task.getEndDate().minusDays(1);
		} else {
			return task.getEndDate();
		}
	}

	private LocalDate firstContainingDate(TaskInfo task, LocalTime startTime,
			LocalTime endTime) {
		if (getTaskStartTime(task).isAfter(endTime)) {
			return getTaskStartDate(task).minusDays(-1);
		} else {
			return getTaskStartDate(task);
		}
	}

	private LocalTime getTaskStartTime(TaskInfo task) {
		if (task.getEndTime() == null) {
			return null;
		} else if (task.getStartTime() == null) {
			return task.getEndTime();
		} else {
			return task.getStartTime();
		}
	}

	private LocalDate getTaskStartDate(TaskInfo task) {
		if (task.getEndDate() == null) {
			return null;
		} else if (task.getStartDate() == null) {
			return task.getEndDate();
		} else {
			return task.getStartDate();
		}
	}
}