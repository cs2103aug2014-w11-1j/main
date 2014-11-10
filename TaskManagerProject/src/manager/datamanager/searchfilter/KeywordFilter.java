package manager.datamanager.searchfilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A filter that matches tasks based on keywords.
 * 
 * A Task matches a keyword if the keyword is a word in either the name
 * or the description.
 * 
 * A Task matches the filter if it matches every single keyword in the filter.
 */
public class KeywordFilter implements Filter{
    public Type getType() {
        return Type.FILTER_KEYWORD;
    }
    
    private String[] keywords;
    
    public KeywordFilter(String[] keywords) {
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
     * Check whether the task matches the filter.
     */
    public boolean isMatching(TaskInfo task) {
        for (String keyword : keywords) {
            if (!match(keyword, task.details) && !match(keyword, task.name)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return the keywords of this filter.
     * @return
     */
    public String[] getKeywords() {
        return keywords;
    }
}
