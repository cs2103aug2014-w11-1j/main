package manager.datamanager.suggestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.Filter.Type;
import manager.datamanager.searchfilter.KeywordFilter;
import manager.datamanager.searchfilter.SuggestionFilter;
import data.ITaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * A class that is used to find keyword suggestions.
 * 
 * It will first find the keyword with the minimum edit distance.
 * If there is a tie, it will find the keyword with the most occurrences.
 *
 */
public class SuggestionFinder {
    
    private final static Comparator<KeywordSuggestion> COMPARE_MATCH =
            new Comparator<KeywordSuggestion>() {
                public int compare(KeywordSuggestion a,
                        KeywordSuggestion b) {
                    return b.getNumberOfMatches() - a.getNumberOfMatches();
                }
            };
    ITaskData taskData;
    
    public SuggestionFinder(ITaskData taskData) {
        this.taskData = taskData;
    }
    
    Set<KeywordSuggestion> mergeKeywordList(Set<KeywordSuggestion> listOne,
            Set<KeywordSuggestion> listTwo) {
        Set<KeywordSuggestion> result = new HashSet<KeywordSuggestion>();
        if (listOne.size() == 0) {
            result.addAll(listTwo);
        } else if (listTwo.size() == 0) {
            result.addAll(listOne);
        } else {
            KeywordSuggestion keywordOne = listOne.iterator().next();
            KeywordSuggestion keywordTwo = listTwo.iterator().next();
            
            if (keywordOne.getDistance() <= keywordTwo.getDistance()) {
                result.addAll(listOne);
            }
            if (keywordOne.getDistance() >= keywordTwo.getDistance()) {
                result.addAll(listTwo);
            }
        }
        
        return result;
    }
    
    void updateKeywordList(Set<KeywordSuggestion> keywords, 
            KeywordSuggestion newKeyword) {
        if (keywords.size() == 0) {
            keywords.add(newKeyword);
        } else {
            KeywordSuggestion firstSuggestion = keywords.iterator().next();
            if (firstSuggestion.getDistance() > newKeyword.getDistance()) {
                keywords.clear();
                keywords.add(newKeyword);
            } else if (firstSuggestion.getDistance() == 
                    newKeyword.getDistance()) {
                keywords.add(newKeyword);
            }
        }
    }
    
    private int getLimit(String string) {
        return string.length() * 2 / 5;
    }
    
    private Set<KeywordSuggestion> findKeywords(String filterString, 
            String string) {
        Set<KeywordSuggestion> keywords = new HashSet<KeywordSuggestion>();
        if (string != null) {
            Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
            Matcher matcher = pattern.matcher(string);
            
            while (matcher.find()) {
                String currentWord = matcher.group();
                EditDistance editDistance = new EditDistance(filterString, 
                        currentWord);
                KeywordSuggestion newKeyword = new KeywordSuggestion(
                        currentWord, editDistance.getDistance());
                if (newKeyword.getDistance() <= getLimit(filterString)) {
                    updateKeywordList(keywords, newKeyword);
                }
            }
        }
        return keywords;
    }
    
    private Set<KeywordSuggestion> findKeywords(String filterString, 
            TaskInfo taskInfo) {
        Set<KeywordSuggestion> keywordsInName = 
                findKeywords(filterString, taskInfo.name);
        Set<KeywordSuggestion> keywordsInDetails = 
                findKeywords(filterString, taskInfo.details);
        return mergeKeywordList(keywordsInName, keywordsInDetails);
    }
    
    Set<KeywordSuggestion> findKeywords(String filterString) {
        Set<KeywordSuggestion> keywords = new HashSet<KeywordSuggestion>();
        TaskId currentId = taskData.getFirst();
        while (currentId.isValid()) {
            TaskInfo task = taskData.getTaskInfo(currentId);
            Set<KeywordSuggestion> newList = findKeywords(filterString, task);
            keywords = mergeKeywordList(keywords, newList);
            currentId = taskData.getNext(currentId);
        }
        
        return keywords;
    }
    
    void updateMatch(KeywordSuggestion keyword) {
        TaskId currentId = taskData.getFirst();
        String keywordArray[] = new String[1];
        keywordArray[0] = keyword.getKeyword();
        
        KeywordFilter filter = new KeywordFilter(keywordArray);
        while (currentId.isValid()) {
            TaskInfo task = taskData.getTaskInfo(currentId);
            if (filter.isMatching(task)) {
                int numberOfMatches = keyword.getNumberOfMatches();
                numberOfMatches++;
                keyword.setNumberOfMatches(numberOfMatches);
            }
            currentId = taskData.getNext(currentId);
        }
    }
    
    private List<KeywordSuggestion> sortKeywords(
            Set<KeywordSuggestion> keywords) {
        List<KeywordSuggestion> keywordList = 
                new ArrayList<KeywordSuggestion>(keywords);
        
        for (KeywordSuggestion keyword : keywordList) {
            updateMatch(keyword);
        }
        
        Collections.sort(keywordList, COMPARE_MATCH);
        
        return keywordList;
    }
    
    private SuggestionFilter getSuggestionFilter(String filterString) {
        Set<KeywordSuggestion> suggestions = findKeywords(filterString);
        List<KeywordSuggestion> suggestionList = sortKeywords(suggestions);
        String[] suggestionArray = new String[suggestionList.size()];
        for (int i = 0; i < suggestionList.size(); i++) {
            suggestionArray[i] = suggestionList.get(i).getKeyword();
        }
        
        SuggestionFilter suggestionFilter = 
                new SuggestionFilter(suggestionArray);
        if (suggestionArray.length == 0) {
            return null;
        }
        
        return suggestionFilter;
    }
    
    private List<SuggestionFilter> getSuggestionFilter(KeywordFilter filter) {
        String[] filterStrings = filter.getKeywords();
        List<SuggestionFilter> newFilterList = new ArrayList<SuggestionFilter>();
        for (String filterString : filterStrings) {
            SuggestionFilter suggestionFilter = getSuggestionFilter(filterString);
            if (suggestionFilter == null) {
                return null;
            }
            newFilterList.add(getSuggestionFilter(filterString));
        }
        
        return newFilterList;
    }
    
    /**
     * Generate SuggestionFilter based on the Filter[] given.
     * @param filters
     * @return The SuggestionFilter[].
     */
    public Filter[] generateSuggestionFilters(Filter[] filters) {
        List<Filter> newFilters = new ArrayList<Filter>();
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].getType() == Type.FILTER_KEYWORD) {
                List<SuggestionFilter> suggestionFilters = 
                        getSuggestionFilter((KeywordFilter)filters[i]);
                if (suggestionFilters == null)
                    return null;
                newFilters.addAll(getSuggestionFilter((KeywordFilter)filters[i]));
            } else {
                newFilters.add(filters[i]);
            }
        }
        
        Filter[] newFilterArray = new Filter[newFilters.size()];
        newFilters.toArray(newFilterArray);
        return newFilterArray;
    }
}
