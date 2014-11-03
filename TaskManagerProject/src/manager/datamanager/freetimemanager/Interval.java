package manager.datamanager.freetimemanager;

import java.time.LocalTime;

//@author A0119432L
public class Interval {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private boolean isOccupied;
	
	public Interval(LocalTime startTime, LocalTime endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Interval(LocalTime startTime, LocalTime endTime, Boolean status){
		this.startTime = startTime;
		this.endTime = endTime;
		this.isOccupied = status;
	}

	public LocalTime getStartTime(){
		return startTime;
	}
	
	public LocalTime getEndTime(){
		return endTime;
	}
	
	public Boolean isOccupied(){
		return isOccupied;
	}
	
	public void setOccupied(Boolean status){
		isOccupied = status;
	}
}
