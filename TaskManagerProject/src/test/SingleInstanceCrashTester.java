package test;

import org.junit.After;
import org.junit.Test;

import test.crashtest.AbstractCrashTest;
import test.crashtest.SingleInstanceCrashTest;
import test.crashtest.TasklineInstanceContainer;

//@author A0065475X
/**
 * The original crash tester. Tests a bunch of random input, together with some
 * fixed input which have been known to crash the program before, to see if any
 * of the input causes the program to crash (exception / assertion) or infinite
 * loop.
 */
public class SingleInstanceCrashTester {
    private static final String TEST_ALIAS_FILENAME = "testAlias.txt";
    private static final String TEST_FILENAME = "testTasks.txt";
    
    private static final int RANDOM_SEED = 1;
    private static final int LOG_QUEUE_LENGTH = 40;

    private AbstractCrashTest crashTester;
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
        crashTester = new SingleInstanceCrashTest(tasklineInstanceContainer,
                RANDOM_SEED, LOG_QUEUE_LENGTH);
        
        crashTester.runTest();
    }
}
