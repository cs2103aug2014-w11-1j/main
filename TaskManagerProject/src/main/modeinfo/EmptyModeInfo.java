package main.modeinfo;

//@author A0113011L
/**
 * A ModeInfo that is used when the user is in the normal mode, without
 * any specific mode to print out.
 *
 */
public class EmptyModeInfo implements ModeInfo {

	/**
	 * Get the Type of the ModeInfo, which is Type.EMPTY_MODE.
	 */
    public Type getType() {
        return Type.EMPTY_MODE;
    }

}
