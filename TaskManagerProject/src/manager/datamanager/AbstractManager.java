package manager.datamanager;

import data.TaskData;
import io.FileInputOutput;

public abstract class AbstractManager {
    protected final TaskData taskData;
    
    public AbstractManager(TaskData taskData) {
        this.taskData = taskData;
    }
    
}
