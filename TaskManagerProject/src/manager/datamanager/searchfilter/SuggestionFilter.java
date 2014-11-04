package manager.datamanager.searchfilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.taskinfo.TaskInfo;

//@author A0113011L
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

    @Override
    public boolean filter(TaskInfo task) {
        for (String keyword : keywords) {
            if (match(keyword, task.name) || match(keyword, task.details)) {
                return true;
            }
        }
        return false;
    }
    
}
