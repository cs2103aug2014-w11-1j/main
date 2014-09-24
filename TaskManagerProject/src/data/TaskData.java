package data;

import java.time.Duration;
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
 * 
 * @author Oh
 */
public class TaskData {
    
    public static final int NO_TASK = -1;
    private static final Task EMPTY_SLOT = null;
    
    private ArrayList<Task> taskList;
    private ArrayList<Integer> nextTaskList;
    private ArrayList<Integer> previousTaskList;
    private LinkedList<Integer> freeSlotList;
    
    private UndoSnapshot undoSnapshot;
    
    private int firstTask = NO_TASK;
    private int lastTask = NO_TASK;
    
    private int size = 0;
    
    public TaskData() {
        taskList = new ArrayList<>();
        freeSlotList = new LinkedList<>();
        nextTaskList = new ArrayList<>();
        previousTaskList = new ArrayList<>();
        
        undoSnapshot = new UndoSnapshot();
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
        return new TaskId(next(taskId.id));
    }
    
    public TaskId getPrevious(TaskId taskId) {
        return new TaskId(previous(taskId.id));
    }
    
    public boolean taskExists(TaskId taskId) {
        return getTask(taskId) != EMPTY_SLOT;
    }

    public String getTaskName(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getName();
        }
    }

    public boolean setTaskName(TaskId taskId, String name) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setName(name);
            return true;
        }
    }
    
    public Duration getTaskDuration(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getDuration();
        }
    }
    
    public boolean setTaskDuration(TaskId taskId, Duration duration) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setDuration(duration);
            return true;
        }
    }
    
    public LocalTime getTaskEndTime(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getEndTime();
        }
    }
    
    public boolean setTaskEndTime(TaskId taskId, LocalTime time) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setEndTime(time);
            return true;
        }
    }
    
    public LocalDate getTaskDate(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getEndDate();
        }
    }

    public boolean setTaskDate(TaskId taskId, LocalDate date) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setEndDate(date);
            return true;
        }
    }
    
    public String getTaskDetails(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getDetails();
        }
    }

    public boolean setTaskDetails(TaskId taskId, String details) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setDetails(details);
            return true;
        }
    }
    
    public Priority getTaskPriority(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getPriority();
        }
    }


    public boolean setTaskPriority(TaskId taskId, Priority priority) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setPriority(priority);
            return true;
        }
    }
    
    public Status getTaskStatus(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStatus();
        }
    }

    public boolean setTaskStatus(TaskId taskId, Status status) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setStatus(status);
            return true;
        }
    }

    public Tag[] getTaskTags(TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getTags();
        }
    }
    
    public boolean addTag(TaskId taskId, Tag tag) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.addTag(tag);
        }
    }
    
    public boolean removeTag(TaskId taskId, Tag tag) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.removeTag(tag);
        }
    }
    
    public boolean clearTags(TaskId taskId) {
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
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getTaskInfo();
        }
    }
    
    public boolean setTaskInfo(TaskId taskId, TaskInfo taskInfo) {
        addToSnapshot(taskId);
        
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setAllInfo(taskInfo);
            return true;
        }
    }

    /**
     * Resets entire task list with a new list of tasks.
     * @param tasks List of tasks as retrieved from file.
     */
    public void updateTaskList(Task[] tasks) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    /**
     * Purpose is for the undo function to replace tasks to their original Id.
     * Throws an exception if this is not possible.
     * @param taskInfo
     * @param taskId
     */
    public void addTaskWithSpecificId(TaskInfo taskInfo, TaskId taskId) {
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
        undoSnapshot = new UndoSnapshot();
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
