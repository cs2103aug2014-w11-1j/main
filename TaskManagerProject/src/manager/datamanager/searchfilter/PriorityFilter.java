package manager.datamanager.searchfilter;

import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class PriorityFilter implements Filter {

    private Priority[] priorities;
    public PriorityFilter(Priority[] priorities) {
        this.priorities = priorities;
    }
    
    public Type getType() {
        return Type.FILTER_PRIORITY;
    }
    
    private boolean match(Priority priority, TaskInfo task) {
        return task.priority == priority;
    }
    
    public boolean filter(TaskInfo task) {
        for (Priority priority : priorities) {
            if (match(priority, task)) {
                return true;
            }
        }
        return false;
    }

}
