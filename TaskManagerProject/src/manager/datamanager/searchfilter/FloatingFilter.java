package manager.datamanager.searchfilter;

import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A Filter that matches floating tasks.
 *
 */
public class FloatingFilter implements Filter {

    @Override
    public Type getType(){
        return Type.FILTER_FLOATING;
    }

    @Override
    public boolean isMatching(TaskInfo task) {
        return task.endDate == null && task.startDate == null
                && task.endTime == null && task.startTime == null;
    }
}
