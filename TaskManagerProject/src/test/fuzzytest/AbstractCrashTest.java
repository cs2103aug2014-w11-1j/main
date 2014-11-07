package test.fuzzytest;

import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

import main.MainController;
import test.fuzzytest.KeywordLibrary.ListType;

/**
 * Tests a bunch of random inputs to see if anything crashes the program.
 */
//@author A0065475X
public abstract class AbstractCrashTest {
    
    private final int logQueueSize;
    private final int SIZE_INITIAL_HASHSET = 3000;

    private final int randomSeed;

    private TasklineInstanceContainer tasklineInstanceContainer;
    private KeywordLibrary keywordLibrary;
    private HashSet<String> testedStrings;
    private ArrayBlockingQueue<String> logQueue;
    private int totalStrings;
    private boolean success = false;
    
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
        
        keywordLibrary = new KeywordLibrary(randomSeed);
        
        testedStrings = new HashSet<>(SIZE_INITIAL_HASHSET);
        totalStrings = 0;
        logQueue = new ArrayBlockingQueue<String>(logQueueSize);

        knownCrashTest();
        fuzzyTest();
        
        System.out.println("Crash Test successful - Unique strings: " +
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
    
    protected void test(String input) {
        totalStrings++;
        testedStrings.add(input);
        
        if (totalStrings%100 == 0) {
            System.out.println("Tested: " + testedStrings.size() + " / " +
                    totalStrings);
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
