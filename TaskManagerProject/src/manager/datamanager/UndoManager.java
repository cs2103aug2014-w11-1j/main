package manager.datamanager;

import java.util.LinkedList;

import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.UndoSnapshot;

public class UndoManager extends AbstractManager {
    private static int UNDO_LIMIT = 100;
    private LinkedList<UndoSnapshot> undoHistory;
    private LinkedList<UndoSnapshot> redoHistory;

    public UndoManager(TaskData taskData) {
        super(taskData);
        undoHistory = new LinkedList<>();
        redoHistory = new LinkedList<>();
    }

    public void clearHistory() {
        undoHistory.clear();
        redoHistory.clear();
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
        
        undoSnapshot.applySnapshotChange();
        
        retrieveRedoSnapshot();
        
        return new SimpleResult(Result.Type.UNDO_SUCCESS);
    }

    
    public Result redo() {
        if (redoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.REDO_FAILURE);
        }
        
        UndoSnapshot redoSnapshot = redoHistory.pop();
        
        redoSnapshot.applySnapshotChange();
        
        retrieveUndoSnapshot();
        
        return new SimpleResult(Result.Type.REDO_SUCCESS);
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
    
}
