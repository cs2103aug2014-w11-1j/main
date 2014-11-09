package data;

import java.time.LocalDate;
import java.time.LocalTime;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * Facade for TaskData - used by the managers for read/write operations to
 * TaskData. (e.g. SearchManager)
 */
//@author A0065475X
public interface ITaskData {

    public static final int NO_TASK = -1;

    public abstract TaskId getFirst();

    public abstract TaskId getLast();

    public abstract int getSize();

    public abstract TaskId getNext(TaskId taskId);

    public abstract TaskId getPrevious(TaskId taskId);

    public abstract boolean taskExists(TaskId taskId);

    public abstract String getTaskName(TaskId taskId);

    public abstract boolean setTaskName(TaskId taskId, String name);

    public abstract LocalTime getTaskStartTime(TaskId taskId);

    public abstract boolean setTaskStartTime(TaskId taskId, LocalTime time);

    public abstract LocalDate getTaskStartDate(TaskId taskId);

    public abstract boolean setTaskStartDate(TaskId taskId, LocalDate date);

    public abstract LocalTime getTaskEndTime(TaskId taskId);

    public abstract boolean setTaskEndTime(TaskId taskId, LocalTime time);

    public abstract LocalDate getTaskDate(TaskId taskId);

    public abstract boolean setTaskDate(TaskId taskId, LocalDate date);

    public abstract String getTaskDetails(TaskId taskId);

    public abstract boolean setTaskDetails(TaskId taskId, String details);

    public abstract Priority getTaskPriority(TaskId taskId);

    public abstract boolean setTaskPriority(TaskId taskId, Priority priority);

    public abstract Status getTaskStatus(TaskId taskId);

    public abstract boolean setTaskStatus(TaskId taskId, Status status);

    public abstract Tag[] getTaskTags(TaskId taskId);

    public abstract boolean addTag(TaskId taskId, Tag tag);

    public abstract boolean removeTag(TaskId taskId, Tag tag);

    public abstract boolean clearTags(TaskId taskId);

    public abstract TaskInfo getTaskInfo(TaskId taskId);

    public abstract boolean setTaskInfo(TaskId taskId, TaskInfo taskInfo);

    /**
     * @param taskInfo information about a task.
     * @return the generated taskId of the task.
     * Returns null if unable to add new task.
     */
    public abstract TaskId add(TaskInfo taskInfo);

    /**
     * @param taskId id of the task you wish to remove.
     * @return true iff the deletion is successful.
     * Deletion can be unsuccessful if task does not exist.
     */
    public abstract boolean remove(TaskId taskId);

    /**
     * Use to reverse all the changes in the last undo snapshot.
     */
    public abstract void reverseLastChange();

}