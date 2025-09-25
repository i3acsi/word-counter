package ru.word.counter.impl;

import ru.word.counter.WordCounter;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WordCounterImpl implements WordCounter {

    private final Map<String, Long> wordCountMap;

    private final static Comparator<Map.Entry<String, Long>> WORD_COUNT_COMPARATOR =
            (e1, e2) -> e2.getValue().compareTo(e1.getValue());

    public WordCounterImpl() {
        wordCountMap = new ConcurrentHashMap<>();
    }

    public void applyWord(String word) {
        wordCountMap.compute(word, (k, v) -> {
            if (v == null) {
                return 1L;
            } else {
                return v + 1L;
            }
        });
    }

    @Override
    public Map<String, Long> countTopTen() {
        return wordCountMap.entrySet().stream()
                .sorted(WORD_COUNT_COMPARATOR)
                .limit(100)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
