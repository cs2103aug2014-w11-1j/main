package manager.datamanager;

import java.time.LocalDate;
import java.util.ArrayList;

import manager.result.ReportResult;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.TaskInfo;

public class ReportManager extends AbstractManager{
	
	private ArrayList<TaskInfo> taskList;
	
	public ReportManager(TaskData taskData) {
		super(taskData);
	}

	private ArrayList<TaskInfo> updateTask(){
		ArrayList<TaskInfo> list = new ArrayList<>();
		TaskId taskId = taskData.getFirst();
		TaskInfo task;
		while (taskId.isValid()){
			task = taskData.getTaskInfo(taskId);
			if (task.status == Status.UNDONE){
			list.add(task);
			}
			taskId = taskData.getNext(taskId);
		}
		
		return list;
	}
	
	private boolean isTaskOnDate(TaskInfo task, LocalDate date){
		if ((task.getStartDate() == null) && (task.getEndDate().equals(date))){
			return true;
		}
		if ((!task.getStartDate().isAfter(date)) && (!task.getEndDate().isBefore(date))){
			return true;
		}
		return false;
	}
	
	private ArrayList<TaskInfo> getTaskByDate(LocalDate date){
		ArrayList<TaskInfo> list = new ArrayList<>();
		for (TaskInfo task : taskList){
			if (isTaskOnDate(task, date)){
				list.add(task);
			}
		}
		return list;
	}
	
	private ArrayList<TaskInfo> getUrgentTask(){
		ArrayList<TaskInfo> list = new ArrayList<>();
		for (TaskInfo task : taskList){
			if ((task.priority == Priority.HIGH) && 
			((isTaskOnDate(task, LocalDate.now())) || (isTaskOnDate(task, LocalDate.now().minusDays(-1))))){
				list.add(task);
			}
		}
		return list;
	}
	
	public Result report(){
		updateTask();
		ArrayList<TaskInfo> todayTask = getTaskByDate(LocalDate.now());
		ArrayList<TaskInfo> tmrTask = getTaskByDate(LocalDate.now().minusDays(-1));
		ArrayList<TaskInfo> urgentTask = getUrgentTask();
		
		return new ReportResult(todayTask, tmrTask, urgentTask);
	}
}

