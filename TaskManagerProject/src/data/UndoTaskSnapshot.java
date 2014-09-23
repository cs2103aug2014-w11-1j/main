package data;

import data.taskinfo.TaskInfo;

/**
 * This class stores a snapshot of a task before a modification.<br>
 * It is used to undo changes.<br>
 * <br>
 * Note: if taskInfo = NO_TASK, it means the task did not exist before the change.
 * 
 * @author Oh
 */
public class UndoTaskSnapshot {

    public static TaskInfo NO_TASK = null;
    
    public final TaskInfo taskInfo;
    public final TaskId taskId;
    
    public UndoTaskSnapshot(TaskInfo taskInfo, TaskId taskId) {
        this.taskInfo = taskInfo;
        this.taskId = taskId;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UndoTaskSnapshot other = (UndoTaskSnapshot) obj;
        if (taskId == null) {
            if (other.taskId != null)
                return false;
        } else if (!taskId.equals(other.taskId))
            return false;
        return true;
    }
    
}
