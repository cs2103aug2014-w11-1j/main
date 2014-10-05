package data.taskinfo;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW,
    NONE,
    DEFAULT_NONE;
    
    public static Priority defaultPriority() {
        return DEFAULT_NONE;
    }
}
