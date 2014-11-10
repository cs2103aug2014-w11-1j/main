package main.command;

import java.util.LinkedList;

import main.command.parser.CommandParser;
import main.response.Response;
import manager.ManagerHolder;
import manager.datamanager.SearchManager;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.KeywordFilter;
import manager.result.Result;
import manager.result.SearchResult;
import data.TaskId;

//@author A0065475X
/**
 * Any command that "acts" on existing tasks should extend TargetedCommand.<br>
 * TargetedCommand gives the functionality to parse task numbers, absolute task
 * Ids, and to act on tasks by specifying keywords.
 * (e.g. delete orange juice)
 * Examples: edit, delete.
 */
public abstract class TargetedCommand extends Command {

    private static final char DELIMITER_WHITESPACE = ' ';
    private static final char DELIMITER_COMMA = ',';
    private static final char DELIMITER_DASH = '-';
    private static final String DELIMITER_DASH_STRING = "-";

    private final SearchManager searchManager;
    
    protected TaskIdSet targetTaskIdSet;
    protected KeywordFilter keywordFilter;

    public TargetedCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();
    }

    @Override
    public Response execute() {
        if (targetTaskIdSet == null) {
            if (keywordFilter == null) {
                return updateInvalidArguments();
            } else {
                return keywordFilterExecute();
            }
        } else {
            assert keywordFilter == null;
            return super.execute();
        }
    }

    /**
     * Add target taskIds to a stored TargetedCommand, so that the command
     * will be executed on these taskIds.
     * @param taskIds the taskIds to be added.
     */
    public void setTargets(TaskIdSet taskIdSet){
        this.targetTaskIdSet = taskIdSet;
    }

    /**
     * Attempts to parse a String containing Task IDs into the targetTaskIdSet.
     * targetTaskIdSet will be set to null if this is unsuccessful.
     * @param args A string that possibly specifies task IDs. The task IDs
     * should be at the front of the string.
     * @return The remaining string that after cutting out the task ids.<br>
     * returns the original string if parsing is unsuccessful.
     */
    protected String tryParseIdsIntoSet(String args) {
        targetTaskIdSet = new TaskIdSet();
        try {
            String remainingArgs = parseIdsIntoSet(args);
            return remainingArgs;

        } catch (IllegalArgumentException e) {
            targetTaskIdSet = null;
            return args;
        }
    }

    /**
     * Parse a string as a substitute for Task IDs and make this command
     * execute a search instead, and store itself in StateManager for later
     * execution by the confirmation.
     * @param searchString
     */
    protected void parseAsSearchString(String searchString) {
        assert targetTaskIdSet == null : "targetTaskIdSet needs to be null";

        searchString = CommandParser.stripIgnoreSymbols(searchString);
        if (!searchString.isEmpty()) {
            String[] keywords = searchString.split(" ");
            keywordFilter = new KeywordFilter(keywords);
        }
    }

    /**
     * @param args String form of task ID. Can be relative or absolute.
     * @return a TaskId object corresponding to the relative or absolute id.<br>
     * null if it is unsuccessful. (e.g. not in search mode for relative ids).
     */
    private TaskId parseTaskId(String args) {
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return null;
        }

        try {
            int relativeTaskId = Integer.parseInt(args);
            return retrieveAbsoluteTaskId(relativeTaskId);
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            TaskId taskId = TaskId.makeTaskId(absoluteTaskId);
            if (taskId != null && searchManager.getTaskInfo(taskId) != null) {
                return taskId;
            } else {
                return null;
            }
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
                idStrings.add(args.substring(tokenStart, currentIndex));
                tokenStart = currentIndex+1;
                lastTokenHadDelimiter = true;
                readingCharacters = false;

            } else if (c == DELIMITER_DASH) {
                lastTokenHadDelimiter = true;
                readingCharacters = false;

            } else if (c == DELIMITER_WHITESPACE) {
                readingCharacters = false;

            } else {
                if (!readingCharacters && !lastTokenHadDelimiter) {
                    break;
                }
                lastTokenHadDelimiter = false;
                readingCharacters = true;
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
            TaskId taskId = retrieveAbsoluteTaskId(i);
            if (taskId == null) {
                throw new IllegalArgumentException("Invalid range");
            }
            targetTaskIdSet.add(taskId);
        }
    }

    private TaskId retrieveAbsoluteTaskId(int relativeTaskId) {
        if (stateManager.canQuerySearchManager()) {
            try {
                TaskId result = searchManager.getAbsoluteIndex(relativeTaskId);
                return result;
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * This is used to execute the command using a keyword filter instead
     * of a target TaskIdSet. (aka delete by name)
     * @return
     */
    private Response keywordFilterExecute() {
        if (isCommandAllowed() && stateManager.canSearch()) {
            Filter[] filters = new Filter[]{keywordFilter};
            keywordFilter = null;

            Result result = searchManager.searchTasksWithoutSplit(filters);
            Response response = null;
            if (onlyOneSearchResult(result)) {
                response = executeOnSearchResult(result);
            } else {
                response = stateManager.updateAndStoreCommand(result, this);
            }
            return response;

        } else {
            return updateCannotExecuteCommand();
        }
    }

    private boolean onlyOneSearchResult(Result result) {
        if (result.getType() != Result.Type.SEARCH_SUCCESS) {
            return false;
        }
        SearchResult searchResult = (SearchResult)result;
        return searchResult.onlyOneSearchResult();
    }

    private Response executeOnSearchResult(Result result) {
        assert result.getType() == Result.Type.SEARCH_SUCCESS;
        SearchResult searchResult = (SearchResult)result;

        targetTaskIdSet = new TaskIdSet();
        targetTaskIdSet.add(searchResult.getOnlySearchResult());
        keywordFilter = null;

        return execute();
    }
}
