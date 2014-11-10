package main.formatting.utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.formatting.utility.ColorUtility.Color;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A utility to format a set of tasks in a condensed form, based on the 
 * date of the task.
 *
 */
public class SummaryUtility {
    private final static int WIDTH_LINE = 79;
    private final static int WIDTH_TIME = 14;
    private final static int WIDTH_ABSOLUTE = 7;
    private final static String LINE_FLOATING = "Floating Tasks ---";
    private final static String LINE_NO_TASK = "No tasks found.";
    private final static String LINE_BLANK = "";
    
    private final static int BULLET = -1;

    ColorUtility colorUtility;
    
    public SummaryUtility(){
        colorUtility = new ColorUtility();
    }
    
    private String getDateLine(TaskInfo task) {
        LocalDate date;
        date = task.endDate;
        if (task.startDate != null && task.endTime == LocalTime.MIDNIGHT){
            date = task.endDate.plusDays(-1);
        }
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("E, d MMM u");
        String dateString = formatter.format(date);
        return dateString + " ---";
    }
    
    private String getTimeString(LocalTime startTime, LocalTime endTime) {
        if(startTime == null && endTime == null) {
            return "[           ] ";
        } else if (startTime == null) {
            DateTimeFormatter formatter = 
                    DateTimeFormatter.ofPattern("HH:mm");
            String addedFormat = "[   %1$s   ] ";
            return String.format(addedFormat, formatter.format(endTime));
        } else {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("HH:mm");
            String addedFormat = "[%1$s-%2$s] ";
            String startTimeString = formatter.format(startTime);
            String endTimeString = formatter.format(endTime);
            if (endTimeString.equals("00:00")) {
                endTimeString = "24:00";
            }
            return String.format(addedFormat, startTimeString, endTimeString);
        }
    }
    
    int numberLength(int number) {
        String numberString = Integer.toString(number);
        return numberString.length();
    }
    
    private String cutToWidth(String s, int width) {
        String cutString = s.substring(0, width - 3);
        return cutString + "...";
    }
    
