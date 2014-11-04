package data;

import java.util.Arrays;

import jline.SimpleCompletor;

public class AutoCompleteDictionary {
    
    SimpleCompletor completor;
    
    public AutoCompleteDictionary(SimpleCompletor completor) {
        this.completor = completor;
    }
    
    //@author A0113011L
    public void refreshDictionary(String[] strings) {
        completor.setCandidateStrings(strings);
    }
}
