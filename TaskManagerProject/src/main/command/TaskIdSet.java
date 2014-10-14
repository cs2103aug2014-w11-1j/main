package main.command;

import java.util.ArrayList;
import java.util.Iterator;

import data.TaskId;

/**
 * A set of task IDs that is used for a command operation.
 * 
 * @author Oh
 */
public class TaskIdSet implements Iterable<TaskId>{
    private ArrayList<TaskId> idList;
    
    public TaskIdSet() {
        idList = new ArrayList<>();
    }

    /**
     * @return a non-modifiable iterator for the TargetIdSet.
     */
    @Override
    public Iterator<TaskId> iterator() {
        final Iterator<TaskId> iterator = idList.iterator();
        return new Iterator<TaskId>() {
            public boolean hasNext(){
                return iterator.hasNext();
            }
            public TaskId next(){
                return iterator.next();
            }
            public void remove(){
                throw new UnsupportedOperationException("You can't remove from this iterator!");
            }
        };
    }
    
    public boolean contains(TaskId taskId) {
        return idList.contains(taskId);
    }
    
    /**
     * Note: will not add repeat values.
     * @param taskId
     * @return true iff there was a change.
     */
    public boolean add(TaskId taskId) {
        if (!idList.contains(taskId)) {
            return idList.add(taskId);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return idList.toString();
    }
    
    public String numericIdString() {
        StringBuilder sb = new StringBuilder();
        
        String comma = "";
        for (TaskId id : idList) {
            sb.append(comma);
            sb.append(TaskId.toIntId(id.toString()));
            comma = ",";
        }
        return sb.toString();
    }
}
