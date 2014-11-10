package manager.datamanager.searchfilter;

import data.taskinfo.Status;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A filter that is used to search for a task with a specific status.
 *
 */
public class StatusFilter implements Filter {

    private Status[] statuses;

    public StatusFilter(Status[] statuses) {
        this.statuses = statuses;
    }

    /**
     * Create a default StatusFilter.
     * @return A StatusFilter for UNDONE status.
     */
    public static StatusFilter makeDefault() {
        return new StatusFilter(new Status[]{Status.UNDONE});
    }

    /**
     * Return the type of the filter, which is FILTER_STATUS.
     */
    public Type getType() {
        return Type.FILTER_STATUS;
    }

    /**
     * Check whether the filter matches the task.
     */
    private boolean match(Status status, TaskInfo task) {
        return task.status == status;
    }

    public boolean isMatching(TaskInfo task) {
        for (Status status : statuses) {
            if (match(status, task)) {
                return true;
            }
        }
        return false;
    }

}
