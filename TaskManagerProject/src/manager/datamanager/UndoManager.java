package manager.datamanager;

import java.util.LinkedList;

import manager.result.Result;
import manager.result.SimpleResult;
import manager.result.UndoResult;
import data.ITaskDataUndo;
import data.TaskData;
import data.TaskId;
import data.UndoSnapshot;

public class UndoManager extends AbstractManager {
    private static int UNDO_LIMIT = 100;
    private LinkedList<UndoSnapshot> undoHistory;
    private LinkedList<UndoSnapshot> redoHistory;
    private final ITaskDataUndo taskDataUndo;

    public UndoManager(TaskData taskData) {
        super(taskData);
        taskDataUndo = taskData;
        undoHistory = new LinkedList<>();
        redoHistory = new LinkedList<>();
    }

    public void clearHistory() {
        undoHistory.clear();
        redoHistory.clear();
    }
    
    public void updateUndoHistory() {
        UndoSnapshot undoSnapshot = taskDataUndo.retrieveUndoSnapshot();
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
        
        TaskId[] taskIds = undoSnapshot.getChangedList();
        undoSnapshot.applySnapshotChange();
        
        retrieveRedoSnapshot();
        
        return new UndoResult(Result.Type.UNDO_SUCCESS, taskIds);
    }

    
    public Result redo() {
        if (redoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.REDO_FAILURE);
        }

        UndoSnapshot redoSnapshot = redoHistory.pop();

        TaskId[] taskIds = redoSnapshot.getChangedList();        
        redoSnapshot.applySnapshotChange();
        
        retrieveUndoSnapshot();

        return new UndoResult(Result.Type.REDO_SUCCESS, taskIds);
    }
    
    
    
    private void clearRedoHistory() {
        redoHistory.clear();
    }


    private void retrieveUndoSnapshot() {
        UndoSnapshot undoSnapshot = taskDataUndo.retrieveUndoSnapshot();
        undoHistory.push(undoSnapshot);
    }
    
    private void retrieveRedoSnapshot() {
        UndoSnapshot redoSnapshot = taskDataUndo.retrieveUndoSnapshot();
        redoHistory.push(redoSnapshot);
    }
    
}
