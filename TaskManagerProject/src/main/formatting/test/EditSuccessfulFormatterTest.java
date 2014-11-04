package main.formatting.test;



import java.time.LocalDate;
import java.time.LocalTime;

import main.formatting.EditSuccessfulFormatter;
import main.message.EditSuccessfulMessage;
import main.message.EditSuccessfulMessage.Field;

import org.junit.Assert;
import org.junit.Test;

import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class EditSuccessfulFormatterTest {

    @Test
    public void test() {
        EditSuccessfulFormatter formatter = new EditSuccessfulFormatter();
        
        TaskId[] taskIds = new TaskId[1];
        TaskInfo[] tasks = new TaskInfo[1];
        
        taskIds[0] = new TaskId(TaskId.toIntId("4GQ"));
        tasks[0] = TaskInfo.create();
        tasks[0].endTime = LocalTime.parse("13:13");
        tasks[0].endDate = LocalDate.parse("2014-10-02");
        tasks[0].name = "This is a task";
        tasks[0].details = "HAHAHAHA";
        tasks[0].priority = Priority.HIGH;
        tasks[0].tags = new Tag[2];
        tasks[0].tags[0] = new Tag("abcd");
        tasks[0].tags[1] = new Tag("efgh");
        tasks[0].status = Status.DONE;
        Field[] field = new Field[2];
        field[0] = Field.NAME;
        field[1] = Field.STATUS;
        EditSuccessfulMessage message = 
                new EditSuccessfulMessage(tasks, taskIds, field);
        
        String formattedString = formatter.format(message);
        
        String expectedString = 
                "Task name changed." + System.lineSeparator() +
                "Status changed." + System.lineSeparator() +
                System.lineSeparator() + 
                "Task [4GQ]" + System.lineSeparator() +
                "   Name: This is a task" + System.lineSeparator() +
                "   Time: 13:13 (PM)" + System.lineSeparator() +
                "   Date: Thursday, 2 Oct 2014" + System.lineSeparator() +
                "   Tags: abcd, efgh" + System.lineSeparator() + 
                "   Priority: High" + System.lineSeparator() +
                "   Status: Done" + System.lineSeparator() +
                "   Description: HAHAHAHA" + System.lineSeparator();
        
        Assert.assertEquals(expectedString, formattedString);
    }

}
