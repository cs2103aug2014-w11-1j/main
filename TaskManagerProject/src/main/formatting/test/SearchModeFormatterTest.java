package main.formatting.test;

import java.time.LocalDate;
import java.time.LocalTime;

import main.formatting.SearchModeFormatter;
import main.modeinfo.SearchModeInfo;

import org.junit.Test;
import org.junit.Assert;

import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class SearchModeFormatterTest {

    @Test
    public void testSingleTask() {
        SearchModeFormatter formatter = new SearchModeFormatter();
        TaskId taskId = TaskId.makeTaskId("1ab");
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.details = "This is a detail";
        taskInfo.name = "This is a name";
        taskInfo.endTime = LocalTime.parse("12:40");
        taskInfo.endDate = LocalDate.parse("2014-10-01");
        taskInfo.tags = new Tag[2];
        taskInfo.tags[0] = new Tag("wow");
        taskInfo.tags[1] = new Tag("amazing");
        taskInfo.priority = Priority.HIGH;
        taskInfo.status = Status.UNDONE;
        
        TaskId[] taskIdArray = new TaskId[1];
        TaskInfo[] taskInfoArray = new TaskInfo[1];
        taskIdArray[0] = taskId;
        taskInfoArray[0] = taskInfo;
        
        SearchModeInfo searchModeInfo = new SearchModeInfo(taskInfoArray, 
                taskIdArray, null);
        String result = formatter.format(searchModeInfo);
        
        String expected = "Wed, 1 Oct 2014 ---" + System.lineSeparator() +
                "1) [   12:40   ] This is a name" +
                "                                         - [1AB]" + 
                System.lineSeparator();
        
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testOverLengthTaskName() {
        SearchModeFormatter formatter = new SearchModeFormatter();
        TaskId taskId = TaskId.makeTaskId("1ab");
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.details = "This is a detail";
        taskInfo.name = "This is a very very long name abcdefghijklmnopqrstuvwxyz "
                + "zyxwvutsrqponmlkjihgfedbca";
        taskInfo.endTime = LocalTime.parse("12:40");
        taskInfo.endDate = LocalDate.parse("2014-10-01");
        taskInfo.tags = new Tag[2];
        taskInfo.tags[0] = new Tag("wow");
        taskInfo.tags[1] = new Tag("amazing");
        taskInfo.priority = Priority.HIGH;
        taskInfo.status = Status.UNDONE;
        
        TaskId[] taskIdArray = new TaskId[1];
        TaskInfo[] taskInfoArray = new TaskInfo[1];
        taskIdArray[0] = taskId;
        taskInfoArray[0] = taskInfo;
        
        SearchModeInfo searchModeInfo = new SearchModeInfo(taskInfoArray, 
                taskIdArray, null);
        String result = formatter.format(searchModeInfo);
        
        String expected = "Wed, 1 Oct 2014 ---" + System.lineSeparator() +
                "1) [   12:40   ] " +
                "This is a very very long name abcdefghijklmnopqrstuv..."
                + "- [1AB]" + System.lineSeparator();
        
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSugggestion() {
        SearchModeFormatter formatter = new SearchModeFormatter();
        TaskId taskId = new TaskId(TaskId.toIntId("1AB"));
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.details = "This is a detail";
        taskInfo.name = "This is a very very long name abcdefghijklmnopqrstuvwxyz "
                + "zyxwvutsrqponmlkjihgfedbca";
        taskInfo.endTime = LocalTime.parse("12:40");
        taskInfo.endDate = LocalDate.parse("2014-10-01");
        taskInfo.tags = new Tag[2];
        taskInfo.tags[0] = new Tag("wow");
        taskInfo.tags[1] = new Tag("amazing");
        taskInfo.priority = Priority.HIGH;
        taskInfo.status = Status.UNDONE;
        
        TaskId[] taskIdArray = new TaskId[1];
        TaskInfo[] taskInfoArray = new TaskInfo[1];
        String[] suggestionArray = new String[1];
        taskIdArray[0] = taskId;
        taskInfoArray[0] = taskInfo;
        suggestionArray[0] = "orange";
        
        SearchModeInfo searchModeInfo = new SearchModeInfo(taskInfoArray, 
                taskIdArray, suggestionArray);
        String result = formatter.format(searchModeInfo);
        
        String expected = "Did you mean: orange?" + System.lineSeparator() + 
                "Wed, 1 Oct 2014 ---" + System.lineSeparator() +
                "1) [   12:40   ] " +
                "This is a very very long name abcdefghijklmnopqrstuv..."
                + "- [1AB]" + System.lineSeparator();
        
        Assert.assertEquals(expected, result);
    }
}
