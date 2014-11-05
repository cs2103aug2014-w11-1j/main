package manager.datamanager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import manager.result.Result;
import manager.result.SimpleResult;
import manager.result.UndoResult;
import data.ITaskDataUndo;
import data.TaskData;
import data.TaskId;
import data.UndoSnapshot;

//@author A0065475X
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

    public Result undo(int times) {
        assert times >= 1;
        if (undoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.UNDO_FAILURE);
        }
        if (undoHistory.size() < times) {
            times = undoHistory.size();
        }
        
        TaskId[] taskIds = multipleExecute(times, () -> executeUndo());
        
        return new UndoResult(Result.Type.UNDO_SUCCESS, taskIds, times);
    }

    public Result redo(int times) {
        assert times >= 1;
        if (redoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.UNDO_FAILURE);
        }
        if (redoHistory.size() < times) {
            times = redoHistory.size();
        }
        
        TaskId[] taskIds = multipleExecute(times, () -> executeRedo());
        
        return new UndoResult(Result.Type.REDO_SUCCESS, taskIds, times);
    }

    
    public Result undo() {
        if (undoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.UNDO_FAILURE);
        }
        
        TaskId[] taskIds = executeUndo();
        
        return new UndoResult(Result.Type.UNDO_SUCCESS, taskIds, 1);
    }

    
    public Result redo() {
        if (redoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.REDO_FAILURE);
        }

        TaskId[] taskIds = executeRedo();

        return new UndoResult(Result.Type.REDO_SUCCESS, taskIds, 1);
    }

    private TaskId[] multipleExecute(int times, Operation function) {
        
        HashSet<TaskId> taskIdSet = new HashSet<>();
        for (int i = 0; i < times; i++) {
            TaskId[] taskIds = function.execute();
            for (TaskId taskId : taskIds) {
                taskIdSet.add(taskId);
            }
        }
        
        TaskId[] taskIds = new TaskId[taskIdSet.size()];
        taskIds = taskIdSet.toArray(taskIds);
        return taskIds;
    }
    
    private TaskId[] executeUndo() {
        UndoSnapshot undoSnapshot = undoHistory.pop();
        
        TaskId[] taskIds = undoSnapshot.getChangedList();
        undoSnapshot.applySnapshotChange();
        
        retrieveRedoSnapshot();
        return taskIds;
    }

    
    private TaskId[] executeRedo() {
        UndoSnapshot redoSnapshot = redoHistory.pop();

        TaskId[] taskIds = redoSnapshot.getChangedList();        
        redoSnapshot.applySnapshotChange();
        
        retrieveUndoSnapshot();
        return taskIds;
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
    
    interface Operation {
        public abstract TaskId[] execute();
    }
    
}
