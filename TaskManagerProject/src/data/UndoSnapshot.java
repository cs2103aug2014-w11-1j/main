package data;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

/**
 * A "Snapshot" container holding all the UndoTaskSnapshots after an action
 * has been executed. (i.e. all the changes to TaskData in the last action)<br>
 * It is used to reverse all modifications to TaskData at once.
 * 
 * @author Oh
 */
public class UndoSnapshot {
    private ArrayList<UndoTaskSnapshot> taskSnapshotList;
    private final TaskData taskData;
    
    public UndoSnapshot(TaskData taskData) {
        this.taskData = taskData;
        taskSnapshotList = new ArrayList<>();
    }
    
    public void addTaskSnapshot(TaskInfo taskInfo, TaskId taskId) {
        UndoTaskSnapshot taskSnapshot = new UndoTaskSnapshot(taskInfo, taskId);
        if (!taskSnapshotList.contains(taskSnapshot)) {
            taskSnapshotList.add(taskSnapshot);
        }
    }
    
    public boolean hasChanges() {
        return !taskSnapshotList.isEmpty();
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

    public void applySnapshotChange() {
        ArrayList<UndoTaskSnapshot> taskSnapshotList = retrieveTaskSnapshots();
        for (UndoTaskSnapshot undoTaskSnapshot : taskSnapshotList) {
            undoTaskChange(undoTaskSnapshot);
        }
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
