package data.test;

import static org.junit.Assert.*;

import org.junit.Test;

import data.TaskId;

public class TaskIdTest {

    @Test
    public void test() {
        for (int i = 0; i < TaskId.MAX_ID; i++) {
            testStringConvert(i);
        }

        testIntConvert("A7D");
        testIntConvert("8AD");
        testIntConvert("0AA");
        testIntConvert("AA0");
        testIntConvert("ZZ9");
        testIntConvert("9ZZ");
        testIntConvert("D3E");
        testIntConvert("A1C");
        testIntConvert("BG0");

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

        testValidString("g3d");
        testValidString("e3a");
        testValidString("aa0");
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
