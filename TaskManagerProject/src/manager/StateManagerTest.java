package manager;

import static org.junit.Assert.assertEquals;
import io.FileInputOutput;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.result.Result;
import manager.result.Result.Type;
import manager.result.SearchResult;
import manager.result.SimpleResult;

import org.junit.Test;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class StateManagerTest {
	
		
	/*@Test
	public void addTest(){
		TaskInfo testTaskInfo1 = new TaskInfo();
		testTaskInfo1.name = "Test1";
		testTaskInfo1.details = "No details";
		TeskInfo 
		
		StateManagerWithoutFileForTest stateManager =
				new StateManagerWithoutFileForTest(null, null);
		AddResult addResult = new AddResult(Result.Type.ADD_SUCCESS, testTaskInfo1, taskId)
	}*/
	
	

    @Test
    public void test() {
        TestOutput testOutput = new TestOutput();
        
        StubUndoManager undoManager = new StubUndoManager(testOutput);
        StubSearchManager searchManager = new StubSearchManager(testOutput);
        StubFileInputOutput fileInputOutput = new StubFileInputOutput(testOutput);
        
        StateManager stateManager = new StateManager(fileInputOutput, undoManager,
                    searchManager);

        Result result;
        
        result = new SimpleResult(Type.ADD_FAILURE);
        stateManager.beforeCommandExecutionUpdate();
        assertEquals("read\n", testOutput.getOutputAndClear());
        stateManager.update(result);
        
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
    public Result redoLastSearch() {
        testOutput.writeOutput("redo search");
        return null;
    }

    @Override
    public SearchResult getLastSearchResult() {
        TaskInfo[] tasks = new TaskInfo[0];
        TaskId[] taskIds = new TaskId[0];
        return new SearchResult(Result.Type.SEARCH_SUCCESS, tasks, taskIds);
    }
    
    
    
}


class StubFileInputOutput extends FileInputOutput {
    private final TestOutput testOutput;

    public StubFileInputOutput(TestOutput testOutput) {
        super(null, "testFile.txt");
        this.testOutput = testOutput;
    }

    @Override
    public boolean read() {
        testOutput.writeOutput("read");
        return true;
    }

    @Override
    public boolean write() {
        testOutput.writeOutput("write");
        return true;
    }
    
}
