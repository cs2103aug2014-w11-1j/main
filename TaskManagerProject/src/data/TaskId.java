package data;

public class TaskId implements Comparable<TaskId> {
    
    /*
     * Condition: TRANSLATE_PRIME must be coprime to MAX_ID.
     * We can ensure this by simply meeting the below two conditions:
     * 1) TRANSLATE_PRIME > MAX_ID,
     * 2) TRANSLATE_PRIME is prime.
     */
    private static final int TRANSLATE_PRIME = 363767;
    /**
     * TRANSLATE_REVERSE_PRIME should be computed using the Euclidean Algorithm.
     */
    private static final int TRANSLATE_REVERSE_PRIME = 41735;
    private static final int TRANSLATE_SHIFT = 4789;
    public static final int MAX_ID = 46656;
    
    
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
    
    public static int numberTranslateForward(int index) {
        long longIndex = index;
        longIndex += TRANSLATE_SHIFT;
        longIndex *= TRANSLATE_PRIME;
       
        longIndex %= MAX_ID;
        if (longIndex < 0) {
            longIndex += MAX_ID;
        }
        return (int)longIndex;
    }

    public static int numberTranslateInverse(int index) {
        long longIndex = index;
        longIndex *= TRANSLATE_REVERSE_PRIME;
        longIndex -= TRANSLATE_SHIFT;
        
        longIndex %= MAX_ID;
        if (longIndex < 0) {
            longIndex += MAX_ID;
        }
        return (int)longIndex;
    }

    @Override
    public int compareTo(TaskId o) {
        return id - o.id;
    }
    
    
}
