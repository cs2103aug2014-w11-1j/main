package manager.datamanager;

import io.FileInputOutput;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditManager extends AbstractManager {

    public EditManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result deleteTask(TaskInfo taskInfo, TaskId taskId) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}
