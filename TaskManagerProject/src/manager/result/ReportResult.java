package manager.result;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

public class ReportResult implements Result{
	
	
	private ArrayList<TaskInfo> todayTask;
	private ArrayList<TaskInfo> tmrTask;
	private ArrayList<TaskInfo> urgentTask;
	
public ReportResult(ArrayList<TaskInfo> todayTask, ArrayList<TaskInfo> tmrTask
		, ArrayList<TaskInfo> urgentTask){
		
	}
	
	public Type getType(){
		return Type.REPORT;
	}
	
	public int countTodayTask(){
		return todayTask.size();
	}
	
	public int countTmrTask(){
		return tmrTask.size();
	}
	
	public ArrayList<TaskInfo> getUrgentTask(){
		return urgentTask;
	}

}
