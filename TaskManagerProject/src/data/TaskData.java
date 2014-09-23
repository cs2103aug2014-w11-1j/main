package data;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;
import data.taskinfo.Time;

/**
 * Program memory for the tasks in the program.
 * It is the job of TaskData to ensure that the absolute index of each task
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
    
    private int firstTask = NO_TASK;
    private int lastTask = NO_TASK;
    
    private int size = 0;
    
    public TaskData() {
        taskList = new ArrayList<>();
        freeSlotList = new LinkedList<>();
        nextTaskList = new ArrayList<>();
        previousTaskList = new ArrayList<>();
    }

    public int getFirst() {
        return firstTask;
    }
    
    public int getLast() {
        return lastTask;
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

    public String getTaskName (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getName();
        }
    }

    public boolean setTaskName (TaskId taskId, String name) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setName(name);
            return true;
        }
    }
    
    public Time getTaskStartTime (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStartTime();
        }
    }
    
    public boolean setTaskStartTime (TaskId taskId, Time time) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setStartTime(time);
            return true;
        }
    }
    
    public Time getTaskEndTime (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getEndTime();
        }
    }
    
    public boolean setTaskEndTime (TaskId taskId, Time time) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setEndTime(time);
            return true;
        }
    }
    
    public Date getTaskDate (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getDate();
        }
    }

    public boolean setTaskDate (TaskId taskId, Date date) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setDate(date);
            return true;
        }
    }
    
    public String getTaskDetails (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getDetails();
        }
    }

    public boolean setTaskDetails (TaskId taskId, String details) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setDetails(details);
            return true;
        }
    }
    
    public Priority getTaskPriority (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getPriority();
        }
    }


    public boolean setTaskPriority (TaskId taskId, Priority priority) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setPriority(priority);
            return true;
        }
    }
    
    public Status getTaskStatus (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getStatus();
        }
    }

    public boolean setTaskStatus (TaskId taskId, Status status) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            task.setStatus(status);
            return true;
        }
    }

    public Tag[] getTaskTags (TaskId taskId) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return null;
        } else {
            return task.getTags();
        }
    }
    
    public boolean addTag(TaskId taskId, Tag tag) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.addTag(tag);
        }
    }
    
    public boolean removeTag(TaskId taskId, Tag tag) {
        Task task = getTask(taskId);
        if (task == EMPTY_SLOT) {
            return false;
        } else {
            return task.removeTag(tag);
        }
    }
    
    public boolean clearTags(TaskId taskId) {
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
    
    
    private int maxTasks() {
        return TaskId.MAX_ID;
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
