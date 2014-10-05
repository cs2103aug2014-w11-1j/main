package data.taskinfo;

public enum Status {
	DEFAULT_UNDONE,
    UNDONE,
    DONE;
    
    public static Status defaultStatus() {
        return DEFAULT_UNDONE;
    }
}
