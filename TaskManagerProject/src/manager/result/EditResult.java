package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditResult implements Result {
	
	private Type type;
	private TaskInfo taskInfo;
	private TaskId taskId;

	public EditResult (Type type, TaskInfo taskInfo, TaskId taskId){
		this.type = type;
		this.taskInfo = taskInfo;
		this.taskId = taskId;
	}
	@Override
	public Type getType() {
		return type;
	}
	
	public TaskInfo getTaskInfo(){
		return taskInfo;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}

}