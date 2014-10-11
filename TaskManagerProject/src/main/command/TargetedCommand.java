package main.command;

import java.util.LinkedList;

import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import data.TaskId;

public abstract class TargetedCommand extends Command {
    
    private static final char DELIMITER_WHITESPACE = ' ';
    private static final char DELIMITER_COMMA = ',';
    private static final char DELIMITER_DASH = '-';
    private static final String DELIMITER_DASH_STRING = "-";
    
    private final SearchManager searchManager;
    private final StateManager stateManager;
    protected TaskIdSet targetTaskIdSet;
    
    
    public TargetedCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
    }

    public void setTargetSet(){}

    public String tryParseIdsIntoSet(String args) {
        
        targetTaskIdSet = new TaskIdSet();
        try {
            String remainingArgs = parseIdsIntoSet(args);
            return remainingArgs;
            
        } catch (IllegalArgumentException e) {
            targetTaskIdSet = null;
            return args;
        }
    }
    
    private String parseIdsIntoSet(String args) throws IllegalArgumentException{
        int currentIndex = 0;
        int tokenStart = currentIndex;

        boolean readingCharacters = false;
        boolean lastTokenHadDelimiter = true;
        LinkedList<String> idStrings = new LinkedList<>();
        
        while (currentIndex < args.length()) {
            char c = args.charAt(currentIndex);
            
            if (c == DELIMITER_COMMA) {
                lastTokenHadDelimiter = true;
                idStrings.add(args.substring(tokenStart, currentIndex));
                tokenStart = currentIndex+1;
                readingCharacters = false;
                
            } else if (c == DELIMITER_DASH) {
                lastTokenHadDelimiter = true;
                readingCharacters = false;
                
            } else if (c == DELIMITER_WHITESPACE) {
                readingCharacters = false;
                
            } else {
                if (!readingCharacters) {
                    readingCharacters = true;
                    
                    if (lastTokenHadDelimiter) {
                        lastTokenHadDelimiter = false;
                    } else {
                        break;
                    }
                }
            }
            
            currentIndex++;
        }

        idStrings.add(args.substring(tokenStart, currentIndex));
        parseStringsIntoSet(idStrings);
        return args.substring(currentIndex);
    }
    
    private void parseStringsIntoSet(LinkedList<String> idStrings) {
        for (String idString : idStrings) {
            ensureIdStringLength(idString);
            
            String[] args = idString.split(DELIMITER_DASH_STRING);
            if (args.length == 1) {
                addToSet(args[0]);
            } else if (args.length == 2) {
                addToSet(args[0], args[1]);
            } else {
                throw new IllegalArgumentException("Invalid string");
            }
        }
    }

    private void ensureIdStringLength(String idString) {
        if (idString.length() == 0 ||
                idString.charAt(idString.length()-1) == DELIMITER_DASH) {
            throw new IllegalArgumentException("Invalid string");
        }
    }

    private void addToSet(String arg) {
        TaskId taskId = parseTaskId(arg.trim());
        if (taskId == null) {
            throw new IllegalArgumentException("Invalid task Id");
        }
        targetTaskIdSet.add(taskId);
    }
    
    private void addToSet(String arg1, String arg2) {
        int rangeStart, rangeEnd;
        
        try {
            rangeStart = Integer.parseInt(arg1.trim());
            rangeEnd = Integer.parseInt(arg2.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid string");
        }
        
        if (rangeEnd < rangeStart) {
            throw new IllegalArgumentException("Invalid range");
        }
        
        for (int i = rangeStart; i <= rangeEnd; i++) { 
            TaskId taskId = retrieveRelativeTaskId(i);
            if (taskId == null) {
                throw new IllegalArgumentException("Invalid range");
            }
            targetTaskIdSet.add(taskId);
        }
    }

    protected void parseAsSearchString(String searchString) {
        
    }

    /**
     * 
     * @param args
     * @return null iff invalid task Id.
     */
    protected TaskId parseTaskId(String args) {
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return null;
        }
        
        try {
            int relativeTaskId = Integer.parseInt(args);
            return retrieveRelativeTaskId(relativeTaskId);
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private TaskId retrieveRelativeTaskId(int relativeTaskId) {
        if (stateManager.inSearchMode()) {
            return searchManager.getAbsoluteIndex(relativeTaskId);
        } else {
            return null;
        }
    }
    
    
}
