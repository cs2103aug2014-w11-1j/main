package manager.datamanager.searchfilter.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import manager.datamanager.searchfilter.KeywordFilter;

import org.junit.Test;

import data.taskinfo.TaskInfo;

public class KeywordFilterTest {

    @Test
    public void testSingleWordNameTrue() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"test"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
    
    @Test
    public void testSingleWordDetailsTrue() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.details = "I am going to test this task";
        testedTask.name = null;
        String[] words = new String[]{"test"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }

    @Test
    public void testSingleWordNameFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"doesntexist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertFalse(filter.filter(testedTask));
    }
    
    @Test
    public void testSingleDetailsNameFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"doesntexist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertFalse(filter.filter(testedTask));
    }
    
    @Test
    public void testSingleWordDetailsFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"doesntexist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertFalse(filter.filter(testedTask));
    }
    
    @Test
    public void testSingleWordBoth() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "This exist.";
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"exist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
 
    @Test
    public void testMultipleWordsDetailsPartial() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"task", "exist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertFalse(filter.filter(testedTask));
    }
    
    @Test
    public void testCaseSensitivity() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"TeSt"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
    
    @Test
    public void testTab() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "abcd\ttest\tjaksldjf";
        String[] words = new String[]{"test"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
    
    @Test
    public void testNewline() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "hahaha\ntest\nhahaha";
        String[] words = new String[]{"test"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
    @Test
    public void testPunctuation( ){
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "hahaha$%^$%^$!@#test^$&@#$hahaha";
        String[] words = new String[]{"test"};
        KeywordFilter filter = new KeywordFilter(words);
        assertTrue(filter.filter(testedTask));
    }
}
