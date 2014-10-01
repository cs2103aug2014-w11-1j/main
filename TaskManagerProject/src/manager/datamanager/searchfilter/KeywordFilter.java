package manager.datamanager.searchfilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.taskinfo.TaskInfo;

public class KeywordFilter implements Filter{
    public Type getType() {
        return Type.FILTER_KEYWORD;
    }
    
    private String[] keywords;
    
    public KeywordFilter(String[] keywords) {
        this.keywords = keywords;
    }
    
    public boolean match(String keyword, String details) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]*");
        Matcher matcher = pattern.matcher(details);
        while (matcher.find()) {
            if (keyword.equals(matcher.group())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean filter(TaskInfo task) {
        for (String keyword : keywords) {
            if (!match(keyword, task.details)) {
                return false;
            }
        }
        return true;
    }
}