package manager.datamanager.freetimemanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import manager.datamanager.AbstractManager;
import manager.result.FreeTimeResult;
import manager.result.Result;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class FreeTimeSearchManager extends AbstractManager {
	
	private ArrayList<TaskInfo> taskList;
	private ArrayList<Interval> freeTimeList;
	private final LocalTime DAY_START_TIME = LocalTime.of(0, 0);
	private final LocalTime DAY_END_TIME = LocalTime.of(23, 59);
	
	public FreeTimeSearchManager(TaskData taskData) {
		super(taskData);
	}

    public Result searchFreeTimeSlot(LocalDate date){
        taskList = getTaskList();
        freeTimeList = generateTimeList();
        processTaskList(date);
        postProcessIntervalList();
        return new FreeTimeResult(date, freeTimeList);
    }
    

    private Interval getTaskTimeOnDate(TaskInfo taskInfo, LocalDate date){
		
		
		if (taskInfo.getEndDate() == null){ // floating task
			return null;
		} else if (taskInfo.getEndTime() == null){ // no time task
			return null;
		} else if (taskInfo.getEndDate().isBefore(date)){ // past task
			return null;
		}
		 
		if (taskInfo.getEndDate().isEqual(date)){ // end that day
			if (taskInfo.getStartDate() == null){
				// if deadline task, set it to be the time interval before the deadline
				return new Interval(taskInfo.getEndTime().minusMinutes(1), taskInfo.getEndTime());
			} else if (taskInfo.getStartDate().equals(date)){
				return new Interval(taskInfo.getStartTime(), taskInfo.getEndTime());
			} else if (taskInfo.getStartDate().isBefore(date)){
				return new Interval(DAY_START_TIME, taskInfo.getEndTime());
			}
		}
		if (taskInfo.getEndDate().isAfter(date)){
			if (taskInfo.getStartDate() == null){
				return null;
			} else if (taskInfo.getStartDate().isAfter(date)){
				return null;
			} else if (taskInfo.getStartDate().equals(date)){
				return new Interval(taskInfo.getStartTime(),DAY_END_TIME);
			} else if (taskInfo.getStartDate().isBefore(date)){
				return new Interval(DAY_START_TIME, DAY_END_TIME);
			}
		}
		return null;
	}
		
	private ArrayList<Interval> generateTimeList(){
		ArrayList<Interval> list = new ArrayList<>();
		for (int i = 0; i < 24; i++){
			list.add(new Interval(LocalTime.of(i, 0), LocalTime.of(i, 59),false));
		}
		return list;
	}

	private ArrayList<TaskInfo> getTaskList(){
		ArrayList<TaskInfo> list = new ArrayList<>();
		TaskId taskId = taskData.getFirst();
		TaskInfo task;
		while (taskId.isValid()){
			task = taskData.getTaskInfo(taskId);
			list.add(task);
			taskId = taskData.getNext(taskId);
		}
		return list;
	}
	
	private void processInterval(Interval interval){
		if (interval == null){
			return;
		}
		int startHour = interval.getStartTime().getHour();
		int endHour = interval.getEndTime().getHour();
		
		for (int i = startHour; i <= endHour; i++){
			freeTimeList.get(i).setOccupied(true);
		}
	}
	
	private void processTaskList(LocalDate date){
		
		for (TaskInfo taskInfo : taskList){
			Interval itvl = getTaskTimeOnDate(taskInfo, date);
			processInterval(itvl);
		}
	}
		
    private void postProcessIntervalList() {
        LinkedList<Interval> mergedIntervals = new LinkedList<>();
        
        for (Interval interval : freeTimeList) {
            if (!interval.isOccupied()) {
                Interval previous = mergedIntervals.isEmpty() ? null :
                    mergedIntervals.getLast();
                
                if (previous != null && interval.immediatelyAfter(previous)) {
                    mergedIntervals.removeLast();
                    mergedIntervals.addLast(previous.concatenate(interval));
                } else {
                    mergedIntervals.add(new Interval(interval));
                }
            }
        }
        
        freeTimeList = new ArrayList<>(mergedIntervals.size());
        for (Interval interval : mergedIntervals) {
            freeTimeList.add(interval);
        }
    }
}
