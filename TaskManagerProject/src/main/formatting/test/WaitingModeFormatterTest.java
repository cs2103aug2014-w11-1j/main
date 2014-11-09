package main.formatting.test;

import static org.junit.Assert.assertEquals;
import main.formatting.WaitingModeFormatter;
import main.modeinfo.SearchModeInfo;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class WaitingModeFormatterTest {

    @Test
    public void testEmpty() {
        WaitingModeFormatter formatter = new WaitingModeFormatter();
        
        TaskInfo[] emptyTask = new TaskInfo[0];
        TaskId[] emptyId = new TaskId[0];
        String[] emptySuggestion = new String[0];
        SearchModeInfo modeInfo = new SearchModeInfo(emptyTask, emptyId, emptySuggestion);
        
        assertEquals("No tasks found." + 
                System.lineSeparator() + 
                System.lineSeparator(), 
                formatter.format(modeInfo));
    }

    @Test
    public void testMultiple() {
        WaitingModeFormatter formatter = new WaitingModeFormatter();
        
        TaskInfo[] tasks = new TaskInfo[2];
        TaskId[] taskIds = new TaskId[2];
        String[] emptySuggestion = new String[0];
        
        tasks[0] = TaskInfo.create();
        tasks[0].name = "abcd ijkl";
        
        tasks[1] = TaskInfo.create();
        tasks[1].name = "abcd efgh";
        
        taskIds[0] = new TaskId(1);
        taskIds[1] = new TaskId(2);
        SearchModeInfo modeInfo = new SearchModeInfo(tasks, taskIds, emptySuggestion);
        
        
        assertEquals("Did you mean:" + 
                System.lineSeparator() + 
                "1) abcd ijkl" +
                System.lineSeparator() +
                "2) abcd efgh" +
                System.lineSeparator() +
                System.lineSeparator(), 
                formatter.format(modeInfo));
    }
}
