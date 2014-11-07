package data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * Program memory for the tasks in the program.<br>
 * It is the job of TaskData to ensure that the absolute index of each task<br>
 * remains the same even after deletion / addition of tasks.
 */
//@author A0065475X
public class TaskData implements ITaskData, ITaskDataFileInputOutput, ITaskDataUndo {
    private static final String ERROR_NULL_TASKID = "Tried to query/edit TaskData with null task Id.";
    private static final String ERROR_NULL_TASKINFO = "Tried to edit TaskData with null taskInfo.";
    private static final String ERROR_NULL_TASK_ARRAY = "Tried to update TaskData with null task array.";
    
    public static final int NO_TASK = -1;
    private static final Task EMPTY_SLOT = null;
    
    private ArrayList<Task> taskList;
    private ArrayList<Integer> nextTaskList;
    private ArrayList<Integer> previousTaskList;
    private LinkedList<Integer> freeSlotList;
    
    private UndoSnapshot undoSnapshot;
    
    private int firstTask;
    private int lastTask;
    
    private int size = 0;
    
    private boolean hasUnsavedChanges;
    
    public TaskData() {
        initializeTaskData();
    }

    public TaskId getFirst() {
        return new TaskId(firstTask);
    }
    
    public TaskId getLast() {
        return new TaskId(lastTask);
    }
    
    public int getSize() {
        return size;
    }

    public TaskId getNext(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        return new TaskId(next(taskId.id));
    }
    
    public TaskId getPrevious(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        return new TaskId(previous(taskId.id));
    }
    
    public boolean taskExists(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        return getTask(taskId) != EMPTY_SLOT;
    }

