package ru.word.counter.util;

public class WordUtils {

    private static final String MINUS = "-";
    private final String regexp;

    public WordUtils(int min, int max) {
        regexp = "[\\p{L}-]{" + min + "," + max + "}";
    }

    public static String trimToNull(String string) {
        if (string == null) {
            return null;
        }
        String trimmed = string.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public String extractValidWord(String word) {
        return word.matches(regexp)
                ? format(word)
                : null;
    }

    private String format(String s) {
        return filterMinus(s.toLowerCase());
    }

    private String filterMinus(String s) {
        if (s.startsWith(MINUS)) {
            return s.substring(1);
        } else if (s.endsWith(MINUS)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
}
