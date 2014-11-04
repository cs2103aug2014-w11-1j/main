package manager.datamanager.suggestion.test;

import static org.junit.Assert.assertEquals;
import manager.datamanager.suggestion.EditDistance;

import org.junit.Test;

//@author A0113011L
public class EditDistanceTest {

    @Test
    public void testEmpty() {
        test("", "", 0);
    }
    
    @Test
    public void testOneEmpty() {
        test("ThisIsAPointlessString", "", 22);
    }
    
    @Test
    public void testTwoEmpty() {
        test("", "ThisIsAPointlessString", 22);
    }
    
    @Test
    public void testInsert() {
        test("abcdfgh", "abcdefgh", 1);
    }
    
    @Test
    public void testDelete() {
        test("abcdefgh", "abcdfgh", 1);
    }
    
    @Test
    public void testChange() {
        test("abcdefgh", "abccefgh", 1);
    }
    
    @Test
    public void testCombination() {
        test("kitten", "sitting", 3);
    }
    
    public void test(String stringOne, String stringTwo, int expected) {
        EditDistance editDistance = new EditDistance(stringOne, stringTwo);
        int distance = editDistance.getDistance();
        assertEquals(expected, distance);
    }
}
