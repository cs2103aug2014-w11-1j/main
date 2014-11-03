package main.message;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

//@author A0119432L
public class ReportMessage implements Message{
	
	private int countTodayTask;
	private int countTmrTask;
	private ArrayList<TaskInfo> urgentTask;
	
	public ReportMessage(int countTodayTask, int countTmrTask, ArrayList<TaskInfo> urgentTask){
		this.countTodayTask = countTodayTask;
		this.countTmrTask = countTmrTask;
		this.urgentTask = urgentTask;
	}
	
	public int getCountTodayTask(){
		return countTodayTask;
	}
	
	public int getCountTmrTask(){
		return countTmrTask;
	}
	
	public ArrayList<TaskInfo> getUrgentTask(){
		return urgentTask;
	}

	public Type getType() {
		return Type.REPORT;
	}
}
