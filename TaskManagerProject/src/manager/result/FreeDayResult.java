package manager.result;

import java.time.LocalDate;
import java.util.ArrayList;

public class FreeDayResult implements Result{

	private Type type;
	private ArrayList<LocalDate> freeDate;
	private LocalDate lastTaskEndDate;
	
	public FreeDayResult(Type type, ArrayList<LocalDate> freeDate, LocalDate lastTaskEndDate){
		this.type = type;
		this.freeDate = freeDate;
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

}
