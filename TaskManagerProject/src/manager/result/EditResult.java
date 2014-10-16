package manager.result;

import main.message.EditSuccessfulMessage;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditResult implements Result {
	
	private Type type;
	private TaskInfo taskInfo;
	private TaskId taskId;
	private EditSuccessfulMessage.Field[] changedFields;

	public EditResult (Type type, TaskInfo taskInfo, TaskId taskId, EditSuccessfulMessage.Field[] fields){
	    assert type == Type.EDIT_SUCCESS;
		this.type = type;
		this.taskInfo = taskInfo;
		this.taskId = taskId;
		this.changedFields = fields;
	}
	
	public EditResult (Type type, TaskInfo taskInfo, TaskId taskId, EditSuccessfulMessage.Field field){
		this.type = type;
		this.taskInfo = taskInfo;
		this.taskId = taskId;
		this.changedFields = new EditSuccessfulMessage.Field[1];
		changedFields[0] = field;
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

	public EditSuccessfulMessage.Field[] getChangedFields(){
		return changedFields;
	}

}
