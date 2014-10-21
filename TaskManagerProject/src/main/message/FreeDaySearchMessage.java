package main.message;

import java.time.LocalDate;
import java.util.ArrayList;

public class FreeDaySearchMessage implements Message {


    private ArrayList<LocalDate> freeDate;
    private LocalDate lastBusyDate;
    private LocalDate firstBusyDate;
    
    public FreeDaySearchMessage(ArrayList<LocalDate> freeDate,
            LocalDate startDate, LocalDate lastTaskEndDate) {
        this.freeDate = freeDate;
        this.firstBusyDate = startDate;
        this.lastBusyDate = lastTaskEndDate;
    }
    
    @Override
    public Type getType() {
        return Type.FREE_DAY_SEARCH_SUCCESSFUL;
        
    }
    
    public ArrayList<LocalDate> getFreeDate(){
        return freeDate;
    }
    
    public LocalDate getLastBusyDate(){
        return lastBusyDate;
    }
    
    public LocalDate getFirstBusyDate(){
        return firstBusyDate;
    }
}
