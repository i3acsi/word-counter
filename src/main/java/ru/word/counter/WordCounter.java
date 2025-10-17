package ru.word.counter;

import java.util.Map;

public interface WordCounter {

    void applyWord(String word);

    Map<String, Long> countTopTen();

    long countTotal();
}
