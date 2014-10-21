package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class AddResult implements Result {
	
	private TaskInfo taskInfo;
	private TaskId taskId;

	public AddResult (TaskInfo taskInfo, TaskId taskId) {
		this.taskInfo = taskInfo;
		this.taskId = taskId;
	}
	@Override
	public Type getType() {
		return Type.ADD_SUCCESS;
	}
	
	public TaskInfo getTaskInfo(){
		return taskInfo;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}

}
