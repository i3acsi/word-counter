package ru.word.counter.util;

import java.util.Map;
import java.util.Optional;

public class BooleanUtils {

    private static final Map<String, Boolean> BOOLEAN_MAP = Map.of(
            "true", true,
            "t", true,
            "yes", true,
            "y", true,
            "1", true
    );

    public static boolean parseBoolean(String value) {
        return Optional.ofNullable(value)
                .map(String::toLowerCase)
                .map(BOOLEAN_MAP::get)
                .orElse(false);
    }
}
