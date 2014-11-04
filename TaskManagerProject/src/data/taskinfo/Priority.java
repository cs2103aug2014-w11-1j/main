package data.taskinfo;

//@author A0065475X
public enum Priority {
    HIGH,
    MEDIUM,
    LOW,
    NONE;
    
    public static Priority defaultPriority() {
        return NONE;
    }
}
