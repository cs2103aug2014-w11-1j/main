package test;

import org.junit.After;
import org.junit.Test;

import test.crashtest.AbstractCrashTest;
import test.crashtest.MultiInstanceCrashTest;
import test.crashtest.TasklineInstanceContainer;

//@author A0065475X
/**
 * Two separate instance of taskline operating on the same file.<br>
 * Used to check whether the file can be modified externally without crashing
 * taskline.<br>
 * It does this by creating two independent instances of Taskline which operate
 * on the same files “testFile.txt” and “testAlias.txt” and sends random input
 * to one of the two, picked randomly, to see if they are able to cause each
 * other to crash.
 */
public class MultiInstanceCrashTester {
    private static final String TEST_ALIAS_FILENAME = "testAlias.txt";
    private static final String TEST_FILENAME = "testTasks.txt";

    private static final int RANDOM_SEED = 3;
    private static final float RATIO = 0.3f;
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
                createMultiInstance(TEST_FILENAME, TEST_ALIAS_FILENAME,
                        RANDOM_SEED, RATIO);
        crashTester = new MultiInstanceCrashTest(tasklineInstanceContainer,
                RANDOM_SEED, LOG_QUEUE_LENGTH);
        
        crashTester.runTest();
    }
}
