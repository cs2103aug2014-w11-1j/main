package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.message.FreeTimeSearchMessage;
import manager.datamanager.freetimemanager.Interval;

//@author A0065475X
public class FreeTimeSearchFormatter {

    private final static String FORMAT_TIME = "Time: From %1$s - %2$s";
    private final static String FORMAT_DATE = "Free time slots for %1$s";
    private final static String DATETIME_FORMAT_TIME = "HH:mm (a)";
    private final static String DATETIME_FORMAT_DATE = "EEEE, d MMM Y";


    
    public String format(FreeTimeSearchMessage freeTimeMessage) {
        ArrayList<Interval> intervalList = freeTimeMessage.getFreeTimelist();
        LocalDate date = freeTimeMessage.getDate();
        
        return formatFreeTimes(intervalList, date);
    }
    
    private String formatFreeTimes(ArrayList<Interval> intervalList,
            LocalDate date) {
        

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(FORMAT_DATE, formatDate(date)));
        sb.append(System.lineSeparator());
        
        for (Interval interval : intervalList) {
            sb.append(formatInterval(interval));
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
    
    private String formatInterval(Interval interval) {
        LocalTime startTime = interval.getStartTime();
        LocalTime endTime = interval.getRoundedEndTime();

        return String.format(FORMAT_TIME, formatTime(startTime), formatTime(endTime));
    }
    

    private String formatTime(LocalTime time) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_TIME);
        return formatter.format(time);
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATE);
        return formatter.format(date);
    }

}
