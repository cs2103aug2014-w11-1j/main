package manager.datamanager.searchfilter;

import data.taskinfo.Status;
import data.taskinfo.TaskInfo;


public class StatusFilter implements Filter {

    private Status[] statuses;

    public StatusFilter(Status[] statuses) {
        this.statuses = statuses;
    }

    public static StatusFilter makeDefault() {
        return new StatusFilter(new Status[]{Status.UNDONE});
    }

    public Type getType() {
        return Type.FILTER_STATUS;
    }

    private boolean match(Status status, TaskInfo task) {
        return task.status == status;
    }

    public boolean filter(TaskInfo task) {
        for (Status status : statuses) {
            if (match(status, task)) {
                return true;
            }
        }
        return false;
    }

}
