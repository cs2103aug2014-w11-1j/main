package main.formatting;

import main.formatting.utility.SummaryUtility;
import main.modeinfo.SearchModeInfo;

/**
 * Formatter for SearchModeInfo.
 * Example : 
 * Thu, 15 Nov 2014 ---                        
 * 1) [   11:20   ] Eat an apple today                                      - [f8g]
 * Thu, 17 Nov 2014 ---                                
 * 2) [12:20-13:30] Prepare apples for dinner                               - [fu5]
 * 3) [   17:30   ] Eat apples for dinner                                   - [a9f]
 * 4) [   21:10   ] Go to the supermarket and shop for some apples for p... - [gf4]
 * Floating Tasks ---
 * 5) [           ] Remember to eat an apple today                          - [a8d]
 */

//@author A0113011L
public class SearchModeFormatter {
    private final static String LINE_SUGGESTION = "Did you mean: ";
    
    private SummaryUtility summaryUtility;
    
    public SearchModeFormatter() {
        summaryUtility = new SummaryUtility();
    }

    private String getSuggestionLine(String[] suggestions) {
        assert suggestions != null;
        StringBuilder builder = new StringBuilder(LINE_SUGGESTION);
        builder.append(String.join(",", suggestions));
        builder.append("?");
        return builder.toString();
    }
    
    /**
     * Format the SearchModeInfo to a String.
     * @param modeInfo
     * @return
     */
    public String format(SearchModeInfo modeInfo) {
        StringBuilder result = new StringBuilder();
        if (modeInfo.getTasks().length != 0 && modeInfo.getSuggestions() != null) {
            result.append(getSuggestionLine(modeInfo.getSuggestions()));
            result.append(System.lineSeparator());
        }
        result.append(summaryUtility.format(modeInfo.getTasks(), 
                modeInfo.getTaskIds(), true));
        result.append(System.lineSeparator());
        return result.toString();
    }
}
