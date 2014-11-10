package test.crashtest;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

import main.MainController;
import test.crashtest.KeywordDictionary.ListType;

/**
 * Carries the framework for a crash test.
 */
//@author A0065475X
public abstract class AbstractCrashTest {
    
    private final int logQueueSize;
    private final int SIZE_INITIAL_HASHSET = 3000;

    private final int randomSeed;

    private TasklineInstanceContainer tasklineInstanceContainer;
    private KeywordDictionary keywordLibrary;
    private HashSet<String> testedStrings;
    private ArrayBlockingQueue<String> logQueue;
    private int totalStrings;
    private boolean success = false;
    
    /**
     * @param tasklineInstanceContainer contains one or more instances
     * of taskline that is used by the CrashTest.
     * @param startingSeed random seed. (for deterministic testing)
     * @param logQueueLength The maximum length of the log queue. CrashTest logs
     * input and output during the test to a queue. If the test fails, it prints
     * out the logs. Previous logs are deleted when the log queue length is
     * exceeded.
     */
    public AbstractCrashTest(TasklineInstanceContainer tasklineInstanceContainer,
            int startingSeed, int logQueueLength) {
        this.tasklineInstanceContainer = tasklineInstanceContainer;
        
        this.randomSeed = startingSeed;
        this.logQueueSize = logQueueLength;
        
    }

    public void close() {
        if (!success) {
            printLog();
        }
    }
    
    public void runTest() {
        
        keywordLibrary = new KeywordDictionary(randomSeed);
        
        testedStrings = new HashSet<>(SIZE_INITIAL_HASHSET);
        totalStrings = 0;
        logQueue = new ArrayBlockingQueue<String>(logQueueSize);

        knownCrashTest();
        fuzzyTest();
        
        System.out.println("Crash Test successful - Unique inputs: " +
                        testedStrings.size() + " out of " + totalStrings);
        success = true;
    }
    
    /**
     * Tests random strings from the KeywordLibrary.
     */
    protected abstract void fuzzyTest();
    
    /**
     * Tests strings that have been known to crash the program before
     */
    private void knownCrashTest() {

        test("edit po1");
        test("add tue 2pm tue 4pm");
        test("add today 2pm yesterday 2pm");
        test("report");
        test("add \"task\"");
        test("edit task");
        test("edit \"task\"");

        test("add meep 2pm tomorrow");
        test("add meepietwo");
        test("edit meepietwo date tomorrow");
        test("search     ");
        
        test("add yesterday 1300 0pm tomorrow");
        test("freeday today yesterday");

        test("add task 3am 29 oct 2014 3am 29 oct 2014");
        test("add sat after undone done 2pm tomorrow 2pm ##");
        
        test("alias show orange");
        test("show $4");
        test("unalias show");
    }
    
    /**
     * Tests a string made out of the specified combination of ListTypes.<br>
     * The combination is used in that order.
     * @param times the number of times to test that combination
     * @param listTypes the combination of ListTypes to use.
     */
    protected void testRandom(int times, ListType...listTypes) {
        for (int i = 0; i < times; i++) {
            testRandom(listTypes);
        }
    }
    
    /**
     * Tests a string made out of the specified combination of ListTypes.<br>
     * The combination is used in that order.<br>
     * This has a chance of searching before each test.
     * @param times the number of times to test that combination
     * @param listTypes the combination of ListTypes to use.
     */
    protected void testRandomMaybeSearch(int times, ListType...listTypes) {
        ListType[] maybeSearch = {ListType.SEARCH, ListType.NONE};
        
        for (int i = 0; i < times; i++) {
            test(keywordLibrary.getRandom(maybeSearch));
            testRandom(listTypes);
        }
    }
    
    
    protected void testRandom(ListType...listTypes) {
        StringBuilder inputString = new StringBuilder();
        
        String spacebar = "";
        for (ListType listType : listTypes) {
            inputString.append(spacebar);
            inputString.append(keywordLibrary.getRandom(listType));
            spacebar = " ";
        }
        
        test(inputString.toString());
    }

    /**
     * Tests a string made out of the specified combination of ListTypes.<br>
     * The combination is used in that order.
     * @param times the number of times to test that combination
     * @param listTypes the combination of ListTypes to use.
     */
    protected void testRandom(int times, ListType[]...listTypes) {
        for (int i = 0; i < times; i++) {
            testRandom(listTypes);
        }
    }

    /**
     * Tests a string made out of the specified combination of ListTypes.<br>
     * The combination is used in that order.<br>
     * This has a chance of searching before each test.
     * @param times the number of times to test that combination
     * @param listTypes the combination of ListTypes to use.
     */
    protected void testRandomMaybeSearch(int times, ListType[]...listTypes) {
        ListType[] maybeSearch = {ListType.SEARCH, ListType.NONE};
        
        for (int i = 0; i < times; i++) {
            test(keywordLibrary.getRandom(maybeSearch));
            testRandom(listTypes);
        }
    }
    
    
    protected void testRandom(ListType[]...listTypes) {
        StringBuilder inputString = new StringBuilder();
        
        String spacebar = "";
        for (ListType[] listType : listTypes) {
            inputString.append(spacebar);
            inputString.append(keywordLibrary.getRandom(listType));
            spacebar = " ";
        }
        
        test(inputString.toString());
    }
    
    /**
     * Tests a specific string by sending it into the taskline instance.
     * @param input the user input to test. e.g. "add orange juice 2pm"
     */
    protected void test(String input) {
        totalStrings++;
        testedStrings.add(input);
        
        if (totalStrings%100 == 0) {
            System.out.println("Tested: " + testedStrings.size() + " / " +
                    totalStrings + " tested");
        }
        
        try {
            MainController mainController = getNextMainController();
            log("[" + getInstanceIndex() + "] Input: " + input);
            String result = mainController.runCommand(input);
            log(result);
        } catch (Exception e) {
            System.out.println("Exception Thrown!");
            System.out.println(" Attempted Input: [" + input + "]");
            printLog();
            e.printStackTrace();
            throw e;
        }
    }
    
    private void log(String result) {
        if (logQueue.remainingCapacity() <= 0) {
            logQueue.poll();
        }
        logQueue.offer(result);
    }

    private void printLog() {
        while (!logQueue.isEmpty()) {
            System.out.println("*******");
            System.out.println(logQueue.poll());
        }
    }
    
    private MainController getNextMainController() {
        return tasklineInstanceContainer.getNextInstance();
    }
    
    private int getInstanceIndex() {
        return tasklineInstanceContainer.indexOfLastInstance();
    }
}
