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
    }

}
