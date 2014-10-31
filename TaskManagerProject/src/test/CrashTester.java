package test;

import io.FileInputOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

import main.MainController;
import main.command.alias.AliasStorage;
import manager.ManagerHolder;

import org.junit.After;
import org.junit.Test;

import test.fuzzytest.KeywordLibrary;
import test.fuzzytest.KeywordLibrary.ListType;
import data.TaskData;

/**
 * Tests a bunch of random inputs to see if anything crashes the program.
 * @author Oh
 */
public class CrashTester {
    private static final int SIZE_LOGQUEUE = 200;
    private static final int SIZE_INITIAL_HASHSET = 1000;
    
    private static final String TEST_FILENAME = "testTasks.txt";
    private static final int RANDOM_SEED = 1;

    private MainController mainController;
    private KeywordLibrary keywordLibrary;
    private HashSet<String> testedStrings;
    private ArrayBlockingQueue<String> logQueue;
    private int totalStrings;

    @After
    public void after() {
        deleteTestFile();
    }
    
    @Test
    public void initialiseTest() {

        String fileName = TEST_FILENAME;
        deleteTestFile();

        String aliasFileName = "testAlias.txt";
        AliasStorage aliasStorage = new AliasStorage(aliasFileName);

        TaskData taskData = new TaskData();
        FileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName);
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput);
        mainController = new MainController(managerHolder, aliasStorage);
        keywordLibrary = new KeywordLibrary(RANDOM_SEED);
        
        testedStrings = new HashSet<>(SIZE_INITIAL_HASHSET);
        totalStrings = 0;
        logQueue = new ArrayBlockingQueue<String>(SIZE_LOGQUEUE);

        knownCrashTest();
        fuzzyTest();
        
        System.out.println("Crash Test successful - Unique strings: " +
                        testedStrings.size() + " out of " + totalStrings);
    }
    
    /**
     * Tests random strings from the KeywordLibrary.
     */
    private void fuzzyTest() {
        ListType[] edit = {ListType.EDIT};
        ListType[] add = {ListType.ADD};
        ListType[] delete = {ListType.DELETE};

        ListType[] dateTime = {ListType.DATETIME};
        ListType[] comma = {ListType.COMMA};
        
        ListType[] editKeywords = {ListType.EDITKEYWORD};
        ListType[] validTargets = {ListType.VALIDNUMBER, ListType.NONE,
                ListType.VALIDTASKID};
        ListType[] validItems = {ListType.DATETIME, ListType.ITEM};

        ListType[] randomTargets = {ListType.NUMBER, ListType.NONE, ListType.TASKID};
        ListType[] randomItems = {ListType.DATETIME, ListType.ITEM, ListType.NONE,
                ListType.RANDOM, ListType.SYMBOL, ListType.CONNECTOR};

        testRandom(30, ListType.ADD, ListType.ALL);
        testRandom(30, ListType.ADD, ListType.RANDOM, ListType.ITEM);
        testRandom(60, ListType.ADD, ListType.DATETIME, ListType.DATETIME,
                ListType.DATETIME, ListType.DATETIME);
        testRandom(30, add, randomItems, randomItems, randomItems,
                randomItems, randomItems, randomItems);

        
        testRandom(30, ListType.DELETE, ListType.ALL);
        testRandom(30, ListType.DELETE, ListType.RANDOM);
        testRandom(30, ListType.DELETE, ListType.TASKID);
        
        for (int i = 0 ; i < 200; i++) {
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets, comma, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets, validTargets, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(edit, validTargets, editKeywords, dateTime);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(edit, validTargets, validTargets, editKeywords, dateTime);
        }
        
        System.out.println(mainController.runCommand("show"));
        
        testRandom(100, ListType.ALL, ListType.ALL);
        
        testRandom(ListType.SEARCH);

        testRandom(100, ListType.COMMAND, ListType.ALL);
        testRandom(100, ListType.COMMAND, ListType.ALL, ListType.ALL);
        testRandom(100, ListType.COMMAND, ListType.ALL, ListType.ALL, ListType.ALL);

        testRandom(100, ListType.SEARCH, ListType.ALL);
        testRandom(50, ListType.SEARCH, ListType.RANDOM);
        testRandom(50, ListType.SEARCH, ListType.RANDOM, ListType.RANDOM);
        testRandom(50, ListType.SEARCH, ListType.RANDOM, ListType.RANDOM, ListType.RANDOM);
        testRandom(200, ListType.SEARCH, ListType.ALL, ListType.ALL, ListType.ALL);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.RANDOM, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.CONNECTOR, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.SYMBOL, ListType.DATETIME);

        testRandom(20, ListType.EDIT, ListType.EDITKEYWORD, ListType.RANDOM);
        testRandom(20, ListType.EDIT, ListType.RANDOM, ListType.RANDOM);
        testRandom(200, new ListType[]{ListType.EDIT}, validTargets,
                validTargets, validTargets, validTargets,
                editKeywords, validItems);
        testRandom(200, new ListType[]{ListType.EDIT}, validTargets,
                validTargets, validTargets, validTargets, validTargets,
                editKeywords, validItems, validItems);
        testRandom(200, new ListType[]{ListType.EDIT}, randomTargets,
                randomTargets, randomTargets, randomTargets,
                editKeywords, randomItems, randomItems);

    }
    
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
    }
    
    /**
     * Tests a string made out of the specified combination of ListTypes.<br>
     * The combination is used in that order.
     * @param times the number of times to test that combination
     * @param listTypes the combination of ListTypes to use.
     */
    private void testRandom(int times, ListType...listTypes) {
        for (int i = 0; i < times; i++) {
            testRandom(listTypes);
        }
    }
    
    private void testRandom(ListType...listTypes) {
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
    private void testRandom(int times, ListType[]...listTypes) {
        for (int i = 0; i < times; i++) {
            testRandom(listTypes);
        }
    }
    
    private void testRandom(ListType[]...listTypes) {
        StringBuilder inputString = new StringBuilder();
        
        String spacebar = "";
        for (ListType[] listType : listTypes) {
            inputString.append(spacebar);
            inputString.append(keywordLibrary.getRandom(listType));
            spacebar = " ";
        }
        
        test(inputString.toString());
    }
    
    private void test(String input) {
        totalStrings++;
        if (testedStrings.contains(input)) {
            return;
        } else {
            testedStrings.add(input);
        }
        
        try {
            log("Input: " + input);
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
    
    private void terminateAndPrintLog() {
        printLog();
        System.exit(0);
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
    private void deleteTestFile() {
        try {
            Path path = Paths.get(TEST_FILENAME);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
