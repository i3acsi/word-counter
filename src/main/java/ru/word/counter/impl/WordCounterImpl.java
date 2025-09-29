package ru.word.counter.impl;

import ru.word.counter.WordCounter;
import ru.word.counter.util.Counter;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class WordCounterImpl implements WordCounter {

    private final static Comparator<Map.Entry<String, Counter>> WORD_COUNT_COMPARATOR =
            (e1, e2) -> Long.compare(e2.getValue().getCount(), e1.getValue().getCount());
    private final Map<String, Counter> wordCountMap;

    public WordCounterImpl() {
        wordCountMap = new ConcurrentHashMap<>();
    }

    public void applyWord(String word) {
        wordCountMap.compute(word, (k, v) -> {
            if (v == null) {
                return new Counter();
            } else {
                return v.incrementAndGet();
            }
        });
    }

    @Override
    public Map<String, Long> countTopTen() {
        return wordCountMap.entrySet().stream()
                .sorted(WORD_COUNT_COMPARATOR)
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getCount(), (e1, e2) -> {
                    throw new RuntimeException();
                }, LinkedHashMap::new));
    }
}
