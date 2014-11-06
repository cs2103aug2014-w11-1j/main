package manager.datamanager.searchfilter;

import data.taskinfo.TaskInfo;

//@author A0113011L
public interface Filter {
    public enum Type {
        FILTER_TAG,
        FILTER_DATETIME,
        FILTER_DATE,
        FILTER_PRIORITY,
        FILTER_STATUS,
        FILTER_KEYWORD,
        FILTER_SUGGESTION,
        FILTER_FLOATING
    }

    public Type getType();
    public boolean filter(TaskInfo task);
}
