package manager.datamanager;

import data.ITaskData;
import data.TaskData;

//@author A0065475X
public abstract class AbstractManager {
    protected final ITaskData taskData;
    
    public AbstractManager(TaskData taskData) {
        this.taskData = taskData;
    }
    
}
