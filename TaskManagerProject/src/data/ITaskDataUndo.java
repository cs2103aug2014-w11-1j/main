package data;

import data.taskinfo.TaskInfo;

/**
 * Facade for TaskData - used by the undoManager to retrieve undo snapshots.
 */
//@author A0065475X
public interface ITaskDataUndo {

    public static final int NO_TASK = -1;

    /**
     * Purpose is for the undo function to replace tasks to their original Id.
     * Throws an exception if this is not possible.
     * @param taskInfo
     * @param taskId
     */
    public abstract void addTaskWithSpecificId(TaskInfo taskInfo, TaskId taskId);

    /**
     * @param taskId id of the task you wish to remove.
     * @return true iff the deletion is successful.
     * Deletion can be unsuccessful if task does not exist.
     */
    public abstract boolean remove(TaskId taskId);

    /**
     * Retrieves the undo snapshot holding the previous state of all tasks
     * that were changed in the last action.<br>
     * The undo snapshot is cleared (deleted) when this method is called.
     * @return an UndoSnapshot holding the previous state of TaskData.
     */
    public abstract UndoSnapshot retrieveUndoSnapshot();

    /**
     * Used after an undo so that you don't undo an undo. :D
     */
    public abstract void discardUndoSnapshot();


}