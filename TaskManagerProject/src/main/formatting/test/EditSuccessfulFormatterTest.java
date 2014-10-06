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

public class EditSuccessfulFormatterTest {

    @Test
    public void test() {
        EditSuccessfulFormatter formatter = new EditSuccessfulFormatter();
        
        TaskId taskId = new TaskId(TaskId.toIntId("4ef"));
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.endTime = LocalTime.parse("13:13");
        taskInfo.endDate = LocalDate.parse("2014-10-02");
        taskInfo.name = "This is a task";
        taskInfo.details = "HAHAHAHA";
        taskInfo.priority = Priority.HIGH;
        taskInfo.tags = new Tag[2];
        taskInfo.tags[0] = new Tag("abcd");
        taskInfo.tags[1] = new Tag("efgh");
        taskInfo.status = Status.DONE;
        Field[] field = new Field[2];
        field[0] = Field.NAME;
        field[1] = Field.STATUS;
        EditSuccessfulMessage message = 
                new EditSuccessfulMessage(taskInfo, taskId, field);
        
        String formattedString = formatter.format(message);
        
        String expectedString = 
                "Task name changed." + System.lineSeparator() +
                "Status changed." + System.lineSeparator() +
                System.lineSeparator() + 
                "Task [4ef]" + System.lineSeparator() +
                "   Name: This is a task" + System.lineSeparator() +
                "   Time: 13:13 (PM)" + System.lineSeparator() +
                "   Date: Thursday, 2 Oct 2014" + System.lineSeparator() +
                "   Tags: abcd, efgh" + System.lineSeparator() + 
                "   Priority: High" + System.lineSeparator() +
                "   Description: HAHAHAHA" + System.lineSeparator();
        
        Assert.assertEquals(expectedString, formattedString);
    }

}
