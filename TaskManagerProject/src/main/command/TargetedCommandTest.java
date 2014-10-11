package main.command;

import static org.junit.Assert.assertEquals;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import manager.result.Result;

import org.junit.Test;

import data.TaskId;

public class TargetedCommandTest {

    @Test
    public void test() {
        StubTargetedCommand command = new StubTargetedCommand();

        String args;
        String result;

        // Valid inputs
        args = "1,2 , 3, 4 , 5-8 , 9 - 10   , 11   oran";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2,3,4,5,6,7,8,9,10,11", command.toString());
        assertEquals("oran", result);

        args = "  2    ,  4 -    5   , 555 -  558 3333 3333 , 3333";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("2,4,5,555,556,557,558", command.toString());
        assertEquals("3333 3333 , 3333", result);

        args = "1,2,3,4,3-6 7";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2,3,4,5,6", command.toString());
        assertEquals("7", result);

        args = "1- 2   oran";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2", command.toString());
        assertEquals("oran", result);

        args = "1 -3   oran";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2,3", command.toString());
        assertEquals("oran", result);

        args = "1- 4   ";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2,3,4", command.toString());
        assertEquals("", result);

        args = "   1  , 4   ";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,4", command.toString());
        assertEquals("", result);

        args = " 1";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1", command.toString());
        assertEquals("", result);

        args = " 1 2";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1", command.toString());
        assertEquals("2", result);

        args = " 1,2";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("1,2", command.toString());
        assertEquals("", result);

        args = "12";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("12", command.toString());
        assertEquals("", result);

        args = "3,3,4";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("3,4", command.toString());
        assertEquals("", result);

        args = "3,3-3,4 3";
        result = command.tryParseIdsIntoSet(args);
        assertEquals("3,4", command.toString());
        assertEquals("3", result);
        
        
        
        // Invalid inputs
        assertInvalidInput("3,3-3,e 3", command);
        assertInvalidInput("3 , - 3 a", command);
        assertInvalidInput("3,,3", command);
        assertInvalidInput("3,3--3", command);
        assertInvalidInput("1-1000,", command);
        assertInvalidInput("1-1000,-,", command);
        assertInvalidInput("-", command);
        assertInvalidInput(",", command);
        assertInvalidInput(",3", command);
        assertInvalidInput("-3", command);
        assertInvalidInput("3-", command);
        assertInvalidInput("3,", command);
        assertInvalidInput("3-2", command);
        assertInvalidInput("--", command);
        assertInvalidInput(",,", command);
        assertInvalidInput("1,e,2,3,4", command);
        assertInvalidInput("e", command);
        assertInvalidInput("!", command);
        assertInvalidInput("3,%", command);
        assertInvalidInput("$-1", command);
        assertInvalidInput("4-p", command);
        assertInvalidInput("2, 3, 4, 4 - p 4", command);
        assertInvalidInput("1, 2, 4 - 6 - 8", command);
        assertInvalidInput("1, 2, 2-4-5", command);
        assertInvalidInput("1-4-2", command);
        assertInvalidInput("1 -, 3", command);

    }

    private void assertInvalidInput(String args, StubTargetedCommand command) {
        String result = command.tryParseIdsIntoSet(args);
        assertEquals(null, command.getTargetIdSet());
        assertEquals(args, result);
    }

}

class StubTargetedCommand extends TargetedCommand {

    public StubTargetedCommand() {
        super(new StubManagerHolder());
    }

    @Override
    protected boolean isValidArguments() {
        return false;
    }

    @Override
    protected boolean isCommandAllowed() {
        return false;
    }

    @Override
    protected Result executeAction() {
        return null;
    }
    
    public String toString() {
        return targetTaskIdSet.numericIdString();
    }
    
    public TaskIdSet getTargetIdSet() {
        return targetTaskIdSet;
    }
    
}

class StubManagerHolder extends ManagerHolder {

    public StubManagerHolder() {
        super(null, null);
    }

    @Override
    public SearchManager getSearchManager() {
        return new StubSearchManager();
    }

    @Override
    public StateManager getStateManager() {
        return new StubStateManager();
    }
    
}

class StubSearchManager extends SearchManager {

    public StubSearchManager() {
        super(null);
    }

    public TaskId getAbsoluteIndex(int relativeTaskId) {
        return new TaskId(relativeTaskId);
    }
}

class StubStateManager extends StateManager {

    public StubStateManager() {
        super(null, null, null);
    }

    public boolean inSearchMode() {
        return true;
    }
    
}