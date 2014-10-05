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

    private TaskInfo() {
    }
    
    /**
     * Default constructor for TaskInfo.<br>
     * Always use this when creating a new task.<br>
     * Initialises priority and status to their default valies.<br>
     * They shouldn't be initialised as null.
     * @return A new template taskInfo.
     */
    public static TaskInfo create() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.priority = Priority.defaultPriority();
        taskInfo.status = Status.defaultStatus();
        return taskInfo;
    }
    
    /**
     * USE THIS SPARINGLY<br>
     * This creates a taskInfo object without initialising the priority and
     * status. Avoid using this unless you really need them to be null.
     * Use TaskInfo.create() instead.<br>
     * If you can't decide which one to use, use TaskInfo.create().
     * @return An empty taskInfo with priority and status set to null.
     */
    public static TaskInfo createEmpty() {
        TaskInfo taskInfo = new TaskInfo();
        return taskInfo;
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
        
        return (namesEqual(other) &&
                detailsEqual(other) &&
                durationsEqual(other) &&
                endDatesEqual(other) &&
                endTimesEqual(other) &&
                numberOfTimesEqual(other) &&
                prioritiesEqual(other) &&
                repeatIntervalsEqual(other) &&
                statusesEqual(other) &&
                tagsEqual(other));
    }

    private boolean tagsEqual(TaskInfo other) {
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

    private boolean statusesEqual(TaskInfo other) {
        if (status != other.status) {
            return false;
        }
        return true;
    }

    private boolean repeatIntervalsEqual(TaskInfo other) {
        if (repeatInterval == null) {
            if (other.repeatInterval != null) {
                return false;
            }
        } else if (!repeatInterval.equals(other.repeatInterval)) {
            return false;
        }
        return true;
    }

    private boolean prioritiesEqual(TaskInfo other) {
        if (priority != other.priority) {
            return false;
        }
        return true;
    }

    private boolean numberOfTimesEqual(TaskInfo other) {
        if (numberOfTimes != other.numberOfTimes) {
            return false;
        }
        return true;
    }

    private boolean endTimesEqual(TaskInfo other) {
        if (endTime == null) {
            if (other.endTime != null) {
                return false;
            }
        } else if (!endTime.equals(other.endTime)) {
            return false;
        }
        return true;
    }

    private boolean endDatesEqual(TaskInfo other) {
        if (endDate == null) {
            if (other.endDate != null) {
                return false;
            }
        } else if (!endDate.equals(other.endDate)) {
            return false;
        }
        return true;
    }

    private boolean durationsEqual(TaskInfo other) {
        if (duration == null) {
            if (other.duration != null) {
                return false;
            }
        } else if (!duration.equals(other.duration)) {
            return false;
        }
        return true;
    }

    private boolean detailsEqual(TaskInfo other) {
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
        return true;
    }

    private boolean namesEqual(TaskInfo other) {
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
        return true;
    }

}
