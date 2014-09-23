package data;

public class TaskId implements Comparable<TaskId> {
    
    /*
     * Condition: TRANSLATE_PRIME must be coprime to MAX_ID.
     * We can ensure this by simply meeting the below two conditions:
     * 1) TRANSLATE_PRIME > MAX_ID,
     * 2) TRANSLATE_PRIME is prime.
     */
    private static final int TRANSLATE_PRIME = 363767;
    /**
     * TRANSLATE_REVERSE_PRIME should be computed using the Euclidean Algorithm.
     */
    private static final int TRANSLATE_REVERSE_PRIME = 16823;
    private static final int TRANSLATE_SHIFT = 4789;
    public static final int MAX_ID = 20280;
    
    
    public final int id;
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskId other = (TaskId) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    public TaskId(int id) {
        this.id = id;
    }
    
    public static String toStringId(int indexId) {
        if (indexId >= MAX_ID)
            throw new IllegalArgumentException("index ID exceeds range");
        
        int translatedIndex = numberTranslateForward(indexId);
        
        int numberPosition = translatedIndex%3;
        translatedIndex /= 3;
        int number = translatedIndex%10;
        translatedIndex /= 10;
        char character1 = (char)(translatedIndex%26 + 'a');
        translatedIndex /= 26;
        char character2 = (char)(translatedIndex + 'a');
        
        String result = "";
        if (numberPosition == 0) {
            result = "" + number + character1 + character2;
            
        } else if (numberPosition == 1) {
            result = "" + character1 + number + character2;
            
        } else if (numberPosition == 2) {
            result = "" + character1 + character2 + number;
            
        }
        
        return result;
    }
    
    public static int toIntId(String stringId) {
        int numberPosition;
        int number;
        char character1;
        char character2;

        if (isDigit(stringId.charAt(0))) {
            
            number = (int)(stringId.charAt(0) - '0');
            character1 = stringId.charAt(1);
            character2 = stringId.charAt(2);
            numberPosition = 0;
            
        } else if (isDigit(stringId.charAt(1))) {
            
            number = (int)(stringId.charAt(1) - '0');
            character1 = stringId.charAt(0);
            character2 = stringId.charAt(2);
            numberPosition = 1;
            
        } else if (isDigit(stringId.charAt(2))) {
            
            number = (int)(stringId.charAt(2) - '0');
            character1 = stringId.charAt(0);
            character2 = stringId.charAt(1);
            numberPosition = 2;
            
        } else {
            throw new IllegalArgumentException("Invalid string input: " + stringId);
        }
        
        int result = (int)(character2 - 'a');
        result *= 26;
        result += (int)(character1 - 'a');
        result *= 10;
        result += number;
        result *= 3;
        result += numberPosition;
        
        result = numberTranslateInverse(result);
        
        return result;
    }
    
    private static boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }
    
    public static int numberTranslateForward(int index) {
        long longIndex = index;
        longIndex += TRANSLATE_SHIFT;
        longIndex *= TRANSLATE_PRIME;
        longIndex += TRANSLATE_SHIFT;
       
        longIndex %= MAX_ID;
        if (longIndex < 0) {
            longIndex += MAX_ID;
        }
        return (int)longIndex;
    }

    public static int numberTranslateInverse(int index) {
        long longIndex = index;
        longIndex -= TRANSLATE_SHIFT;
        longIndex *= TRANSLATE_REVERSE_PRIME;
        longIndex -= TRANSLATE_SHIFT;
        
        longIndex %= MAX_ID;
        if (longIndex < 0) {
            longIndex += MAX_ID;
        }
        return (int)longIndex;
    }

    @Override
    public int compareTo(TaskId o) {
        return id - o.id;
    }
    
    
}
