package manager.datamanager.suggestion;

//@author A0113011L
/**
 * A class that is used to calculate the edit distance between two Strings.
 * 
 * The edit distance calculated is the Levenshtein distance.
 * It is defined as the minimum number of moves needed to change the first 
 * string to the second string, where each move one of :
 * - Removing a character to the string
 * - Adding a character to the string
 * - Changing a character in the string to another character.
 * 
 */
public class EditDistance {
    private int dpTable[][];
    private boolean calculated[][];
    
    String stringOne, stringTwo;
    
    private int calculate(int indexOne, int indexTwo) {
        if (indexOne == 0) {
            return indexTwo;
        } else if (indexTwo == 0) {
            return indexOne;
        } else if (calculated[indexOne][indexTwo]) {
                return dpTable[indexOne][indexTwo];
        } else {
            int currentAnswer;
            if (isMatching(indexOne, indexTwo)) {
                currentAnswer = calculateMatch(indexOne, indexTwo);
            } else {
                currentAnswer = calculateChange(indexOne, indexTwo);
            }
            
            currentAnswer = Math.min(currentAnswer, calculateInsert(indexOne, indexTwo));
            currentAnswer = Math.min(currentAnswer, calculateDelete(indexOne, indexTwo));
            
            updateDpTable(indexOne, indexTwo, currentAnswer);
            return currentAnswer;
        }
    }

    private void updateDpTable(int indexOne, int indexTwo, int currentAnswer) {
        calculated[indexOne][indexTwo] = true;
        dpTable[indexOne][indexTwo] = currentAnswer;
    }
    
    private boolean isMatching(int indexOne, int indexTwo) {
        return stringOne.charAt(indexOne - 1) == stringTwo.charAt(indexTwo - 1);
    }
    
    private int calculateInsert(int indexOne, int indexTwo) {
        return calculate(indexOne, indexTwo - 1) + 1;
    }
    
    private int calculateDelete(int indexOne, int indexTwo) {
        return calculate(indexOne - 1, indexTwo) + 1;
    }
    
    private int calculateChange(int indexOne, int indexTwo) {
        return calculate(indexOne - 1, indexTwo - 1) + 1;
    }
    
    private int calculateMatch(int indexOne, int indexTwo) {
        assert isMatching(indexOne, indexTwo);
        return calculate(indexOne - 1, indexTwo - 1);
    }
    
    /**
     * Create a EditDistance object that calculates the edit distance between
     * stringOne and stringTwo.
     * @param stringOne The first string.
     * @param stringTwo The second string.
     */
    public EditDistance(String stringOne, String stringTwo) {
        assert stringOne != null;
        assert stringTwo != null;
        dpTable = new int[stringOne.length() + 1][stringTwo.length() + 1];
        calculated = new boolean[stringOne.length() + 1][stringTwo.length() + 1];
        
        this.stringOne = stringOne;
        this.stringTwo = stringTwo;
    }
    
    /**
     * Get the edit distance between stringOne and stringTwo.
     * @return The edit distance.
     */
    public int getDistance() {
        return calculate(stringOne.length(), stringTwo.length());
    }
}
