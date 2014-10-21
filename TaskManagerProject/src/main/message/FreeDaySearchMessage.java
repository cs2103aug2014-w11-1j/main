package main.message;

import java.time.LocalDate;
import java.util.ArrayList;

public class FreeDaySearchMessage implements Message {


    private ArrayList<LocalDate> freeDates;
    private LocalDate lastBusyDate;
    private LocalDate firstBusyDate;
    
    public FreeDaySearchMessage(ArrayList<LocalDate> freeDates,
            LocalDate startDate, LocalDate lastTaskEndDate) {
        this.freeDates = freeDates;
        this.firstBusyDate = startDate;
        this.lastBusyDate = lastTaskEndDate;
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
}
