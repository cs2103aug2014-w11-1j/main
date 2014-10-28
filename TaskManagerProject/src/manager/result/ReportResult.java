package manager.result;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

public class ReportResult implements Result{
    
	private final ArrayList<TaskInfo> todayTask;
	private final ArrayList<TaskInfo> tmrTask;
	private final ArrayList<TaskInfo> urgentTask;
	
	public ReportResult(ArrayList<TaskInfo> todayTask,
            ArrayList<TaskInfo> tmrTask, ArrayList<TaskInfo> urgentTask) {
        this.todayTask = todayTask;
        this.tmrTask = tmrTask;
        this.urgentTask = urgentTask;
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
