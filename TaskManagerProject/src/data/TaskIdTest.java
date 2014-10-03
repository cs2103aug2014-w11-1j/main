package data;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskIdTest {

    @Test
    public void test() {
        for (int i = 0; i < TaskId.MAX_ID; i++) {
            testStringConvert(i);
        }

        testIntConvert("a7d");
        testIntConvert("8ad");
        testIntConvert("0aa");
        testIntConvert("aa0");
        testIntConvert("zz9");
        testIntConvert("9zz");
        testIntConvert("d3e");
        testIntConvert("a1c");
        testIntConvert("bg0");

        testInvalidString("ggg");
        testInvalidString("a00");
        testInvalidString("a0cF");
        testInvalidString("9d");
        testInvalidString("9!d");
        testInvalidString("9-d");
        testInvalidString(",dd");
        testInvalidString(",..");
        testInvalidString(",.0");
        testInvalidString("***");
        testInvalidString("1*1");
        testInvalidString("1 1");
        testInvalidString("dke9");
        testInvalidString("gad");
        testInvalidString("E");
        testInvalidString("ggg");

        testValidString("G3D");
        testValidString("E3A");
        testValidString("AA0");
        testValidString("9dR");
    }
    
    
    private void testStringConvert(int a) {
        String stringId = TaskId.toStringId(a);
        assertEquals(3, stringId.length());
        
        int c = TaskId.toIntId(stringId);
        assertEquals(a, c);
    }
    
    
    private void testIntConvert(String stringId) {
        int intId = TaskId.toIntId(stringId);
        assertEquals(stringId, TaskId.toStringId(intId));
        
        testValidString(stringId);
    }

    private void testInvalidString(String stringId) {
        TaskId taskId = TaskId.makeTaskId(stringId);
        assertEquals(taskId, null);
    }

    private void testValidString(String stringId) {
        TaskId taskId = TaskId.makeTaskId(stringId);
        assertFalse(taskId == null);
    }
}
