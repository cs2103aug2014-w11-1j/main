package main.formatting;

import main.modeinfo.EditModeInfo;

public class EditModeFormatter {
    private final static String FORMAT_LINE = "You are now editing %1$s." + 
            System.lineSeparator();
    
    public String format(EditModeInfo editInfo) {
        return String.format(FORMAT_LINE, editInfo.getTask().name);
    }
}
