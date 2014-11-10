package manager.datamanager.searchfilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.taskinfo.TaskInfo;

//@author A0113011L 
/**
 * A filter that is used to simulate suggestions.
 * 
 * A task matches this filter if at least one of the keywords is inside the
 * task's details or name.
 *
 */
public class SuggestionFilter implements Filter {
    String keywords[];
    
    public SuggestionFilter(String[] keywords) {
        this.keywords = keywords;
    }
    
    private boolean match(String keyword, String details) {
        if (details == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        Matcher matcher = pattern.matcher(details);
        while (matcher.find()) {
            if (keyword.toLowerCase().equals(
                    matcher.group().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the first suggestion in the suggestion list.
     * @return The first suggestion.
     */
    public String getTopSuggestion() {
        if (keywords.length == 0) {
            return null;
        } else {
            return keywords[0];
        }
    }
    
    @Override
    public Type getType() {
        return Type.FILTER_SUGGESTION;
    }

    /**
     * Check whether the task matches the filter.
     */
    @Override
    public boolean isMatching(TaskInfo task) {
        for (String keyword : keywords) {
            if (match(keyword, task.name) || match(keyword, task.details)) {
                return true;
            }
        }
        return false;
    }
    
}
