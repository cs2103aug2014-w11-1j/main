package manager.datamanager.searchfilter;

import manager.datamanager.searchfilter.Filter.Type;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A Filter that matches floating tasks.
 *
 */
public class FloatingFilter {

    public Type getType(){
        return Type.FILTER_FLOATING;
    }
    public boolean filter(TaskInfo task) {
        return task.endDate == null && task.startDate == null 
                && task.endTime == null && task.startTime == null;
    }
}
