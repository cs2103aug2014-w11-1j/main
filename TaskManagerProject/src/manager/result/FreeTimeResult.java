package manager.result;

import java.time.LocalDate;
import java.util.ArrayList;

import manager.datamanager.freetimemanager.Interval;

//@author A0119432L
public class FreeTimeResult implements Result {

	private ArrayList<Interval> freeTimeList;
	private LocalDate date;
	
	public FreeTimeResult(LocalDate date, ArrayList<Interval> list){
		this.date = date;
		this.freeTimeList = new ArrayList<>(list);
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public ArrayList<Interval> getFreeTimeList(){
		return freeTimeList;
	}
	

	@Override
	public Type getType() {
		return Type.FREE_TIME;
	}
}
