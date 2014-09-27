package manager.result;

import data.taskinfo.TaskInfo;

public class SearchResult implements Result {

    private TaskInfo[] tasks;
    Type type;
    
    public SearchResult(Type type, TaskInfo[] tasks) {
        this.type = type;
        this.tasks = tasks;
    }
    
    @Override
    public Type getType() {
        return type;
    }

}
