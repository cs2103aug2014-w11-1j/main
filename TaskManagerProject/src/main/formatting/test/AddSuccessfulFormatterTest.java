package main.formatting.test;

import static org.junit.Assert.assertEquals;
import main.formatting.AddSuccessfulFormatter;
import main.message.AddSuccessfulMessage;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class AddSuccessfulFormatterTest {

    @Test
    public void test() {
        TaskInfo task = TaskInfo.create();
        task.name = "ABCD";
        
        TaskId taskId = new TaskId(123);
        AddSuccessfulMessage message = new AddSuccessfulMessage(task, taskId);
        
        AddSuccessfulFormatter formatter = new AddSuccessfulFormatter();
       
        String result = formatter.format(message);
        
        assertEquals("Task ABCD added successfully." + 
                System.lineSeparator(), result);
    }

}
