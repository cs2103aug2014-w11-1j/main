package data.taskinfo;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW,
    NONE;
    
    public static Priority defaultPriority() {
        return NONE;
    }
}
