package main.formatting.utility;

/**
 * Utility to colorize text. Works by adding the corresponding ANSI escape 
 * sequences.
 */

//@author A0113011L
public class ColorUtility {
    public enum Color {
        BLACK(0),
        RED(1),
        GREEN(2),
        YELLOW(3),
        BLUE(4),
        MAGENTA(5),
        CYAN(6),
        WHITE(7);
        
        private final int value;
        
        private Color(int value) {
            this.value = value;
        }
        public int getValue() {
            return this.value;
        }
    };
    
    private final static String PREFIX = "\u001b[3%1$dm";
    private final static String SUFFIX = "\u001b[0m";
    
    /**
     * Adds the corresponding ANSI escape sequence to colorize the string.
     * @param s The string to be colorized.
     * @param color The color to be appliaed on the s.
     * @return The colorized String.
     */
    public String colorize(String s, Color color) {
        if (color == Color.WHITE)
            return s;
        String prefix = String.format(PREFIX, color.getValue());
        
        String suffix = SUFFIX;
        
        return prefix + s + suffix;
    }
}
