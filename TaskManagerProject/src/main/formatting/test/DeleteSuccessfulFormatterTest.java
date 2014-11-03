package main.formatting.test;

import static org.junit.Assert.assertEquals;
import main.formatting.DeleteSuccessfulFormatter;
import main.message.DeleteSuccessfulMessage;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DeleteSuccessfulFormatterTest {

    @Test
    public void test() {
        TaskInfo task = TaskInfo.create();
        task.name = "ABCD";
        
        TaskId taskId = new TaskId(1);
        
        DeleteSuccessfulMessage message = 
                new DeleteSuccessfulMessage(task, taskId);
        
        DeleteSuccessfulFormatter formatter = new DeleteSuccessfulFormatter();
        
        assertEquals("Task ABCD deleted." + System.lineSeparator(), 
                formatter.format(message));
    }

}
