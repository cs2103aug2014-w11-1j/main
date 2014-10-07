package manager.datamanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import manager.datamanager.UndoManager;
import manager.result.Result;

import org.junit.Test;

import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class UndoManagerTest {

    @Test
    public void test() {
        TaskData taskData = new TaskData();
        UndoManager undoManager = new UndoManager(taskData);
        
        assertElements(taskData);
        
        
        TaskId task1Id = taskData.add(dummyTask("task1"));
        undoManager.updateUndoHistory();
        assertElements(taskData, "task1");
        
        
        taskData.add(dummyTask("task2"));
        undoManager.updateUndoHistory();
        assertElements(taskData, "task1", "task2");

        
        Result result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1");

        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1", "task2");

        
        // Undo until it you can't undo further.
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1");

        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData);

        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_FAILURE);
        assertElements(taskData);
        

        // Redo everything to get back all tasks. Until you can't redo further.
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1");
        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1", "task2");
        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_FAILURE);
        assertElements(taskData, "task1", "task2");

        
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1");
        
        
        TaskId task3Id = taskData.add(dummyTask("task3"));
        undoManager.updateUndoHistory();
        assertElements(taskData, "task1", "task3");
        

        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_FAILURE);
        assertElements(taskData, "task1", "task3");
        

        taskData.add(dummyTask("task4"));
        taskData.add(dummyTask("task5"));
        undoManager.updateUndoHistory();
        assertElements(taskData, "task1", "task3", "task4", "task5");
        

        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1", "task3");

        
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1");

        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1", "task3");
        
        
        TaskId task6Id = taskData.add(dummyTask("task6"));
        taskData.setTaskName(task1Id, "editTask1");
        undoManager.updateUndoHistory();
        assertElements(taskData, "editTask1", "task3", "task6");
        

        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1", "task3");
        

        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "editTask1", "task3", "task6");
        
        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_FAILURE);
        assertElements(taskData, "editTask1", "task3", "task6");


        taskData.setTaskName(task1Id, "editEditTask1");
        taskData.setTaskName(task3Id, "editTask3");
        taskData.setTaskName(task1Id, "editEditEditTask1");
        undoManager.updateUndoHistory();
        assertElements(taskData, "editEditEditTask1", "editTask3", "task6");

        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_FAILURE);
        //assertElements(taskData, "editEditEditTask1", "editTask3", "task6");

        
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "editTask1", "task3", "task6");

        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "editEditEditTask1", "editTask3", "task6");

        
        taskData.remove(task6Id);
        taskData.setTaskName(task3Id, "editEditTask3");
        undoManager.updateUndoHistory();
        assertElements(taskData, "editEditEditTask1", "editEditTask3");
        

        while (taskData.getSize() > 0) {
            taskData.remove(taskData.getFirst());
        }
        undoManager.updateUndoHistory();
        assertElements(taskData);

        
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "editEditEditTask1", "editEditTask3");
        
        
        
        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "editEditEditTask1", "editTask3", "task6");
        

        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "editTask1", "task3", "task6");


        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1", "task3");


        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData, "task1");


        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_SUCCESS);
        assertElements(taskData);


        result = undoManager.undo();
        assertEquals(result.getType(), Result.Type.UNDO_FAILURE);
        assertElements(taskData);


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1");


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "task1", "task3");


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "editTask1", "task3", "task6");


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "editEditEditTask1", "editTask3", "task6");


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData, "editEditEditTask1", "editEditTask3");


        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_SUCCESS);
        assertElements(taskData);
        
        
        result = undoManager.redo();
        assertEquals(result.getType(), Result.Type.REDO_FAILURE);
        assertElements(taskData);

    }
    
    private TaskInfo dummyTask(String name) {
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.name = name;
        return taskInfo;
    }
    
    private void assertSize(TaskData taskData, int size) {
        assertEquals(size, taskData.getSize());
    }
    
    private void assertElements(TaskData taskData, String... taskNames) {
        TaskId current = taskData.getFirst();
        
        for (String taskName : taskNames) {
            String actualName = taskData.getTaskName(current);
            assertEquals(taskName, actualName);
            current = taskData.getNext(current);
        }
        
        assertFalse(current.isValid());
    }

}
