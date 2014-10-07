package main.formatting;

import java.util.ArrayList;

import main.modeinfo.EditModeInfo;

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
