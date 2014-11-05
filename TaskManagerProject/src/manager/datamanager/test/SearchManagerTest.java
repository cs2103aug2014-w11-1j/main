package manager.datamanager.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import manager.datamanager.SearchManager;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.KeywordFilter;
import manager.result.SearchResult;

import org.junit.Test;

import data.TaskData;
import data.taskinfo.TaskInfo;

public class SearchManagerTest {

    TaskInfo task1 = createTask("apple", getTime(3,0), getDate(11,16),getTime(9, 0), getDate(11, 16));
    TaskInfo task2 = createTask("banana", getTime(5, 0), getDate(11, 16), getTime(12, 0), getDate(11, 17));
    TaskInfo task3 = createTask("cheese", getTime(13, 0), getDate(11,15), getTime(19, 0), getDate(11, 15));
    TaskInfo task4 = createTask("dip", null, null , getTime(11, 0), getDate(11, 16));
    TaskInfo task5 = createTask("egg", getTime(19, 0), getDate(10, 30), getTime(1, 20), getDate(11,16));
    TaskInfo task6 = createTask("fish", null, null, null, null);
    TaskInfo task7 = createTask("gum", getTime(10, 0), getDate(11, 8), getTime(16, 0), getDate(11, 10));
    TaskInfo task8 = createTask("ham", null, null, null, getDate(11, 3));

    /**
     * Test simple task within a day
     */
    @Test
    public void test1(){ 
        TaskData taskData = new TaskData();
        SearchManager manager = new SearchManager(taskData);

        taskData.add(task1);
        taskData.add(task2);
        taskData.add(task3);
        taskData.add(task4);
        taskData.add(task5);
        taskData.add(task6);
        taskData.add(task7);
        taskData.add(task8);
        
        
        Filter[] filters = new Filter[1];
        filters[0] = new KeywordFilter(new String[]{"egg"});
        SearchResult result = (SearchResult)manager.searchTasksWithoutSplit(filters);
        
        assertEquals(1, result.getTasks().length);
        assertEquals("egg", result.getTasks()[0].name);
    }

    
    private static TaskInfo createTask(String name,LocalTime startTime, LocalDate startDate,
            LocalTime endTime, LocalDate endDate){
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.name = name;
        taskInfo.startTime = startTime;
        taskInfo.startDate = startDate;
        taskInfo.endTime = endTime;
        taskInfo.endDate = endDate;
        return taskInfo;
    }
    
    private static LocalTime getTime(int hour, int minute){
        return LocalTime.of(hour, minute);
    }

    private static LocalDate getDate(int month, int day){
        return LocalDate.of(2014, month, day);
    }
}
