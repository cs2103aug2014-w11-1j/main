package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import manager.result.FreeDayResult;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class FreeDaySearchManager extends AbstractManager {

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
		updateTaskList();

		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>();
		ArrayList<TaskInfo> taskListContainingTimeSlot = findTaskContainingTimeSlot(
				startTime, endTime);
		LocalDate firstStartDate = firstContainingDate(taskListContainingTimeSlot.get(0),startTime,endTime);

		if (taskListContainingTimeSlot.size() == 0){
			return new FreeDayResult(null,null,null,startDate, endDate);
		}
		
		for (TaskInfo task : taskListContainingTimeSlot) {
			if (isMatchTimeSlot(startTime, startDate, endTime, endDate, task)) {
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

		clearMemory();
		return new FreeDayResult(freeDays, firstStartDate, checkDate, startDate, endDate);
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
		return lastContainingDate(task, startTime, endTime).isAfter(LocalDate.now())
				&& lastContainingDate(task, startTime, endTime).isAfter(startDate) 
				&& (getTaskStartDate(task).isBefore(endDate));
	}

	
	public Result searchFreeDay(LocalDate startDate, LocalDate endDate) {
		updateTaskList();

		LocalDate checkDate = null;
		LocalDate firststartDate = getTaskStartDate(taskList.get(0));
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>();

		if (taskList.size() == 0){
			return new FreeDayResult(null, null, null,startDate, endDate);
		}
		
		for (TaskInfo task : taskList) {
			if (isMatchDay(startDate, endDate, task)) {
				if (checkDate == null) { // first task initialization
					checkDate = task.getEndDate();
				} else { // if new task's start date < checkDate, update
					// freeDay, else update checkDate
					if (getTaskStartDate(task).isAfter(checkDate)) {
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(getTaskStartDate(task))) {
							if (tempDate.isAfter(startDate.minusDays(1))
									&& (tempDate.isBefore(endDate.minusDays(-1)))){
								freeDays.add(tempDate);
							}
							tempDate = tempDate.minusDays(-1);
						}

						checkDate = task.getEndDate().isBefore(checkDate) ? checkDate
								: task.getEndDate();
					} else if (getTaskStartDate(task).equals(checkDate)) {
						if (task.getEndDate().isAfter(checkDate)) {
							checkDate = task.getEndDate();
						}

					}

				}
			}
		}

		clearMemory();
		return new FreeDayResult(freeDays, firststartDate, checkDate, startDate, endDate);
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
			if (isContainTimeSlot(task, startTime, endTime)) {
				matchingTask.add(task);

			}
		}
		return matchingTask;
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