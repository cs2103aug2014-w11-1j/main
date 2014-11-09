package data;

import data.taskinfo.TaskInfo;

/**
 * Facade for TaskData - used by FileInputOutput to synchronise TaskData with
 * an external file.
 */
//@author A0065475X
public interface ITaskDataFileInputOutput {

    public abstract TaskId getFirst();

    public abstract TaskId getLast();

    public abstract int getSize();

    public abstract TaskId getNext(TaskId taskId);

    public abstract TaskId getPrevious(TaskId taskId);

    public abstract TaskInfo getTaskInfo(TaskId taskId);

    /**
     * Resets entire task list with a new list of tasks.
     * @param tasks List of tasks as retrieved from file.
     */
    public abstract void updateTaskList(TaskInfo[] tasks);

    /**
     * Call this whenever a save is successful so that TaskData knows it no
     * longer has any unsaved changes.
     */
    public abstract void saveSuccessful();

    public abstract boolean hasUnsavedChanges();

}