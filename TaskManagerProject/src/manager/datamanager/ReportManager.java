package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import manager.datamanager.SearchManager.TaskInfoId;
import manager.result.ReportResult;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class ReportManager extends AbstractManager{

	private ArrayList<TaskInfo> taskList;

	public ReportManager(TaskData taskData) {
		super(taskData);
	}

    public Result report(){
        taskList = updateTasks();
        ArrayList<TaskInfo> missedTasks = getMissedTasks();
        ArrayList<TaskInfo> todayTasks = getTaskByDate(LocalDate.now());
        ArrayList<TaskInfo> tmrTasks = getTaskByDate(LocalDate.now().minusDays(-1));
        ArrayList<TaskInfo> urgentTasks = getUrgentTasksFrom(todayTasks, tmrTasks);
        ArrayList<TaskInfo> nonUrgentTasks = getNonUrgentTasksFrom(todayTasks, tmrTasks);

        return new ReportResult(todayTasks.size(), tmrTasks.size(),
                urgentTasks, nonUrgentTasks, missedTasks);
    }
    
    private LocalDate getActualDate(TaskInfo task) {
        if (task.endTime == null) {
            return task.endDate;
        } else if (task.startTime == null) {
            return task.endDate;
        } else {
            if (task.endTime == LocalTime.MIDNIGHT) {
                return task.endDate.plusDays(-1);
            } else {
                return task.endDate;
            }
        }
    }
    
    private ArrayList<TaskInfo> split(TaskInfo task) {
        ArrayList<TaskInfo> result = new ArrayList<TaskInfo>();
        if (task.getStartTime() == null) {
            result.add(task);
        } else {
            LocalDate currentDate = task.getStartDate();
            LocalTime currentTime = task.getStartTime();
            while (!currentDate.equals(task.getEndDate())) {
                TaskInfo taskInfo = new TaskInfo(task);
                taskInfo.startTime = currentTime;
                taskInfo.startDate = currentDate;
                taskInfo.endTime = LocalTime.MIDNIGHT;
                taskInfo.endDate = currentDate.plusDays(1);
                result.add(taskInfo);
                currentTime = LocalTime.parse("00:00");
                currentDate = currentDate.plusDays(1);
            }
            
            if (!currentTime.equals(task.getEndTime())) {
                TaskInfo taskInfo = new TaskInfo(task);
                taskInfo.startTime = currentTime;
                taskInfo.startDate = currentDate;
                taskInfo.endTime = task.endTime;
                taskInfo.endDate = task.endDate;
                result.add(taskInfo);
            }
        }
        
        return result;
    }

	private ArrayList<TaskInfo> updateTasks() {
		ArrayList<TaskInfo> list = new ArrayList<>();
		TaskId taskId = taskData.getFirst();
		TaskInfo task;
		while (taskId.isValid()) {
			task = taskData.getTaskInfo(taskId);
			if (task.status == Status.UNDONE) {
			    list.addAll(split(task));
			}
			taskId = taskData.getNext(taskId);
		}

		return list;
	}

	private boolean isTaskOnDate(TaskInfo task, LocalDate date){
	    return date.equals(getActualDate(task));
	}

    private ArrayList<TaskInfo> getTaskByDate(LocalDate date){
        ArrayList<TaskInfo> list = new ArrayList<>();
        for (TaskInfo task : taskList) {
            if (isTaskOnDate(task, date)) {
                list.add(task);
            }
        }
        return list;
    }

    private ArrayList<TaskInfo> getMissedTasks() {
        ArrayList<TaskInfo> list = new ArrayList<>();
        for (TaskInfo task : taskList) {
            if (task.status != Status.DONE && 
                    isTaskBefore(task, LocalDate.now())) {
                list.add(task);
            }
        }
        return list;
    }

    private boolean isTaskBefore(TaskInfo task, LocalDate date) {
        if (task.getEndDate() == null) {
            return false;
        }

        return task.getEndDate().isBefore(date);
    }

    private ArrayList<TaskInfo> getUrgentTasksFrom(
            ArrayList<TaskInfo> todayList, ArrayList<TaskInfo> tomorrowList) {
        ArrayList<TaskInfo> list = new ArrayList<>();
        for (TaskInfo task : todayList) {
            if ((task.priority == Priority.HIGH)) {
                list.add(task);
            }
        }
        for (TaskInfo task : tomorrowList) {
            if ((task.priority == Priority.HIGH)) {
                list.add(task);
            }
        }
        return list;
    }

    private ArrayList<TaskInfo> getNonUrgentTasksFrom(
            ArrayList<TaskInfo> todayList, ArrayList<TaskInfo> tomorrowList) {
        
        ArrayList<TaskInfo> list = new ArrayList<>();
        for (TaskInfo task : todayList) {
            if ((task.priority != Priority.HIGH)) {
                list.add(task);
            }
        }
        for (TaskInfo task : tomorrowList) {
            if ((task.priority != Priority.HIGH)) {
                list.add(task);
            }
        }
        return list;
    }
}

