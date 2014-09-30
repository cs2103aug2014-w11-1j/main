package manager.datamanager.searchfilter;

import java.time.LocalDate;

import data.taskinfo.TaskInfo;

public class DateFilter implements Filter {

    private LocalDate startDate;
    private LocalDate endDate;
    
    public DateFilter(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    @Override
    public Type getType() {
        return Type.FILTER_DATE;
    }

    @Override
    public boolean filter(TaskInfo task) {
        return startDate.compareTo(task.endDate) <= 0 && 
                endDate.compareTo(task.endDate) <= 0; 
    }

}
