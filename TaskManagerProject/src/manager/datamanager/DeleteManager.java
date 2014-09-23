package manager.datamanager;

import io.FileInputOutput;
import manager.result.Result;
import data.TaskData;
import data.TaskId;

public class DeleteManager extends AbstractManager {

    public DeleteManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result deleteTask(TaskId taskId) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
