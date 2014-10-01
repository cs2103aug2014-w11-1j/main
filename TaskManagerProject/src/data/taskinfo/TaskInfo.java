package data.taskinfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class TaskInfo {


    public String name;
    public Duration duration;
    public LocalTime endTime;
    public LocalDate endDate;

    public String details;
    public Tag[] tags;
    public Priority priority;
    public Status status;

    public int numberOfTimes;
    public Duration repeatInterval;

    public static final int REPEAT_INDEFINITELY = -1;

    public TaskInfo() {
        priority = Priority.NONE;
        status = Status.UNDONE;
    }

    /**
     * Copy constructor
     * @param taskInfo taskInfo to copy.
     */
    public TaskInfo(TaskInfo taskInfo) {
        this.name = taskInfo.name;
        this.duration = taskInfo.duration;
        this.endTime = taskInfo.endTime;
        this.endDate = taskInfo.endDate;
        this.details = taskInfo.details;

        if (taskInfo.tags != null) {
            this.tags = Arrays.copyOf(taskInfo.tags, taskInfo.tags.length);
        }

        this.priority = taskInfo.priority;
        this.status = taskInfo.status;
        this.numberOfTimes = taskInfo.numberOfTimes;
        this.repeatInterval = taskInfo.repeatInterval;
    }
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        result = prime * result
                + ((duration == null) ? 0 : duration.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + numberOfTimes;
        result = prime * result
                + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result
                + ((repeatInterval == null) ? 0 : repeatInterval.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        TaskInfo other = (TaskInfo) obj;
        
        String name1 = name;
        String name2 = other.name;
        if (name1 == null) {
            name1 = "";
        }
        if (name2 == null) {
            name2 = "";
        }
        if (!name1.equals(name2)) {
            return false;
        }
        
        String details1 = details;
        String details2 = other.details;
        if (details1 == null) {
            details1 = "";
        }
        if (details2 == null) {
            details2 = "";
        }
        if (!details1.equals(details2)) {
            return false;
        }
        
        
        if (duration == null) {
            if (other.duration != null) {
                return false;
            }
        } else if (!duration.equals(other.duration)) {
            return false;
        }
        
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        
        if (endTime == null) {
            if (other.endTime != null) {
                return false;
            }
        } else if (!endTime.equals(other.endTime)) {
            return false;
        }
        
        if (numberOfTimes != other.numberOfTimes) {
            return false;
        }
        
        if (priority != other.priority) {
            return false;
        }
        
        if (repeatInterval == null) {
            if (other.repeatInterval != null) {
                return false;
            }
        } else if (!repeatInterval.equals(other.repeatInterval)) {
            return false;
        }
        
        if (status != other.status) {
            return false;
        }

       
        if (tags != null && other.tags != null) {
            List<Tag> tagsList1 = Arrays.asList(tags);
            List<Tag> tagsList2 = Arrays.asList(other.tags);
    
            if (!tagsList1.containsAll(tagsList2)) {
                return false;
            }
            if (!tagsList2.containsAll(tagsList1)) {
                return false;
            }
            
        } else if (tags != null || other.tags != null) {
            return false;
        }
        
        return true;
    }

}
