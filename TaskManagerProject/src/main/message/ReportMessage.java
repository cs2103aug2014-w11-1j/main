package main.message;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

//@author A0119432L
public class ReportMessage implements Message{
	
	private final int countTodayTasks;
	private final int countTmrTasks;
    private final ArrayList<TaskInfo> urgentTasks;
    private final ArrayList<TaskInfo> nonUrgentTasks;
    private final ArrayList<TaskInfo> missedTasks;
	
	public ReportMessage(int countTodayTask, int countTmrTask,
	        ArrayList<TaskInfo> urgentTasks, ArrayList<TaskInfo> nonUrgentTasks,
	        ArrayList<TaskInfo> missedTasks) {
		this.countTodayTasks = countTodayTask;
		this.countTmrTasks = countTmrTask;
        this.urgentTasks = urgentTasks;
        this.nonUrgentTasks = nonUrgentTasks;
        this.missedTasks = missedTasks;
	}
	
	public int getCountTodayTask() {
		return countTodayTasks;
	}
	
	public int getCountTmrTask() {
		return countTmrTasks;
	}
    
    public ArrayList<TaskInfo> getUrgentTasks() {
        return urgentTasks;
    }
    
    public ArrayList<TaskInfo> getNonUrgentTasks() {
        return nonUrgentTasks;
    }

    public ArrayList<TaskInfo> getMissedTasks() {
        return missedTasks;
    }

	public Type getType() {
		return Type.REPORT;
	}
}
