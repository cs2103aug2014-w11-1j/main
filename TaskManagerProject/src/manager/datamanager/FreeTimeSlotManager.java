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
import data.TaskId;
import data.taskinfo.TaskInfo;

public class FreeTimeSlotManager extends AbstractManager {

	public ArrayList<TaskInfo> taskList;
	private int size;

	public FreeTimeSlotManager(TaskData taskData) {
		super(taskData);
		taskList = TaskListWithDate();
		sortTask();
		size = taskList.size();
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

	public Result searchFreeTimeSlot(LocalTime startTime, LocalDate startDate,
			LocalTime endTime, LocalDate EndDate) {
		LocalDate checkDate = null;
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>();
		ArrayList<TaskInfo> taskListContainingTimeSlot = findTaskContainingTimeSlot(
				startTime, endTime);
		LocalDate firstStartDate = firstContainingDate(taskListContainingTimeSlot.get(0),startTime,endTime);

		for (TaskInfo task : taskListContainingTimeSlot) {
			if (lastContainingDate(task, startTime, endTime).isAfter(
					LocalDate.now())) {
				if (checkDate == null) {
					checkDate = lastContainingDate(task, startTime, endTime);
				} else {
					if (firstContainingDate(task, startTime, endTime).isAfter(
							checkDate)) {
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(firstContainingDate(task,
								startTime, endTime))) {
							freeDays.add(tempDate);
							tempDate = tempDate.minusDays(-1);
						}
					}
					checkDate = lastContainingDate(task, startTime, endTime)
							.isBefore(checkDate) ? checkDate
							: lastContainingDate(task, startTime, endTime);
					System.out.println("task is " + task.name);
					System.out.println("LCD = "
							+ lastContainingDate(task, startTime, endTime));

				}
			}
		}

		return new FreeDayResult(Type.FREE_DAY, freeDays, firstStartDate, checkDate);
	}

	public Result searchFreeDay(LocalDate startDate, LocalDate endDate) {
		LocalDate checkDate = null;
		LocalDate firststartDate = getTaskStartDate(taskList.get(0));
		ArrayList<LocalDate> freeDays = new ArrayList<LocalDate>();

		for (TaskInfo task : taskList) {
			if (task.getEndDate().isAfter(LocalDate.now())) { // search start
																// from today,
																// for those
																// tasks end in
																// future
				if (checkDate == null) { // first task initialization
					checkDate = task.getEndDate();
				} else { // if new task's start date < checkDate, update
							// freeDay, else update checkDate
					if (getTaskStartDate(task).isAfter(checkDate)) {
						LocalDate tempDate = checkDate.minusDays(-1);
						while (tempDate.isBefore(getTaskStartDate(task))) {
							// System.out.println(getTaskStartDate(task));
							// System.out.println(checkDate);
							// System.out.println(tempDate);
							// System.out.println("=====");
							freeDays.add(tempDate);
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

		return new FreeDayResult(Type.FREE_DAY, freeDays, firststartDate,
				checkDate);
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
		System.out.println("Matching Task " + matchingTask.size());
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