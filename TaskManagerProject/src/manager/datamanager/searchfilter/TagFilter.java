package manager.datamanager.searchfilter;

import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class TagFilter implements Filter{
    public Type getType() {
        return Type.FILTER_TAG;
    }
    
    Tag tag;
    
    public boolean filter(TaskInfo task) {
        for (Tag taskTag : task.tags) {
            if (taskTag.equals(tag)) {
                return true;
            }
        }
        return false;
    }
    
    public TagFilter(Tag tag) {
        this.tag = tag;
    }
}
