package manager.datamanager;

import io.FileInputOutput;

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

    public UndoManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
        undoHistory = new LinkedList<>();
    }

    public void clearUndoHistory() {
        undoHistory.clear();
    }
    
    public void retrieveUndoSnapshot() {
        UndoSnapshot undoSnapshot = taskData.retrieveUndoSnapshot();
        if (undoSnapshot.hasChanges()) {
            undoHistory.push(undoSnapshot);
            if (undoHistory.size() > UNDO_LIMIT)
                undoHistory.removeLast();
        }
    }
    
    public Result undo() {
        if (undoHistory.isEmpty()) {
            return new SimpleResult(Result.Type.UNDO_FAILURE);
        }
        
        readFromFile();
        
        UndoSnapshot undoSnapshot = undoHistory.pop();
        
        ArrayList<UndoTaskSnapshot> taskSnapshotList = undoSnapshot.retrieveTaskSnapshots();
        for (UndoTaskSnapshot undoTaskSnapshot : taskSnapshotList) {
            undoTaskChange(undoTaskSnapshot);
        }
        
        taskData.discardUndoSnapshot();
        
        writeToFile();
        return new SimpleResult(Result.Type.UNDO_SUCCESS);
    }

    private void undoTaskChange(UndoTaskSnapshot undoTaskSnapshot) {
        TaskId taskId = undoTaskSnapshot.taskId;
        TaskInfo taskInfo = undoTaskSnapshot.taskInfo;
        
        if (taskInfo == UndoTaskSnapshot.NO_TASK) {
            taskData.remove(undoTaskSnapshot.taskId);
            
        } else {
            if (taskData.taskExists(taskId)) {
                taskData.setTaskInfo(taskId, taskInfo);
            } else {
                taskData.addTaskWithSpecificId(taskInfo, taskId);
            }
        }
    }
    
}
