package manager.datamanager.searchfilter;

import data.taskinfo.TaskInfo;

public interface Filter {
    public enum Type {
        FILTER_TAG,
        FILTER_TIME,
        FILTER_PRIORITY
    }
    
    public Type getType();
    public boolean filter(TaskInfo task);
}
