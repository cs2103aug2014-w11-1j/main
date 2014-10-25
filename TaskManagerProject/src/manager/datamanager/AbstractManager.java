package manager.datamanager;

import data.TaskData;

public abstract class AbstractManager {
    protected final TaskData taskData;
    
    public AbstractManager(TaskData taskData) {
        this.taskData = taskData;
    }
    
}
