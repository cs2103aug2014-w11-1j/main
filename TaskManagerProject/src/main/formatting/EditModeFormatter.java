package main.formatting;

import java.util.ArrayList;

import main.formatting.utility.DetailsUtility;
import main.modeinfo.EditModeInfo;

/**
 * A formatter for EditMode.
 * 
 * Example:
 * You are now editing a9f.
 * 
 * Task [a9f]
 *    Name: eat apples for dinner
 *    Time: 17:30 (PM)
 *    Date: Tuesday, 17 Feb 2015
 *    Tags: taskline, food
 *    Priority: High
 *    Description: I will die if I don't eat my apples!
 * 
 *
 */

//@author A0113011L
public class EditModeFormatter {
    private final static String FORMAT_LINE = "You are now editing %1$s." + 
            System.lineSeparator();
    
    private String arrayListToString(ArrayList<String> lines) {
        StringBuilder builder = new StringBuilder("");
        for (String line : lines) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
    
    /**
     * Format an EditModeInfo to a String.
     * @param editInfo The EditModeInfo that needs to be formatted.
     * @return The formatted ModeInfo.
     */
    public String format(EditModeInfo editInfo) {
        DetailsUtility detailsUtil = new DetailsUtility();
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(String.format(FORMAT_LINE, editInfo.getTaskId()));
        ArrayList<String> detailsLines = detailsUtil.formatToArray(editInfo.getTask(), 
                editInfo.getTaskId());
        lines.addAll(detailsLines);
        return arrayListToString(lines);
    }
}