    public String getTaskName(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getName();
        }
    }

    public boolean setTaskName(TaskId taskId, String name) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setName(name);
        }
    }
    
    public LocalTime getTaskStartTime(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStartTime();
        }
    }
    
    public boolean setTaskStartTime(TaskId taskId, LocalTime time) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setStartTime(time);
        }
    }
    
    public LocalDate getTaskStartDate(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStartDate();
        }
    }

    public boolean setTaskStartDate(TaskId taskId, LocalDate date) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setStartDate(date);
        }
    }
    
    public LocalTime getTaskEndTime(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getEndTime();
        }
    }
    
    public boolean setTaskEndTime(TaskId taskId, LocalTime time) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setEndTime(time);
        }
    }
    
    public LocalDate getTaskDate(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getEndDate();
        }
    }

    public boolean setTaskDate(TaskId taskId, LocalDate date) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setEndDate(date);
        }
    }
    
    public String getTaskDetails(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getDetails();
        }
    }

    public boolean setTaskDetails(TaskId taskId, String details) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setDetails(details);
        }
    }
    
    public Priority getTaskPriority(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getPriority();
        }
    }


    public boolean setTaskPriority(TaskId taskId, Priority priority) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setPriority(priority);
        }
    }
    
    public Status getTaskStatus(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStatus();
        }
    }

    public boolean setTaskStatus(TaskId taskId, Status status) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setStatus(status);
        }
    }

    public Tag[] getTaskTags(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getTags();
        }
    }
    
    public boolean addTag(TaskId taskId, Tag tag) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.addTag(tag);
        }
    }
    
    public boolean removeTag(TaskId taskId, Tag tag) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.removeTag(tag);
        }
    }
    
    public boolean clearTags(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.clearTags();
            return true;
        }
    }

    public TaskInfo getTaskInfo(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getTaskInfo();
        }
    }
    
    public boolean setTaskInfo(TaskId taskId, TaskInfo taskInfo) {
        assert taskId != null : ERROR_NULL_TASKID;
        assert taskInfo != null : ERROR_NULL_TASKINFO;
        
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.setAllInfo(taskInfo);
        }
    }

    /**
     * Resets entire task list with a new list of tasks.
     * @param tasks List of tasks as retrieved from file.
     */
    public void updateTaskList(TaskInfo[] tasks) {
        assert tasks != null : ERROR_NULL_TASK_ARRAY;
        
        initializeTaskData();
        for (TaskInfo taskInfo : tasks) {
            if (taskInfo.isValid()) {
                add(taskInfo);
                discardUndoSnapshot(); // calling this here actually reduces lag.
            }
        }
        discardUndoSnapshot();
    }
    
    /**
     * Purpose is for the undo function to replace tasks to their original Id.
     * Throws an exception if this is not possible.
     * @param taskInfo
     * @param taskId
     */
    public void addTaskWithSpecificId(TaskInfo taskInfo, TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        assert taskInfo != null : ERROR_NULL_TASKINFO;
        
        if (getTask(taskId) != EMPTY_SLOT) {
            throw new IllegalArgumentException("Unable to add task with id = " + taskId);
        }

        int insertIndex = taskId.id;

        if (!freeSlotList.remove(new Integer(insertIndex))) {
            throw new IllegalArgumentException("Unable to remove id from free slot list.");
        }

        Task task = new Task(taskInfo);
        insertTask(task, insertIndex);
        task.setId(insertIndex);
    }

    /**
     * @param taskInfo information about a task.
     * @return the generated taskId of the task.
     * Returns null if unable to add new task.
     */
    public TaskId add(TaskInfo taskInfo) {
        assert taskInfo != null : ERROR_NULL_TASKINFO;
        
        Task task = new Task(taskInfo);
        int id = insertTask(task);
        
        if (id != NO_TASK) {
            task.setId(id);
            return new TaskId(id);
        }
        else {
            return null;
        }
    }
    
    /**
     * @param taskId id of the task you wish to remove.
     * @return true iff the deletion is successful.
     * Deletion can be unsuccessful if task does not exist.
     */
    public boolean remove(TaskId taskId) {
        assert taskId != null : ERROR_NULL_TASKID;
        return deleteTask(taskId.id);
    }

    
    /**
     * Retrieves the undo snapshot holding the previous state of all tasks
     * that were changed in the last action.<br>
     * The undo snapshot is cleared (deleted) when this method is called.
     * @return an UndoSnapshot holding the previous state of TaskData.
     */
    public UndoSnapshot retrieveUndoSnapshot() {
        UndoSnapshot temp = undoSnapshot;
        discardUndoSnapshot();
        return temp;
    }
    
    /**
     * Used after an undo so that you don't undo an undo. :D
     */
    public void discardUndoSnapshot() {
        undoSnapshot = new UndoSnapshot(this);
    }
    
    /**
     * Use to reverse all the changes in the last undo snapshot.
     */
    public void reverseLastChange() {
        UndoSnapshot lastSnapshot = retrieveUndoSnapshot();
        lastSnapshot.applySnapshotChange();
        discardUndoSnapshot();
    }
    
    
    /**
     * Call this whenever a save is successful so that TaskData knows it no
     * longer has any unsaved changes.
     */
    public void saveSuccessful() {
        hasUnsavedChanges = false;
    }
    
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }
    
    private void initializeTaskData() {
        taskList = new ArrayList<>();
        freeSlotList = new LinkedList<>();
        nextTaskList = new ArrayList<>();
        previousTaskList = new ArrayList<>();
        
        firstTask = NO_TASK;
        lastTask = NO_TASK;
        
        undoSnapshot = new UndoSnapshot(this);
        size = 0;
        hasUnsavedChanges = false;
    }
        
    private int maxTasks() {
        return TaskId.MAX_ID;
    }
    
    /**
     * To save a snapshot of the task before a change is made.<br>
     * If a snapshot has already been saved, does nothing.<br>
     * Remember to call this BEFORE a task is modified!
     * @param taskId the id of the task you want to add to snapshot.
     */
    private void addToSnapshot(TaskId taskId) {
        hasUnsavedChanges = true;
        
        Task task = getTask(taskId);
        TaskInfo taskInfo = UndoTaskSnapshot.NO_TASK;
        if (task != null) {
            taskInfo = task.getTaskInfo();
        }
        
        undoSnapshot.addTaskSnapshot(taskInfo, taskId);
    }
    
    /**
     * how do I reference the javadoc of addToSnapshot(TaskId taskId)?
     * @param index the id of the task you want to add to snapshot.
     */
    private void addToSnapshot(int index) {
        addToSnapshot(new TaskId(index));
    }

    private Task getTask(TaskId taskId) {
        if (taskId.id < taskList.size()) {
            return taskList.get(taskId.id);
        }
        return EMPTY_SLOT;
    }
    
    private int insertTask(Task task) {
        
        if (taskList.size() < maxTasks()) {
            taskList.add(EMPTY_SLOT);
            nextTaskList.add(NO_TASK);
            previousTaskList.add(NO_TASK);
            
            int newIndex = taskList.size()-1;
            insertTask(task, newIndex);
            return newIndex;
            
        } else {
            
            if (freeSlotList.isEmpty()) {
                // No more slots to insert tasks. Task list full.
                return NO_TASK;
            }
            
            int newIndex = freeSlotList.removeFirst();
            insertTask(task, newIndex);
            return newIndex;
        }
    }

    private void insertTask(Task task, int newIndex) {
        if (taskList.get(newIndex) != EMPTY_SLOT) {
            throw new IllegalArgumentException("insertTask can only insert to empty slots!");
        }
        
        addToSnapshot(newIndex);
        taskList.set(newIndex, task);
        
        setNext(newIndex, NO_TASK);
        setPrevious(newIndex, lastTask);
        if (lastTask != NO_TASK)
            setNext(lastTask, newIndex);

        if (firstTask == NO_TASK) {
            firstTask = newIndex;
        }
        
        lastTask = newIndex;
        
        size++;
    }
    
    private boolean deleteTask(int index) {
        // Note: Uses lazy deletion to maintain index.
        
        if (taskList.size() <= index)
            return false;
        if (taskList.get(index) == EMPTY_SLOT)
            return false;

        addToSnapshot(index);
        
        if (index == firstTask) {
            firstTask = next(firstTask);
        }
        if (index == lastTask) {
            lastTask = previous(lastTask);
        }
        
        taskList.set(index, null);
        freeSlotList.addLast(index);
        if (previous(index) != NO_TASK) {
            setNext(previous(index), next(index));
        }
        if (next(index) != NO_TASK) {
            setPrevious(next(index), previous(index));
        }
        
        size--;
        return true;
    }
    
    private void setNext(int index, int next) {
        nextTaskList.set(index, next);
    }
    
    private void setPrevious(int index, int previous) {
        previousTaskList.set(index, previous);
    }

    private int next(int index) {
        return nextTaskList.get(index);
    }
    
    private int previous(int index) {
        return previousTaskList.get(index);
    }
    
    
}
