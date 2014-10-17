package manager.result;

import java.time.LocalDate;
import java.util.ArrayList;

public class FreeDayResult implements Result{

	private Type type;
	private ArrayList<LocalDate> freeDate;
	private LocalDate lastTaskEndDate;
	private LocalDate firstTaskDate;
	
	public FreeDayResult(Type type, ArrayList<LocalDate> freeDate, LocalDate startDate, LocalDate lastTaskEndDate){
		this.type = type;
		this.freeDate = freeDate;
		this.firstTaskDate = startDate;
		this.lastTaskEndDate = lastTaskEndDate;
	}
	
	@Override
	public Type getType() {
		return type;
		
	}
	
	public ArrayList<LocalDate> getFreeDate(){
		return freeDate;
	}
	
	public LocalDate getLastTaskEndDate(){
		return lastTaskEndDate;
	}
	
	public LocalDate getStartDate(){
		return firstTaskDate;
	}

}
