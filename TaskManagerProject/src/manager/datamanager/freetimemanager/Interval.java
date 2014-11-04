package manager.datamanager.freetimemanager;

import java.time.LocalTime;
import java.time.temporal.TemporalUnit;

//@author A0119432L
public class Interval {
	
	private final LocalTime startTime;
	private final LocalTime endTime;
	private boolean isOccupied;
	
	public Interval(Interval copy) {
	    this.startTime = copy.startTime;
	    this.endTime = copy.endTime;
	    this.isOccupied = copy.isOccupied;
	}
	
	public Interval(LocalTime startTime, LocalTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public Interval(LocalTime startTime, LocalTime endTime, boolean status) {
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
    
    public LocalTime getRoundedEndTime(){
        LocalTime plus30 = endTime.plusMinutes(30);
        return LocalTime.of(plus30.getHour(), 0);
    }
	
	public Boolean isOccupied(){
		return isOccupied;
	}
	
	public void setOccupied(Boolean status){
		isOccupied = status;
	}

    public boolean immediatelyAfter(Interval previous) {
        return previous.endTime.plusMinutes(1).equals(this.startTime);
    }

    public Interval concatenate(Interval next) {
        return new Interval(this.startTime, next.endTime);
    }

    @Override
    public String toString() {
        return "[" + startTime + "-" + endTime
                + "," + isOccupied + "]";
    }
}
