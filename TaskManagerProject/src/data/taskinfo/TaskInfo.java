package data.taskinfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

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

}
