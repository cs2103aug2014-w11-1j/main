package main.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import data.TaskId;

/**
 * A set of task IDs that is used for a command operation.
 * 
 * @author Oh
 */
public class TargetIdSet implements Iterable<TaskId>{
    private ArrayList<TaskId> idList;
    
    public TargetIdSet() {
        idList = new ArrayList<>();
    }

    @Override
    public Iterator<TaskId> iterator() {
        return idList.iterator();
    }
    
    public boolean contains(TaskId taskId) {
        return idList.contains(taskId);
    }
    
    /**
     * @param taskId
     * @return true iff there was a change.
     */
    public boolean add(TaskId taskId) {
        return idList.add(taskId);
    }
}
