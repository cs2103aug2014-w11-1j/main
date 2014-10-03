package manager.datamanager;

import java.util.ArrayList;
import java.util.LinkedList;

import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.UndoSnapshot;
import data.UndoTaskSnapshot;
import data.taskinfo.TaskInfo;

public class UndoManager extends AbstractManager {
    private static int UNDO_LIMIT = 100;
    private LinkedList<UndoSnapshot> undoHistory;
    private LinkedList<UndoSnapshot> redoHistory;

    public UndoManager(TaskData taskData) {
        super(taskData);
        undoHistory = new LinkedList<>();
        redoHistory = new LinkedList<>();
    }

    public void clearUndoHistory() {
        undoHistory.clear();
    }
    
    public void updateUndoHistory() {
        UndoSnapshot undoSnapshot = taskData.retrieveUndoSnapshot();
        if (undoSnapshot.hasChanges()) {
            clearRedoHistory();
            
            undoHistory.push(undoSnapshot);
            if (undoHistory.size() > UNDO_LIMIT) {
                undoHistory.removeLast();
            }
        }
    }
    
    public Result undo() {
        if (undoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.UNDO_FAILURE);
        }
        
        UndoSnapshot undoSnapshot = undoHistory.pop();
        
        applySnapshotChange(undoSnapshot);
        
        retrieveRedoSnapshot();
        
        return new SimpleResult(Result.Type.UNDO_SUCCESS);
    }

    
    public Result redo() {
        if (redoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.REDO_FAILURE);
        }
        
        UndoSnapshot redoSnapshot = redoHistory.pop();
        
        applySnapshotChange(redoSnapshot);
        
        retrieveUndoSnapshot();
        
        return new SimpleResult(Result.Type.REDO_SUCCESS);
    }
    

    private void applySnapshotChange(UndoSnapshot undoSnapshot) {
        ArrayList<UndoTaskSnapshot> taskSnapshotList = undoSnapshot.retrieveTaskSnapshots();
        for (UndoTaskSnapshot undoTaskSnapshot : taskSnapshotList) {
            undoTaskChange(undoTaskSnapshot);
        }
    }
    
    
    private void clearRedoHistory() {
        redoHistory.clear();
    }


    private void retrieveUndoSnapshot() {
        UndoSnapshot undoSnapshot = taskData.retrieveUndoSnapshot();
        undoHistory.push(undoSnapshot);
    }
    
    private void retrieveRedoSnapshot() {
        UndoSnapshot redoSnapshot = taskData.retrieveUndoSnapshot();
        redoHistory.push(redoSnapshot);
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
