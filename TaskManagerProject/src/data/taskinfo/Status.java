package data.taskinfo;

public enum Status {
    UNDONE,
    DONE;
    
    public static Status defaultStatus() {
        return UNDONE;
    }
}
