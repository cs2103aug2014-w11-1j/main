package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskIdTest {

    @Test
    public void test() {
        for (int i=0; i<TaskId.MAX_ID; i++)
            testIndexConvert(i);

    }
    
    private void testIndexConvert(int a) {
        int b = TaskId.numberTranslateForward(a);
        assertFalse(a == b);
        int c = TaskId.numberTranslateInverse(b);
        assertEquals(a,c);

        b = TaskId.numberTranslateInverse(a);
        assertFalse(a == b);
        c = TaskId.numberTranslateForward(b);
        assertEquals(a,c);
    }

}
