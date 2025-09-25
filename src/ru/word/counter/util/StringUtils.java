package ru.word.counter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private final Pattern pattern;

    public StringUtils(int min, int max) {
        pattern = Pattern.compile("\\p{L}{" + min + "," + max + "}");
    }

    public static String trimToNull(String string) {
        String trimmed = string == null ? null : string.trim();
        return (trimmed == null || trimmed.isEmpty())
                ? null
                : trimmed;
    }

    public List<String> parseWords(String word) {
        Matcher matcher = pattern.matcher(word);
        List<String> strings = new ArrayList<>();
        while (matcher.find()){
            strings.add(matcher.group().toLowerCase());
        }
        return strings;
    }
}
