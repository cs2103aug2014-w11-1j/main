package manager.datamanager.freetimemanager;

import java.time.LocalTime;

public class Interval {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	
	public Interval(LocalTime startTime, LocalTime endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalTime getStartTime(){
		return startTime;
	}
	
	public LocalTime getEndtime(){
		return endTime;
	}
}
