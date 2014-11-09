package manager.datamanager.searchfilter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.KeywordFilter;
import manager.datamanager.searchfilter.SuggestionFilter;

import org.junit.Test;

import data.taskinfo.TaskInfo;

//@author A0113011L
public class SuggestionFilterTest {

    @Test
    public void testType() {
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertEquals(Filter.Type.FILTER_SUGGESTION, filter.getType());
    }
    
    @Test
    public void testTopSuggestion() {
        String[] words = new String[]{"test", "abcd", "efgh"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertEquals("test", filter.getTopSuggestion());
    }
    
    @Test
    public void testSingleWordNameTrue() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
    
    @Test
    public void testSingleWordDetailsTrue() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.details = "I am going to test this task";
        testedTask.name = null;
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }

    @Test
    public void testSingleWordNameFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"doesntexist"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertFalse(filter.isMatching(testedTask));
    }
    
    @Test
    public void testSingleDetailsNameFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"doesntexist"};
        KeywordFilter filter = new KeywordFilter(words);
        assertFalse(filter.isMatching(testedTask));
    }
    
    @Test
    public void testSingleWordDetailsFalse() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"doesntexist"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertFalse(filter.isMatching(testedTask));
    }
    
    @Test
    public void testSingleWordBoth() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "This exist.";
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"exist"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
 
    @Test
    public void testMultipleWordsDetailsPartial() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = null;
        testedTask.details = "I am going to test this task";
        String[] words = new String[]{"task", "exist"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
    
    @Test
    public void testCaseSensitivity() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "I am going to test this task";
        testedTask.details = null;
        String[] words = new String[]{"TeSt"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
    
    @Test
    public void testTab() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "abcd\ttest\tjaksldjf";
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
    
    @Test
    public void testNewline() {
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "hahaha\ntest\nhahaha";
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
    @Test
    public void testPunctuation(){
        TaskInfo testedTask = TaskInfo.create();
        testedTask.name = "hahaha$%^$%^$!@#test^$&@#$hahaha";
        String[] words = new String[]{"test"};
        SuggestionFilter filter = new SuggestionFilter(words);
        assertTrue(filter.isMatching(testedTask));
    }
}
