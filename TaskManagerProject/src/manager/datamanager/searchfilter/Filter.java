package manager.datamanager.searchfilter;

import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A Filter interface. A Filter is used to search for tasks.
 *
 */
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

    /**
     * Get the type of the filter.
     * @return The type of the filter.
     */
    public Type getType();
    
    /**
     * Check whether the task matches the filter.
     * @param task
     * @return
     */
    public boolean isMatching(TaskInfo task);
}
