package data;

public class TaskId implements Comparable<TaskId> {
    public final int id;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskId other = (TaskId) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    public TaskId(int id) {
        this.id = id;
    }
    
    public static String toStringId(int indexId) {
        return String.valueOf(indexId); // placeholder
    }
    
    public static int toStringId(String stringId) {
        return Integer.parseInt(stringId); // placeholder
    }

    @Override
    public int compareTo(TaskId o) {
        return id - o.id;
    }
    
    
}
