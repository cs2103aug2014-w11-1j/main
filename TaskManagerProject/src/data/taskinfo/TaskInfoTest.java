package data.taskinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

public class TaskInfoTest {

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

        taskInfo.tags = new Tag[0];
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
