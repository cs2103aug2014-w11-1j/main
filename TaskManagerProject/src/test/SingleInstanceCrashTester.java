package test;

import org.junit.After;
import org.junit.Test;

import test.fuzzytest.CrashTester;
import test.fuzzytest.TasklineInstanceContainer;

public class SingleInstanceCrashTester {
    private static final String TEST_ALIAS_FILENAME = "testAlias.txt";
    private static final String TEST_FILENAME = "testTasks.txt";
    
    private static final int RANDOM_SEED = 1;
    private static final int LOG_QUEUE_LENGTH = 40;

    private CrashTester crashTester;
    private TasklineInstanceContainer tasklineInstanceContainer;

    @After
    public void after() {
        crashTester.close();
        tasklineInstanceContainer.close();
    }
    
    @Test
    public void runCrashTest() {
        tasklineInstanceContainer = TasklineInstanceContainer.
                createMonoInstance(TEST_FILENAME, TEST_ALIAS_FILENAME);
        crashTester = new CrashTester(tasklineInstanceContainer,
                RANDOM_SEED, LOG_QUEUE_LENGTH);
        
        crashTester.runTest();
    }
}
