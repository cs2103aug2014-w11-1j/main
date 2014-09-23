package data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.taskinfo.TaskInfo;

public class TaskDataTest {

    @Test
    public void test() {
        TaskInfo dummyTask = new TaskInfo();
        dummyTask.name = "testname1";
        
        TaskData taskData = new TaskData();
        
        assertEquals(0, taskData.getSize());
        
        TaskId id1 = taskData.add(dummyTask);
        assertEquals(1, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));

        dummyTask.name = "testname2";
        TaskId id2 = taskData.add(dummyTask);
        assertEquals(2, taskData.getSize());
        assertEquals("testname2", taskData.getTaskName(id2));

        dummyTask.name = "testname3";
        TaskId id3 = taskData.add(dummyTask);
        assertEquals(3, taskData.getSize());
        assertEquals("testname3", taskData.getTaskName(id3));

        dummyTask.name = "testname4";
        TaskId id4 = taskData.add(dummyTask);
        assertEquals(4, taskData.getSize());
        assertEquals("testname4", taskData.getTaskName(id4));


        assertEquals(true, taskData.remove(id2));
        assertEquals(3, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));


        dummyTask.name = "testname5";
        TaskId id5 = taskData.add(dummyTask);
        assertEquals(4, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals("testname5", taskData.getTaskName(id5));


        assertEquals(false, taskData.remove(id2));
        assertEquals(4, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals("testname5", taskData.getTaskName(id5));


        assertEquals(true, taskData.remove(id5));
        assertEquals(3, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));


        assertEquals(true, taskData.remove(id4));
        assertEquals(2, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals(null, taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));
        
        
        dummyTask.name = "testname6";
        TaskId id6 = taskData.add(dummyTask);
        assertEquals(3, taskData.getSize());
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals(null, taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));
        assertEquals("testname6", taskData.getTaskName(id6));
        

        assertEquals(true, taskData.remove(id1));
        assertEquals(false, taskData.remove(id2));
        assertEquals(true, taskData.remove(id3));
        assertEquals(false, taskData.remove(id4));
        assertEquals(false, taskData.remove(id5));
        assertEquals(true, taskData.remove(id6));

        assertEquals(false, taskData.remove(id1));
        assertEquals(false, taskData.remove(id6));

        assertEquals(0, taskData.getSize());
        
        dummyTask.name = "testname7";
        TaskId id7 = taskData.add(dummyTask);
        assertEquals(1, taskData.getSize());
        assertEquals(null, taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals(null, taskData.getTaskName(id3));
        assertEquals(null, taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));
        assertEquals(null, taskData.getTaskName(id6));
        assertEquals("testname7", taskData.getTaskName(id7));

        TaskId testId1 = new TaskId(id7.id+1);
        TaskId testId2 = new TaskId(id7.id+2);
        TaskId testId3 = new TaskId(id7.id+3);
        TaskId testId4 = new TaskId(id7.id+4);

        assertEquals(null, taskData.getTaskName(testId1));
        assertEquals(null, taskData.getTaskName(testId2));
        assertEquals(null, taskData.getTaskName(testId3));
        assertEquals(null, taskData.getTaskName(testId4));

        
        // Fill up entire task data list.
        TaskId resultId = testId1;
        while (resultId != null) {
            resultId = taskData.add(dummyTask);
        }
        
        assertEquals(TaskId.MAX_ID, taskData.getSize());
        taskData.remove(testId1);
        taskData.remove(testId4);
        assertEquals(TaskId.MAX_ID-2, taskData.getSize());
        taskData.remove(testId4);
        assertEquals(TaskId.MAX_ID-2, taskData.getSize());

        
        dummyTask.name = "testId5";
        TaskId testId5 = taskData.add(dummyTask);
        assertTrue(testId5 != null);
        assertEquals(TaskId.MAX_ID-1, taskData.getSize());
        assertEquals("testId5", taskData.getTaskName(testId5));
        
        
        taskData.remove(testId2);
        assertEquals(TaskId.MAX_ID-2, taskData.getSize());
        assertEquals("testId5", taskData.getTaskName(testId5));
        

        dummyTask.name = "testId6";
        TaskId testId6 = taskData.add(dummyTask);
        assertTrue(testId6 != null);
        assertEquals(TaskId.MAX_ID-1, taskData.getSize());
        assertEquals("testId5", taskData.getTaskName(testId5));
        assertEquals("testId6", taskData.getTaskName(testId6));
        

        TaskId testId7 = taskData.add(dummyTask);
        assertTrue(testId7 != null);
        assertEquals(TaskId.MAX_ID, taskData.getSize());
        
        TaskId testId8 = taskData.add(dummyTask);
        assertTrue(testId8 == null);
        assertEquals(TaskId.MAX_ID, taskData.getSize());
    }

}
