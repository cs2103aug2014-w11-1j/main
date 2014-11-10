package manager.datamanager.searchfilter;

import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A Filter that is used to search for tasks by tags.
 *
 * A task matches this filter if it has all of the tags.
 */
public class TagFilter implements Filter{
    public Type getType() {
        return Type.FILTER_TAG;
    }
    
    Tag[] tags;
    
    public boolean isMatching(TaskInfo task) 
    {
        if (task.tags == null) {
            return false;
        }
        
        for (Tag filterTag : tags) {
            boolean isExist = false;
            for (Tag taskTag : task.tags) {
                if (taskTag.equals(filterTag)) {
                    isExist = true;
                }
            }
            if (!isExist) {
                return false;
            }
        }
        return true;
    }
    
    public TagFilter(Tag[] tags) {
        this.tags = tags;
    }
}
