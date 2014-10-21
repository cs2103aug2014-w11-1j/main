package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DeleteResult implements Result{
	
	private TaskId taskId;
	private TaskInfo taskInfo;
	
	public DeleteResult(TaskId taskId, TaskInfo taskInfo){
		this.taskId = taskId;
		this.taskInfo = taskInfo;
	}

	@Override
	public Type getType() {
		return Type.DELETE_SUCCESS;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}

	public TaskInfo getTaskInfo(){
		return taskInfo;
	}
	
}
