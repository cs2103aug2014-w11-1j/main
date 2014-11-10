package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import manager.result.FreeDayResult;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;


//@author A0119432L
/**
 * @author BRUCE
 *
 */
public class FreeDaySearchManager extends AbstractManager {

	private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);
    private static final LocalTime START_OF_DAY = LocalTime.of(0, 0);
    
    private ArrayList<TaskInfo> taskList;
	private int size;

	/** 
	 * Constructor
	 * @param taskData
	 */
	public FreeDaySearchManager(TaskData taskData) {
		super(taskData);
	}

	/**
	 * Get the taskInfos of tasks that consist of date, which are not floating
	 * @return ArrayList of taskInfos
	 */
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

	/**
	 * this is to initialize and update the task list for the sake of searching
	 */
	private void updateTaskList() {
		taskList = TaskListWithDate();
		sortTask();
		size = taskList.size();
	}

	private void clearMemory() {
		taskList.clear();
		taskList = null;   
	}
	
	
	/**
	 * Search for those days that are free from start time 
	 * to end time between start date and end date 
	 * @param startTime start time of spare interval
	 * @param startDate start date of searching period
	 * @param endTime end time of spare intervaal
	 * @param endDate end date of searching period
	 * @return a result containing those free days
	 */
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
					updateFreeDay(startTime, startDate, endTime, endDate,
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

	/**
	 * to check two time date pair whether one come before the other 
	 * @param startTime  time of first task
	 * @param startDate  date of first task
	 * @param endTime  time of second task
	 * @param endDate  date of second task
	 * @return a boolean representing whether first task appears before second task
	 */
	private boolean startBeforeEnd(LocalTime startTime, LocalDate startDate,
            LocalTime endTime, LocalDate endDate) {
        return !endDate.isBefore(startDate) && !endTime.isBefore(startTime);
    }

	
	/**
	 * This function is to update the free day list by adding new free days 
	 * @param startTime
	 * @param startDate
	 * @param endTime
	 * @param endDate
	 * @param checkDate
	 * @param freeDays
	 * @param task
	 */
    private void updateFreeDay(LocalTime startTime, LocalDate startDate,
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

    /**
     * This is to check whether a task happens in the specific time slot
     * @param startTime start time of spare interval
     * @param startDate start date of searching period
     * @param endTime end time of spare interval
     * @param endDate end date of searching period
     * @param task task to check
     * @return a boolean whether task happens in the specific time slot
     */
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

	/**
	 * This is to find the list of tasks that appears in a specific time slot
	 * @param startTime start time of time slot
	 * @param endTime end time of time slot
	 * @return list of tasks
	 */
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