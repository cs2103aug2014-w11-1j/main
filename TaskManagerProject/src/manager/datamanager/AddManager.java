package manager.datamanager;

import io.FileInputOutput;
import manager.Result;
import data.TaskData;
import data.taskinfo.TaskInfo;

public class AddManager extends AbstractManager {
    
    public AddManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result addTask(TaskInfo taskInfo) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
}
