package main.formatting.test;

import static org.junit.Assert.assertEquals;
import main.formatting.DeleteSuccessfulFormatter;
import main.message.DeleteSuccessfulMessage;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class DeleteSuccessfulFormatterTest {

    @Test
    public void testSingle() {
        TaskInfo task = TaskInfo.create();
        task.name = "ABCD";
        
        TaskId taskId = new TaskId(1);
        
        TaskInfo[] tasks = new TaskInfo[1];
        TaskId[] taskIds = new TaskId[1];
        
        tasks[0] = task;
        taskIds[0] = taskId;
        DeleteSuccessfulMessage message = 
                new DeleteSuccessfulMessage(tasks, taskIds);
        
        DeleteSuccessfulFormatter formatter = new DeleteSuccessfulFormatter();
        
        assertEquals("Task ABCD deleted." + System.lineSeparator() +
                System.lineSeparator(), formatter.format(message));
    }

    @Test
    public void testMulti() {
        TaskInfo[] tasks = new TaskInfo[2];
        TaskId[] taskIds = new TaskId[2];
        
        tasks[0] = TaskInfo.create();
        tasks[0].name = "ABCD";
        taskIds[0] = new TaskId(1);
        
        tasks[1] = TaskInfo.create();
        tasks[1].name = "EFGH";
        taskIds[1] = new TaskId(2);
        
        DeleteSuccessfulMessage message = 
                new DeleteSuccessfulMessage(tasks, taskIds);
        
        DeleteSuccessfulFormatter formatter = new DeleteSuccessfulFormatter();
        
        assertEquals("2 tasks deleted." + System.lineSeparator() +
                "- ABCD" + System.lineSeparator() +
                "- EFGH" + System.lineSeparator() + System.lineSeparator(), 
                formatter.format(message));
    }
}
