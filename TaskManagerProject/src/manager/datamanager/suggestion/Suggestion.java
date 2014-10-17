package manager.datamanager.suggestion;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class Suggestion {
    private String[] keywords;
    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    
    public Suggestion(String[] keywords, TaskInfo[] tasks, TaskId[] taskIds) {
        this.keywords = keywords;
        this.tasks = tasks;
        this.taskIds = taskIds;
    }
}
