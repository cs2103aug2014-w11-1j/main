package main.formatting;

import main.modeinfo.EmptyModeInfo;

/**
 * A formatter for the EmptyModeInfo.
 * @author nathanajah
 *
 */
public class EmptyModeFormatter {
	
	/**
	 * Returns an empty String, since the ModeInfo is empty.
	 * @param emptyInfo
	 * @return an empty String.
	 */
    public String format(EmptyModeInfo emptyInfo) {
        return "";
    }
}
