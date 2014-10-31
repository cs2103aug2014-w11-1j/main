package main.command.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.command.EditCommand;
import main.command.TargetedCommand;
import main.command.TaskIdSet;
import main.response.Response;
import manager.ManagerHolder;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.KeywordFilter;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class EditCommandTest {

    @Test
    public void test() {
        ManagerHolder managerHolder = new StubManagerHolder();
        StubEditManager editManager = (StubEditManager)managerHolder.getEditManager();
        StubStateManager stateManager = (StubStateManager)managerHolder.getStateManager();

        stateManager.canEdit = true;
        stateManager.inEditMode = false;
        
        TaskInfo editTaskInfo = TaskInfo.createEmpty();
        
        // Expected: rename 1 to orange.
        executeEdit("1 name orange", managerHolder);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1);
        editTaskInfo.name = "orange";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: rename 1, 4-7 to orange.
        executeEdit("1 , 4 - 7 name orange", managerHolder);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 4, 5, 6, 7);
        editTaskInfo.name = "orange";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: start edit mode on 1, 4-7
        executeEdit("1 , 4 - 7", managerHolder);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.START_EDIT_MODE, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 4, 5, 6, 7);
        assertNull(editManager.lastTaskInfo);
        assertNull(editManager.lastTags);

        
        // Expected: invalid arguments.
        executeEdit("2, 4 name", managerHolder);
        assertInvalidArguments(stateManager);


        // Expected: invalid arguments.
        executeEdit("2 oraerkj", managerHolder);
        assertInvalidArguments(stateManager);
        

        // Expected: invalid arguments.
        executeEdit("2 time", managerHolder);
        assertInvalidArguments(stateManager);
        
        // Expected: invalid arguments.
        executeEdit("", managerHolder);
        assertInvalidArguments(stateManager);

        // Expected: search "name orange" and try to start edit mode.
        executeEdit("name orange", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 2, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.START_EDIT_MODE, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        assertNull(editManager.lastTaskInfo);
        assertNull(editManager.lastTags);
        

        // Expected: search "name 1 orange" and try to start edit mode.
        executeEdit("name 1 orange", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 2, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.START_EDIT_MODE, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        assertNull(editManager.lastTaskInfo);
        assertNull(editManager.lastTags);

        
        // Expected: search "name" and try to add the tag #name.
        executeEdit("name tag add name", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 2, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.ADD_TASK_TAGS, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        assertNull(editManager.lastTaskInfo);
        assertTags(editManager, "name");

        
        // Expected: delete tag #name from tasks 1 to 3.
        executeEdit("3 - 4 tag delete d", managerHolder);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.DELETE_TASK_TAGS, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 3, 4);
        assertNull(editManager.lastTaskInfo);
        assertTags(editManager, "d");


        
        // Expected: store command - change details to "status priority"
        executeEdit("name details status priority", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 4, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 3, 4);
        editTaskInfo.details = "status priority";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: store command - change status to undone.
        executeEdit("priority status undone", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 3, 4
        executeStoredCommand(stateManager, managerHolder, 1, 4, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 3, 4);
        editTaskInfo.status = Status.UNDONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: store command - change priority to high.
        executeEdit("tag priority high", managerHolder);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] start edit mode on tasks 1, 5
        executeStoredCommand(stateManager, managerHolder, 1, 5);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 5);
        editTaskInfo.priority = Priority.HIGH;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();



        // Expected: search "done" and try to mark it
        executeEdit("done", managerHolder, EditCommand.ParseType.MARK);
        assertStoreCommand(stateManager);


        // Expected: set status of 1 to done.
        executeEdit("1 done", managerHolder, EditCommand.ParseType.MARK);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1);
        editTaskInfo.status = Status.DONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();


        // Expected: set status of 1-3 to done.
        executeEdit("1-3", managerHolder, EditCommand.ParseType.MARK);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        editTaskInfo.status = Status.DONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();


        // Expected: set status of 1, 3 to undone.
        executeEdit("1, 3", managerHolder, EditCommand.ParseType.UNMARK);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 3);
        editTaskInfo.status = Status.UNDONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();



        // Expected: search for "meep" and store command.
        executeEdit("meep", managerHolder, EditCommand.ParseType.UNMARK);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] unmark tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 2, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        editTaskInfo.status = Status.UNDONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: search for "done" and store command.
        executeEdit("done", managerHolder, EditCommand.ParseType.UNMARK);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] unmark tasks 1, 2, 3
        executeStoredCommand(stateManager, managerHolder, 1, 2, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2, 3);
        editTaskInfo.status = Status.UNDONE;
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        
        // Expected: invalid command (trying to unmark 1, 2 as done)
        executeEdit("1, 2 done", managerHolder, EditCommand.ParseType.UNMARK);
        assertInvalidArguments(stateManager);


        // Expected: Reschedule tasks 1, 2 to 2pm.
        executeEdit("1, 2 2pm", managerHolder, EditCommand.ParseType.RESCHEDULE);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2);
        editTaskInfo.endTime = LocalTime.of(14, 0);
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

        // Expected: Store command: reschedule command "orange" to 2pm 14 oct 2012.
        executeEdit("orange 2pm 14 Oct 2012", managerHolder, EditCommand.ParseType.RESCHEDULE);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] reschedule 1 to 2pm 14 oct 2012.
        executeStoredCommand(stateManager, managerHolder, 1);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1);
        editTaskInfo.endTime = LocalTime.of(14, 0);
        editTaskInfo.endDate = LocalDate.of(2012, 10, 14);
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();


        // Expected: Store command: rename task "rename" to rename.
        executeEdit("rename rename", managerHolder, EditCommand.ParseType.RENAME);
        assertStoreCommand(stateManager);

        // Expected: [Stored Command] rename task "rename" to rename.
        executeStoredCommand(stateManager, managerHolder, 1);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1);
        editTaskInfo.name = "rename";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();

    }
    
    @Test
    public void invertedCommaTest() {
        ManagerHolder managerHolder = new StubManagerHolder();
        StubEditManager editManager = (StubEditManager)managerHolder.getEditManager();
        StubStateManager stateManager = (StubStateManager)managerHolder.getStateManager();
        StubSearchManager searchManager = (StubSearchManager)managerHolder.getSearchManager();

        stateManager.canEdit = true;
        stateManager.inEditMode = false;
        
        TaskInfo editTaskInfo = TaskInfo.createEmpty();
        

        // Expected: Store command: search for 1
        executeEdit("\"1\" name 2", managerHolder);
        assertStoreCommand(stateManager);
        assertLastSearch(searchManager, "1");

        // Expected: [Stored Command] rename 3, 4 to "2".
        executeStoredCommand(stateManager, managerHolder, 3, 4);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 3, 4);
        editTaskInfo.name = "2";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();


        // Expected: Store command: start edit mode on "1 name"
        executeEdit("\"1 name\"", managerHolder);
        assertStoreCommand(stateManager);
        assertLastSearch(searchManager, "1", "name");

        // Expected: [Stored Command] start edit mode on "1 name" for 1,3.
        executeStoredCommand(stateManager, managerHolder, 1, 3);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.START_EDIT_MODE, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 3);
        assertNull(editManager.lastTaskInfo);
        assertNull(editManager.lastTags);


        // Expected: Invalid command
        executeEdit("1 \"name\"", managerHolder);
        assertInvalidArguments(stateManager);


        // Expected: Invalid command
        executeEdit("1, 2 \"name\"", managerHolder);
        assertInvalidArguments(stateManager);


        // Expected: Rename 1, 2 to orange juice
        executeEdit("1, 2 name \"orange juice\"", managerHolder);
        assertNormalExecution(stateManager);
        assertEquals(StubEditManager.Method.EDIT_TASK, editManager.lastMethodCall);
        assertTaskNumbers(editManager, 1, 2);
        editTaskInfo.name = "orange juice";
        assertEquals(editTaskInfo, editManager.lastTaskInfo);
        editTaskInfo = TaskInfo.createEmpty();


        // Expected: Invalid arguments
        executeEdit("1, 2 time \"tuesday\"", managerHolder);
        assertInvalidArguments(stateManager);
        
    }
    
    private void assertLastSearch(StubSearchManager searchManager,
            String...keywords) {
        Filter[] filters = searchManager.filters;
        assertEquals(1, filters.length);
        KeywordFilter keywordFilter = (KeywordFilter)filters[0];
        List<String> searchKeywords = Arrays.asList(keywordFilter.getKeywords());
        
        for (String keyword : keywords) {
            assertTrue(searchKeywords.contains(keyword));
        }
        assertEquals(keywords.length, searchKeywords.size());
    }
    
    
    private void assertInvalidArguments(StubStateManager stateManager) {
        assertEquals(StubStateManager.Update.INVALID_ARGUMENTS,
                stateManager.getLastUpdate());
    }
    
    private void assertNormalExecution(StubStateManager stateManager) {
        assertEquals(StubStateManager.Update.NORMAL_EXECUTION,
                stateManager.getLastUpdate());
    }
    
    private void assertStoreCommand(StubStateManager stateManager) {
        assertEquals(StubStateManager.Update.STORE_COMMAND,
                stateManager.getLastUpdate());
    }
    
    private void assertTaskNumbers(StubEditManager editManager, int... ids) {
        TaskIdSet taskIdSet = editManager.lastTaskIdSet;
        for (int id : ids) {
            TaskId taskId = new TaskId(id);
            assertTrue(taskIdSet.contains(taskId));
        }
        assertEquals(taskIdSet.size(), ids.length);
    }
    
    private void assertTags(StubEditManager editManager, String... tagStrings) {
        Tag[] tags = editManager.lastTags;
        ArrayList<Tag> tagList = new ArrayList<>(tagStrings.length);
        for (String tagString : tagStrings) {
            tagList.add(new Tag(tagString));
        }
        
        for (Tag tag : tags) {
            assertTrue("last tags do not contain " + tag, tagList.contains(tag));
        }
        
        assertEquals(tags.length, tagList.size());
    }
    
    private Response executeStoredCommand(StubStateManager stateManager,
            ManagerHolder managerHolder, int... ids) {
        TargetedCommand command = stateManager.getStoredCommand();
        assertNotNull(command);

        TaskIdSet taskIdSet = new TaskIdSet();
        for (int id : ids) {
            TaskId taskId = new TaskId(id);
            taskIdSet.add(taskId);
        }
        command.setTargets(taskIdSet);
        
        return command.execute();
    }
    
    private Response executeEdit(String args, ManagerHolder managerHolder) {
        EditCommand command = new EditCommand(args, managerHolder);
        return command.execute();
    }
    
    private Response executeEdit(String args, ManagerHolder managerHolder,
            EditCommand.ParseType parseType) {
        EditCommand command = new EditCommand(args, managerHolder, parseType);
        return command.execute();
    }
    

}
