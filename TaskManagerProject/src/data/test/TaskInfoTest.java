package data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

//@author A0065475X
public class TaskInfoTest {
    
    @Test
    public void testIsValid() {

        TaskInfo taskInfo = TaskInfo.create();
        
        // Invalid - null name
        assertFalse(taskInfo.isValid());
        
        // Invalid - no name
        taskInfo.name = "";
        assertFalse(taskInfo.isValid());
        
        // Invalid - whitespace only
        taskInfo.name = "   ";
        assertFalse(taskInfo.isValid());
        
        taskInfo.name = "   heh";
        assertTrue(taskInfo.isValid());

        taskInfo.name = "lalala";
        assertTrue(taskInfo.isValid());

        // Time only, no date - invalid.
        taskInfo.endTime = LocalTime.of(8, 30);
        assertFalse(taskInfo.isValid());
        
        taskInfo.endDate = LocalDate.of(2014, 5, 20);
        assertTrue(taskInfo.isValid());

        // No start time - invalid
        taskInfo.startDate = LocalDate.of(2014, 5, 20);
        assertFalse(taskInfo.isValid());

        // Valid - start before end.
        taskInfo.startTime = LocalTime.of(3, 30);
        assertTrue(taskInfo.isValid());

        // Invalid - start after end.
        taskInfo.startTime = LocalTime.of(8, 31);
        assertFalse(taskInfo.isValid());

        // Valid - start before end
        taskInfo.endDate = LocalDate.of(2014, 6, 20);
        assertTrue(taskInfo.isValid());
        
        // Invalid - no name
        taskInfo.name = "";
        assertFalse(taskInfo.isValid());
    }

    @Test
    public void test() {
        //Solely for testing the extremely long equals() method!
        TaskInfo taskInfo = TaskInfo.create();
        TaskInfo taskInfo2 = TaskInfo.create();

        assertEquals(taskInfo, taskInfo);

        taskInfo.details = "";
        assertEquals(taskInfo, taskInfo2);

        taskInfo.name = "";
        assertEquals(taskInfo, taskInfo2);

        taskInfo2.name = "a";
        assertFalse(taskInfo.equals(taskInfo2));

        taskInfo = new TaskInfo(taskInfo2);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.endDate = LocalDate.of(5, 4, 3);
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.endTime = LocalTime.of(23, 14, 0);
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.priority = Priority.HIGH;
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.status = Status.DONE;
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.tags = null;
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo.tags = new Tag[]{new Tag("one"), new Tag("two"), new Tag("four")};
        assertEquals(taskInfo, taskInfo);
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));
        taskInfo2 = new TaskInfo(taskInfo);
        assertEquals(taskInfo, taskInfo2);

        taskInfo2.tags = new Tag[]{new Tag("one"), new Tag("four"), new Tag("two")};
        assertEquals(taskInfo, taskInfo2);
        assertEquals(taskInfo2, taskInfo);

        taskInfo.tags = new Tag[]{new Tag("one"), new Tag("two"), new Tag("two")};
        assertFalse(taskInfo.equals(taskInfo2));
        assertFalse(taskInfo2.equals(taskInfo));

        taskInfo2.tags = new Tag[]{new Tag("one"), new Tag("two")};
        assertEquals(taskInfo, taskInfo2);
        assertEquals(taskInfo2, taskInfo);

        taskInfo2.tags = new Tag[]{new Tag("one"), new Tag("one"), new Tag("two")};
        assertEquals(taskInfo, taskInfo2);
        assertEquals(taskInfo2, taskInfo);

        assertEquals(taskInfo, taskInfo);
        assertEquals(taskInfo2, taskInfo2);
    }

}
