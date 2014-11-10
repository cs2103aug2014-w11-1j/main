package data;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

//@author A0065475X
/**
 * A "Snapshot" container holding all the UndoTaskSnapshots after an action
 * has been executed. (i.e. all the changes to TaskData in the last action)<br>
 * It is used to reverse all modifications to TaskData at once.
 * 
 */
public class UndoSnapshot {
    private ArrayList<UndoTaskSnapshot> taskSnapshotList;
    private final TaskData taskData;
    
    public UndoSnapshot(TaskData taskData) {
        this.taskData = taskData;
        taskSnapshotList = new ArrayList<>();
    }
    
    public void addTaskSnapshot(TaskInfo taskInfo, TaskId taskId) {
        assert taskId != null;
        assert taskSnapshotList != null;
        
        UndoTaskSnapshot taskSnapshot = new UndoTaskSnapshot(taskInfo, taskId);
        if (!taskSnapshotList.contains(taskSnapshot)) {
            taskSnapshotList.add(taskSnapshot);
        }
    }
    
    public boolean hasChanges() {
        return !taskSnapshotList.isEmpty();
    }
    
    /**
     * @return a list of the TaskIds that have been changed in this snapshot.
     * <br>
     * Assumes that the undo snapshot has not been executed
     * (applySnapshotChange) yet.
     */
    public TaskId[] getChangedList() {
        assert taskSnapshotList != null : "TaskSnapshotList has been already extracted";
        
        TaskId[] taskIds = new TaskId[taskSnapshotList.size()];
        int index = 0;
        for (UndoTaskSnapshot taskSnapshot : taskSnapshotList) {
            taskIds[index] = taskSnapshot.getTaskId();
            index++;
        }
        return taskIds;
    }

    /** 
     * Execute this method to apply all the reversions stored in this
     * UndoSnapshot onto TaskData.<br>
     * Calling this destroys this data structure.
     */
    public void applySnapshotChange() {
        ArrayList<UndoTaskSnapshot> taskSnapshotList = retrieveTaskSnapshots();
        for (UndoTaskSnapshot undoTaskSnapshot : taskSnapshotList) {
            undoTaskChange(undoTaskSnapshot);
        }
    }
    

    /**
     * Extracts all the individual task snapshots at once from the UndoSnapshot.<br>
     * This can only be done once.
     * Once extracted,this data structure is destroyed.<br>
     * @return a list of all the individual task snapshots - the previous state
     * for all the tasks that have been modified in the last action.
     */
    private ArrayList<UndoTaskSnapshot> retrieveTaskSnapshots() {
        ArrayList<UndoTaskSnapshot> tempList = taskSnapshotList;
        taskSnapshotList = null;
        return tempList;
    }
    

    private void undoTaskChange(UndoTaskSnapshot undoTaskSnapshot) {
        TaskId taskId = undoTaskSnapshot.getTaskId();
        TaskInfo taskInfo = undoTaskSnapshot.getTaskInfo();
        
        if (taskInfo == UndoTaskSnapshot.NO_TASK) {
            taskData.remove(taskId);
            
        } else {
            if (taskData.taskExists(taskId)) {
                taskData.setTaskInfo(taskId, taskInfo);
            } else {
                taskData.addTaskWithSpecificId(taskInfo, taskId);
            }
        }
    }
    
    
}
