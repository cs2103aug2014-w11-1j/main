package manager.datamanager;

import data.ITaskData;
import data.TaskData;

public abstract class AbstractManager {
    protected final ITaskData taskData;
    
    public AbstractManager(TaskData taskData) {
        this.taskData = taskData;
    }
    
}
