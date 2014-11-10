package main.modeinfo;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * The ModeInfo that is passed when the user is in the EditMode.
 */
public class EditModeInfo implements ModeInfo {

    private TaskId taskId;
    private TaskInfo taskInfo;
    
    /**
     * Constructor for the EditModeInfo.
     * @param taskInfo The task that is currently being edited.
     * @param taskId The TaskId of the task that is currently bein edited.
     */
    public EditModeInfo(TaskInfo taskInfo, TaskId taskId) {
        this.taskId = taskId;
        this.taskInfo = taskInfo;
    }
    
    /**
     * Return the type of the ModeInfo, which is Type.EDIT_MODE.
     */
    public Type getType() {
        return Type.EDIT_MODE;
    }
    
    /**
     * Get the TaskId of the task that is being edited.
     * @return The TaskId.
     */
    public TaskId getTaskId() {
        return taskId;
    }
    
    /**
     * Get the TaskInfo of the task that is being edited.
     * @return The TaskInfo.
     */
    public TaskInfo getTask() {
        return taskInfo;
    }

}
