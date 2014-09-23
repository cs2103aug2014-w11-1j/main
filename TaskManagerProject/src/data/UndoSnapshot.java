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
    
    public UndoSnapshot() {
        taskSnapshotList = new ArrayList<>();
    }
    
    public void addTaskSnapshot(TaskInfo taskInfo, TaskId taskId) {
        UndoTaskSnapshot taskSnapshot = new UndoTaskSnapshot(taskInfo, taskId);
        if (!taskSnapshotList.contains(taskSnapshot)) {
            taskSnapshotList.add(taskSnapshot);
        }
    }
    
    /**
     * Extracts all the individual task snapshots at once from the UndoSnapshot.<br>
     * This can only be done once.
     * Once extracted,this data structure is destroyed.<br>
     * @return a list of all the individual task snapshots - the previous state
     * for all the tasks that have been modified in the last action.
     */
    public ArrayList<UndoTaskSnapshot> retrieveTaskSnapshots() {
        ArrayList<UndoTaskSnapshot> tempList = taskSnapshotList;
        taskSnapshotList = null;
        return tempList;
    }
}
