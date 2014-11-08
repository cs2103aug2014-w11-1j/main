package manager.result;

import java.util.ArrayList;

import data.taskinfo.TaskInfo;

//@author A0119432L
public class ReportResult implements Result{

    private final int nTodayTasks;
    private final int nTomorrowTasks;
    private final ArrayList<TaskInfo> urgentTasks;
    private final ArrayList<TaskInfo> nonUrgentTasks;
    private final ArrayList<TaskInfo> missedTasks;
	
    public ReportResult(int nTodayTasks, int nTomorrowTasks,
            ArrayList<TaskInfo> urgentTasks, ArrayList<TaskInfo> nonUrgentTasks,
            ArrayList<TaskInfo> missedTasks) {
        this.nTodayTasks = nTodayTasks;
        this.nTomorrowTasks = nTomorrowTasks;
        this.urgentTasks = urgentTasks;
        this.nonUrgentTasks = nonUrgentTasks;
        this.missedTasks = missedTasks;
    }

    public Type getType() {
		return Type.REPORT;
	}
	
	public int countTodayTasks() {
	    return nTodayTasks;
	}
	
	public int countTmrTasks() {
	    return nTomorrowTasks;
	}
    
    public ArrayList<TaskInfo> getUrgentTasks() {
        return urgentTasks;
    }
    
    public ArrayList<TaskInfo> getNonUrgentTasks() {
        return nonUrgentTasks;
    }
    
    public ArrayList<TaskInfo> getMissedTasks() {
        return missedTasks;
    }

}
