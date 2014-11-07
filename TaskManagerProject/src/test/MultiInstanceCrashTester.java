package test;

import org.junit.After;
import org.junit.Test;

import test.fuzzytest.CrashTester;
import test.fuzzytest.TasklineInstanceContainer;

/**
 * Two separate instance of taskline operating on the same file.<br>
 * Used to check whether the file can be modified externally without crashing
 * taskline.
 */
//@author A0065475X
public class MultiInstanceCrashTester {
    private static final String TEST_ALIAS_FILENAME = "testAlias.txt";
    private static final String TEST_FILENAME = "testTasks.txt";

    private static final int RANDOM_SEED = 2;
    private static final float RATIO = 0.3f;
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
                createMultiInstance(TEST_FILENAME, TEST_ALIAS_FILENAME,
                        RANDOM_SEED, RATIO);
        crashTester = new CrashTester(tasklineInstanceContainer,
                RANDOM_SEED, LOG_QUEUE_LENGTH);
        
        crashTester.runTest();
    }
}
