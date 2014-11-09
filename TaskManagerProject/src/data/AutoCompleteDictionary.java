package data;

import jline.SimpleCompletor;

/**
 * A dictionary that keeps an updated list of strings that can be
 * auto-completed.
 */
//@author A0113011L
public class AutoCompleteDictionary {
    
    SimpleCompletor completor;
    
    public AutoCompleteDictionary(SimpleCompletor completor) {
        this.completor = completor;
    }
    
    public void refreshDictionary(String[] strings) {
        completor.setCandidateStrings(strings);
    }
}
