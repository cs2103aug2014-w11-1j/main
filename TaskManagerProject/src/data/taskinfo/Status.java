package data.taskinfo;

//@author A0065475X
public enum Status {
    UNDONE,
    DONE;
    
    public static Status defaultStatus() {
        return UNDONE;
    }
}
