package test.crashtest;

import test.crashtest.KeywordDictionary.ListType;

/**
 * The crash test configuration for a multi-instance crash test. Focus is on
 * checking whether the other taskline functions properly when one of them is
 * constantly adding and deleting tasks.
 */
//@author A0065475X
public class MultiInstanceCrashTest extends AbstractCrashTest {

    public MultiInstanceCrashTest(
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
        
        for (int i = 0; i <12; i++) {
            testRandom(3, ListType.ADD, ListType.ALL);
            testRandom(3, ListType.ADD, ListType.RANDOM, ListType.ITEM);
            testRandom(3, ListType.ADD, ListType.DATETIME, ListType.DATETIME,
                    ListType.DATETIME, ListType.DATETIME);
            testRandom(3, add, randomItems, randomItems, randomItems,
                    validItems, validItems, validItems);
    
            testRandomMaybeSearch(5, ListType.ALL, ListType.ALL);
            
            testRandomMaybeSearch(3, ListType.DELETE, ListType.ALL);
            testRandomMaybeSearch(3, ListType.DELETE, ListType.RANDOM);
            testRandomMaybeSearch(3, ListType.DELETE, ListType.TASKID);
    
            testRandom(delete, validTargets);
            testRandom(delete, validTargets, validTargets);
            testRandom(delete, validTargets, validTargets, validTargets);
            testRandom(edit, validTargets, editKeywords, dateTime);
            testRandom(edit, validTargets, validTargets, editKeywords, dateTime);
    
            testRandom(ListType.ADD, ListType.ALL);
            testRandomMaybeSearch(1, ListType.DELETE, ListType.ALL);
            
            testRandomMaybeSearch(3, ListType.ALL, ListType.ALL);
    
            testRandom(2, ListType.SEARCH, ListType.ALL, ListType.ALL);
            
            testRandom(1, ListType.FREEDAY, ListType.DATETIME);
            testRandom(1, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME);
            testRandom(1, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME,
                    ListType.DATETIME);
    
            testRandom(3, ListType.SEARCH, ListType.ALL, ListType.ALL);
            
            testRandom(1, ListType.FREEDAY, ListType.DATETIME, ListType.DATETIME,
                    ListType.DATETIME, ListType.DATETIME);
            testRandom(2, ListType.FREEDAY, ListType.ALL, ListType.ALL,
                    ListType.ALL, ListType.ALL);
    
            testRandomMaybeSearch(5, ListType.ALL, ListType.ALL);
    
            testRandom(ListType.ADD, ListType.ALL);
            testRandomMaybeSearch(1, ListType.DELETE, ListType.ALL);
            
            testRandom(1, ListType.FREETIME, ListType.DATETIME);
            testRandom(1, ListType.FREETIME, ListType.DATETIME, ListType.DATETIME);
            testRandom(2, ListType.FREETIME, ListType.ALL, ListType.ALL);
            
            testRandomMaybeSearch(5, ListType.ALL, ListType.ALL);
            testRandom(3, ListType.UNALIAS, ListType.COMMAND);
            
            testRandom(ListType.SEARCH);
    
            testRandom(2, ListType.SEARCH, ListType.ALL);
    
            testRandom(2, ListType.COMMAND, ListType.ALL);
            testRandom(2, ListType.COMMAND, ListType.ALL, ListType.ALL);
            testRandom(2, ListType.COMMAND, ListType.ALL, ListType.ALL, ListType.ALL);
            testRandom(3, ListType.UNALIAS, ListType.COMMAND);
    
    
            testRandomMaybeSearch(2, ListType.EDIT, ListType.EDITKEYWORD);
            
            testRandom(3, ListType.SEARCH, ListType.ALL, ListType.ALL);
            
            testRandomMaybeSearch(2, ListType.EDIT, ListType.EDITKEYWORD, ListType.RANDOM);
            testRandomMaybeSearch(2, ListType.EDIT, ListType.RANDOM, ListType.RANDOM);
            
            testRandomMaybeSearch(6, edit, validTargets, clear, editKeywords);
            testRandomMaybeSearch(2, edit, validTargets, editKeywords, validItems);
            testRandomMaybeSearch(3, edit, validTargets, clear, editKeywords);
            testRandomMaybeSearch(2, edit, validTargets, validTargets, editKeywords,
                    validItems);
            testRandomMaybeSearch(2, edit, validTargets, validTargets, validTargets,
                    editKeywords, validItems,
                    validItems);
            testRandomMaybeSearch(2, edit, randomTargets, randomTargets, randomTargets,
                    editKeywords, randomItems, randomItems);

            testRandomMaybeSearch(15, ListType.DELETE, ListType.ALL);
            testRandomMaybeSearch(15, ListType.DELETE, ListType.RANDOM);
            testRandomMaybeSearch(15, ListType.DELETE, ListType.TASKID);
        }

        // Random aliasing
        for (int i = 0; i < 80; i++) {
            testRandom(3, ListType.ALIAS, ListType.ALL, ListType.COMMAND,
                    ListType.ALL);
            testRandom(2, ListType.COMMAND, ListType.ALL);
            
            testRandom(3, ListType.ALIAS, ListType.ALL, ListType.ALL,
                    ListType.ALL);

            testRandom(3, ListType.UNALIAS, ListType.ALL);
    
            testRandom(3, ListType.ALL, ListType.ALL);
            testRandom(3, ListType.ALL, ListType.ALL, ListType.ALL);
            testRandom(3, ListType.ALL, ListType.ALL, ListType.ALL,
                    ListType.ALL);
        }
    }

}
