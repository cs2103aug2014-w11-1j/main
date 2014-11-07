package test.fuzzytest;

import test.fuzzytest.KeywordLibrary.ListType;

public class SingleInstanceCrashTest extends AbstractCrashTest {

    public SingleInstanceCrashTest(
            TasklineInstanceContainer tasklineInstanceContainer,
            int startingSeed, int logQueueLength) {
        super(tasklineInstanceContainer, startingSeed, logQueueLength);
    }

    @Override
    protected void fuzzyTest() {
        ListType[] edit = {ListType.EDIT};
        ListType[] add = {ListType.ADD};
        ListType[] delete = {ListType.DELETE};

        ListType[] clear = {ListType.CLEAR};

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
                validItems, validItems, validItems);

        testRandom(30, ListType.UNALIAS, ListType.COMMAND);
        
        for (int i = 0 ; i < 100; i++) {
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(delete, validTargets, validTargets, validTargets);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(edit, validTargets, editKeywords, dateTime);
            testRandom(ListType.SEARCH, ListType.RANDOM);
            testRandom(edit, validTargets, validTargets, editKeywords, dateTime);
        }

        testRandom(10, ListType.FREEDAY, ListType.DATETIME);
        testRandom(10, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME);
        testRandom(10, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME,
                ListType.DATETIME);
        testRandom(10, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME,
                ListType.DATETIME, ListType.DATETIME);
        testRandom(40, ListType.FREEDAY, ListType.ALL, ListType.ALL,
                ListType.ALL, ListType.ALL);

        testRandom(30, ListType.FREETIME, ListType.DATETIME);
        testRandom(10, ListType.FREETIME, ListType.DATETIME, ListType.DATETIME);
        testRandom(40, ListType.FREETIME, ListType.ALL, ListType.ALL);
        
        testRandomMaybeSearch(30, ListType.DELETE, ListType.ALL);
        testRandomMaybeSearch(30, ListType.DELETE, ListType.RANDOM);
        testRandomMaybeSearch(30, ListType.DELETE, ListType.TASKID);
        
        
        testRandomMaybeSearch(200, ListType.ALL, ListType.ALL);
        testRandom(30, ListType.UNALIAS, ListType.COMMAND);
        
        testRandom(ListType.SEARCH);

        testRandom(150, ListType.COMMAND, ListType.ALL);
        testRandom(150, ListType.COMMAND, ListType.ALL, ListType.ALL);
        testRandom(150, ListType.COMMAND, ListType.ALL, ListType.ALL, ListType.ALL);
        testRandom(30, ListType.UNALIAS, ListType.COMMAND);

        testRandom(100, ListType.SEARCH, ListType.ALL);
        testRandom(50, ListType.SEARCH, ListType.RANDOM);
        testRandom(50, ListType.SEARCH, ListType.RANDOM, ListType.RANDOM);
        testRandom(50, ListType.SEARCH, ListType.RANDOM, ListType.RANDOM, ListType.RANDOM);
        testRandom(200, ListType.SEARCH, ListType.ALL, ListType.ALL, ListType.ALL);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.RANDOM, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.CONNECTOR, ListType.DATETIME);
        testRandom(50, ListType.SEARCH, ListType.DATETIME, ListType.SYMBOL, ListType.DATETIME);

        testRandomMaybeSearch(20, ListType.EDIT, ListType.EDITKEYWORD);
        testRandomMaybeSearch(20, ListType.EDIT, ListType.EDITKEYWORD, ListType.RANDOM);
        testRandomMaybeSearch(20, ListType.EDIT, ListType.RANDOM, ListType.RANDOM);
        
        testRandomMaybeSearch(120, edit, validTargets, clear, editKeywords);
        testRandomMaybeSearch(200, edit, validTargets, editKeywords, validItems);
        testRandomMaybeSearch(30, edit, validTargets, clear, editKeywords);
        testRandomMaybeSearch(200, edit, validTargets, validTargets, editKeywords,
                validItems);
        testRandomMaybeSearch(200, edit, validTargets, validTargets, validTargets,
                editKeywords, validItems,
                validItems);
        testRandomMaybeSearch(200, edit, randomTargets, randomTargets, randomTargets,
                editKeywords, randomItems, randomItems);

        // Random aliasing
        for (int i = 0; i < 3; i++) {
            testRandom(20, ListType.ALIAS, ListType.ALL, ListType.COMMAND,
                    ListType.ALL);
            testRandom(20, ListType.ALIAS, ListType.ALL, ListType.ALL,
                    ListType.ALL);
    
            testRandom(20, ListType.ALL, ListType.ALL);
            testRandom(20, ListType.ALL, ListType.ALL, ListType.ALL);
            testRandom(20, ListType.ALL, ListType.ALL, ListType.ALL,
                    ListType.ALL);
        }

        testRandom(50, ListType.COMMAND, ListType.ALL);
    }
    
    

}