    private String padRightToWidth(String s, int width) {
        StringBuilder builder = new StringBuilder(s);
        for (int i = s.length(); i < width; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }
    
    
    private String padLeftToWidth(String s, int width) {
        StringBuilder builder = new StringBuilder("");
        for (int i = s.length(); i < width; i++) {
            builder.append(" ");
        }
        builder.append(s);
        return builder.toString();
    }
    
    private String getTaskNameString(String taskName, int width) {
        if (taskName.length() > width) {
            return cutToWidth(taskName, width);
        } else {
            return padRightToWidth(taskName, width);
        }
    }
    
    private String getTaskNumberString(int taskNumber, int width) {
        if (taskNumber == BULLET) {
            return "   ";
        } else {
            String line = Integer.toString(taskNumber) + ") ";
            return padLeftToWidth(line, width);
        }
    }
    
    private String getAbsoluteTaskIdString(TaskId taskId) {
        if (taskId == null) {
            return "       ";
        } else {
            return String.format("- [%1$s]", taskId.toString());
        }
    }
    
    private String getTaskInfoLine(TaskInfo task, TaskId taskId, 
            int taskNumber, 
            int numberWidth,
            Color color) {
        StringBuilder line = new StringBuilder();
        int taskNameWidth = WIDTH_LINE - WIDTH_ABSOLUTE - WIDTH_TIME - 
                numberWidth;
        line.append(getTaskNumberString(taskNumber, numberWidth));
        line.append(getTimeString(task.startTime, task.endTime));
        
        String taskNameString = getTaskNameString(task.name, taskNameWidth);
        
        line.append(colorUtility.colorize(taskNameString, color));
        
        line.append(getAbsoluteTaskIdString(taskId));
        
        return line.toString();
    }
    
    private String getFloatingTaskLine() {
        return LINE_FLOATING;
    }
    
    private LocalDate getActualDate(TaskInfo task) {
        if (task.endTime == null) {
            return task.endDate;
        } else if (task.startTime == null) {
            return task.endDate;
        } else {
            if (task.endTime == LocalTime.MIDNIGHT) {
                return task.endDate.plusDays(-1);
            } else {
                return task.endDate;
            }
        }
    }

    private LocalDate getCompleteTaskEndDate(TaskInfo[] tasks, TaskId[] taskIds,
            TaskId wantedTaskId) {
        LocalDate result = null;
        
        for (int i = 0; i < tasks.length; i++) {
            if (taskIds[i].equals(wantedTaskId) && 
                    getActualDate(tasks[i]) != null) {
                if (result == null || 
                        result.compareTo(getActualDate(tasks[i])) < 0) {
                    result = getActualDate(tasks[i]);
                }
            }
        }
        return result;
    }    
    private boolean isDifferentDate(TaskInfo task1, TaskInfo task2) {
        LocalDate date1 = getActualDate(task1);
        LocalDate date2 = getActualDate(task2);
        if (date1 == null) {
            return date2 != null;
        } else {
            return !date1.equals(date2);
        }
    }
    
    private boolean isOverdue(LocalDate date) {
        if (date == null) {
            return false;
        } else {
            LocalDate now = LocalDate.now();
            return date.compareTo(now) < 0;
        }
    }
    
    private boolean isOverdue(TaskInfo[] tasks, TaskId[] taskIds, 
            TaskId wantedTaskId) {
        LocalDate date = getCompleteTaskEndDate(tasks, taskIds, wantedTaskId);
        return isOverdue(date);
    }
    
    private boolean isOverdueNoId(TaskInfo task) {
        LocalDate date = getActualDate(task);
        return isOverdue(date);
    }
    
    private boolean isUrgent(TaskInfo task) {
        return task.priority == Priority.HIGH;
    }
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks, 
            TaskId[] taskIds, boolean isNumbered) {
        ArrayList<String> result = new ArrayList<String>();
        if (tasks.length == 0)
            result.add(LINE_NO_TASK);
        else {
            int numberWidth;
            if (!isNumbered) {
                numberWidth = 3;
            } else {
                numberWidth = numberLength(tasks.length) + 2;
            }
            for (int i = 0; i < tasks.length; i++) {
                if (tasks[i].endDate == null) {
                    if (i == 0) {
                        result.add(getFloatingTaskLine());
                    } else if (tasks[i - 1].endDate != null) {
                        result.add(LINE_BLANK);
                        result.add(getFloatingTaskLine());
                    }
                }
                else {
                    if (i == 0) {
                        result.add(getDateLine(tasks[i]));
                    } else if (isDifferentDate(tasks[i], tasks[i - 1])) {
                        result.add(LINE_BLANK);
                        result.add(getDateLine(tasks[i]));
                    }
                }
                int lineNumber;
                if (isNumbered) {
                    lineNumber = i + 1;
                } else {
                    lineNumber = BULLET;
                }
                if (taskIds == null) {
                    if (isOverdueNoId(tasks[i])) {
                        result.add(getTaskInfoLine(tasks[i], null, 
                                lineNumber, numberWidth, Color.RED));
                    } else if (isUrgent(tasks[i])) {
                        result.add(getTaskInfoLine(tasks[i], null,
                                lineNumber, numberWidth, Color.YELLOW));
                    } else {
                        result.add(getTaskInfoLine(tasks[i], null,
                                lineNumber, numberWidth, Color.WHITE));
                    }
                } else {
                    if (isOverdue(tasks, taskIds, taskIds[i])) {
                        result.add(getTaskInfoLine(tasks[i], taskIds[i], 
                                lineNumber, numberWidth, Color.RED));
                    } else if (isUrgent(tasks[i])) {
                        result.add(getTaskInfoLine(tasks[i], taskIds[i],
                                lineNumber, numberWidth, Color.YELLOW));
                    } else {
                        result.add(getTaskInfoLine(tasks[i], taskIds[i],
                                lineNumber, numberWidth, Color.WHITE));
                    }
                }
            }
        }
        return result;
        
    }
    
    private String arrayListToStringLines(ArrayList<String> lines) {
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line);
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
    
    /**
     * Format an array of tasks to the summary form.
     * 
     * All TaskInfo shouldn't span more than one day. If the TaskInfo spans more
     * than one day, please split the TaskInfo into multiple TaskInfo 
     * beforehand.
     * @param tasks The tasks to be shown.
     * @param taskIds The TaskId of the tasks to be shown
     * @param isNumbered true if the format is numbered, false if it is not.
     * @return The formatted TaskInfo[] as a String.
     */
    public String format(TaskInfo[] tasks, TaskId[] taskIds, 
            boolean isNumbered) {
        ArrayList<String> formattedTaskArray = 
                formatToArrayList(tasks, taskIds, isNumbered);
        return arrayListToStringLines(formattedTaskArray);
    }
}
