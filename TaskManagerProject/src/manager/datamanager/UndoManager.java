package manager.datamanager;

import io.FileInputOutput;
import data.TaskData;
import manager.Result;

public class UndoManager extends AbstractManager {

    public UndoManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result undo() {
        throw new UnsupportedOperationException("Not Implemented Yet");    
    }
}
