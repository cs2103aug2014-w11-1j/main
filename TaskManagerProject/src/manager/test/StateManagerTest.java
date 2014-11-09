package manager.test;

import static org.junit.Assert.assertEquals;
import io.IFileInputOutput;
import manager.StateManager;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.datamanager.searchfilter.Filter;
import manager.result.Result;
import manager.result.Result.Type;
import manager.result.SearchResult;
import manager.result.SimpleResult;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class StateManagerTest {
	
    @Test
    public void test() {
        TestOutput testOutput = new TestOutput();
        
        StubUndoManager undoManager = new StubUndoManager(testOutput);
        StubSearchManager searchManager = new StubSearchManager(testOutput);
        StubFileInputOutput fileInputOutput =
                new StubFileInputOutput(testOutput, "taskData");
        StubFileInputOutput aliasFileInputOutput =
                new StubFileInputOutput(testOutput, "alias");
        
        StateManager stateManager = new StateManager(fileInputOutput,
                aliasFileInputOutput, undoManager, searchManager);

        Result result;
        
        result = new SimpleResult(Type.ADD_FAILURE);
        stateManager.beforeCommandUpdate();
        assertEquals("taskData read\n", testOutput.getOutputAndClear());
        stateManager.update(result);
        assertEquals("update undo\n"
                + "alias write\n"
                + "taskData write\n",
                testOutput.getOutputAndClear());
    }
}

class TestOutput {
    private StringBuilder output;
    
    public TestOutput() {
        output = new StringBuilder();
    }
    
    public String getOutputAndClear() {
        String temp = output.toString();
        output = new StringBuilder();
        return temp;
    }
    
    public void writeOutput(String message) {
        output.append(message).append("\n");
    }
}


class StubUndoManager extends UndoManager {
    private final TestOutput testOutput;
    
    public StubUndoManager(TestOutput testOutput) {
        super(null);
        this.testOutput = testOutput;
    }
    
    @Override
    public void updateUndoHistory() {
        testOutput.writeOutput("update undo");
    }
    
}


class StubSearchManager extends SearchManager {
    private final TestOutput testOutput;

    public StubSearchManager(TestOutput testOutput) {
        super(null);
        this.testOutput = testOutput;
    }

    @Override
    public Result searchTasks(Filter[] filters) {
        testOutput.writeOutput("do a search");
        return null;
    }

    @Override
    public SearchResult getLastSearchResult() {
        TaskInfo[] tasks = new TaskInfo[0];
        TaskId[] taskIds = new TaskId[0];
        return new SearchResult(tasks, taskIds, null);
    }
    
}


class StubFileInputOutput implements IFileInputOutput {
    private final TestOutput testOutput;
    private final String prefix;

    public StubFileInputOutput(TestOutput testOutput, String prefix) {
        this.testOutput = testOutput;
        this.prefix = prefix;
    }

    @Override
    public boolean read() {
        testOutput.writeOutput(prefix + " read");
        return true;
    }

    @Override
    public boolean write() {
        testOutput.writeOutput(prefix + " write");
        return true;
    }
    
}
