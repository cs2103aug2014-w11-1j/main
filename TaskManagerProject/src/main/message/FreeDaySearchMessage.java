package main.message;

import java.time.LocalDate;
import java.util.ArrayList;

public class FreeDaySearchMessage implements Message {


    private ArrayList<LocalDate> freeDates;
    private LocalDate lastBusyDate;
    private LocalDate firstBusyDate;
    private LocalDate searchStartDate;
    private LocalDate searchEndDate;
    
    public FreeDaySearchMessage(ArrayList<LocalDate> freeDates,
            LocalDate startDate, LocalDate lastTaskEndDate,
            LocalDate searchStartDate, LocalDate searchEndDate) {
        this.freeDates = freeDates;
        this.firstBusyDate = startDate;
        this.lastBusyDate = lastTaskEndDate;
        this.searchStartDate = searchStartDate;
        this.searchEndDate = searchEndDate;
    }
    
    @Override
    public Type getType() {
        return Type.FREE_DAY_SEARCH_SUCCESSFUL;
    }
    
    public ArrayList<LocalDate> getFreeDateList(){
        return freeDates;
    }
    
    public LocalDate getLastBusyDate(){
        return lastBusyDate;
    }
    
    public LocalDate getFirstBusyDate(){
        return firstBusyDate;
    }
    
    public LocalDate getSearchStartDate(){
    	return searchStartDate;
    }
    
    public LocalDate getSearchEndDate(){
    	return searchEndDate;
    }
}
