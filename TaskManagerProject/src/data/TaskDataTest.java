package data;

import static org.junit.Assert.*;

import org.junit.Test;

import data.taskinfo.TaskInfo;

public class TaskDataTest {

    @Test
    public void test() {
        TaskInfo dummyTask = TaskInfo.create();
        dummyTask.name = "testname1";
        
        TaskData taskData = new TaskData();
        
        assertEquals(0, taskData.getSize());
        testSizeAndIteration(taskData, 0);
        
        TaskId id1 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 1);
        assertEquals("testname1", taskData.getTaskName(id1));

        dummyTask.name = "testname2";
        TaskId id2 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 2);
        assertEquals("testname2", taskData.getTaskName(id2));

        dummyTask.name = "testname3";
        TaskId id3 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 3);
        assertEquals("testname3", taskData.getTaskName(id3));

        dummyTask.name = "testname4";
        TaskId id4 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 4);
        assertEquals("testname4", taskData.getTaskName(id4));


        assertEquals(true, taskData.remove(id2));
        testSizeAndIteration(taskData, 3);
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));


        dummyTask.name = "testname5";
        TaskId id5 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 4);
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals("testname5", taskData.getTaskName(id5));


        assertEquals(false, taskData.remove(id2));
        testSizeAndIteration(taskData, 4);
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals("testname5", taskData.getTaskName(id5));


        assertEquals(true, taskData.remove(id5));
        testSizeAndIteration(taskData, 3);
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals("testname4", taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));


        assertEquals(true, taskData.remove(id4));
        testSizeAndIteration(taskData, 2);
        assertEquals("testname1", taskData.getTaskName(id1));
        assertEquals(null, taskData.getTaskName(id2));
        assertEquals("testname3", taskData.getTaskName(id3));
        assertEquals(null, taskData.getTaskName(id4));
        assertEquals(null, taskData.getTaskName(id5));
        
        
        dummyTask.name = "testname6";
        TaskId id6 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 3);
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
        testSizeAndIteration(taskData, 0);
        
        dummyTask.name = "testname7";
        TaskId id7 = taskData.add(dummyTask);
        testSizeAndIteration(taskData, 1);
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
            taskData.discardUndoSnapshot();
            resultId = taskData.add(dummyTask);
        }
        
        testSizeAndIteration(taskData, TaskId.MAX_ID);
        taskData.remove(testId1);
        taskData.remove(testId4);
        testSizeAndIteration(taskData, TaskId.MAX_ID-2);
        taskData.remove(testId4);
        testSizeAndIteration(taskData, TaskId.MAX_ID-2);

        
        dummyTask.name = "testId5";
        TaskId testId5 = taskData.add(dummyTask);
        assertTrue(testId5 != null);
        testSizeAndIteration(taskData, TaskId.MAX_ID-1);
        assertEquals("testId5", taskData.getTaskName(testId5));
        
        
        taskData.remove(testId2);
        testSizeAndIteration(taskData, TaskId.MAX_ID-2);
        assertEquals("testId5", taskData.getTaskName(testId5));
        

        dummyTask.name = "testId6";
        TaskId testId6 = taskData.add(dummyTask);
        assertTrue(testId6 != null);
        testSizeAndIteration(taskData, TaskId.MAX_ID-1);
        assertEquals("testId5", taskData.getTaskName(testId5));
        assertEquals("testId6", taskData.getTaskName(testId6));
        

        TaskId testId7 = taskData.add(dummyTask);
        assertTrue(testId7 != null);
        testSizeAndIteration(taskData, TaskId.MAX_ID);
        
        TaskId testId8 = taskData.add(dummyTask);
        assertTrue(testId8 == null);
        testSizeAndIteration(taskData, TaskId.MAX_ID);
    }

    /**
     * Checks whether the size of taskData matches size, and checks whether
     * the taskData is able to iterate through the list through next / previous
     * methods properly.
     * 
     * @param taskData taskData to test
     * @param size expected size of taskData.
     */
    private void testSizeAndIteration(TaskData taskData, int size) {
        assertEquals(size, taskData.getSize());
        
        if (size == 0) {
            assertFalse(taskData.getFirst().isValid());
            assertFalse(taskData.getLast().isValid());
            
        } else {
            TaskId current = taskData.getFirst();
            
            for (int i=0; i<size-1; i++) {
                TaskId next = taskData.getNext(current);
                assertEquals(current, taskData.getPrevious(next));
                current = next;
            }
            assertEquals(current, taskData.getLast());
            assertFalse(taskData.getNext(current).isValid());
        }
    }

}
