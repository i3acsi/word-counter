package ru.word.counter.impl;

import ru.word.counter.LinesProcessor;
import ru.word.counter.util.ThreadUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class LinesProcessorImpl implements LinesProcessor {

    private final List<Thread> lineThreads;
    private final Consumer<String> wordConsumer;
    private final Supplier<String> linesSupplier;
    private final int min;
    private final int max;

    private static final char MINUS = '-';
    private static final Pattern PATTERN = Pattern.compile("[\\p{L}-]+");

    public LinesProcessorImpl(Supplier<String> linesSupplier,
                              Consumer<String> wordConsumer,
                              int threadsCount, int min, int max) {
        this.wordConsumer = wordConsumer;
        this.linesSupplier = linesSupplier;
        this.lineThreads = ThreadUtils.startThreads(threadsCount, this::startProcessLines);
        this.min = min;
        this.max = max;

    }

    private void startProcessLines() {
        String line = linesSupplier.get();
        while (line != null) {
            PATTERN
                    .matcher(line)
                    .results()
                    .map(MatchResult::group)
                    .map(s -> strip(s, MINUS))
                    .filter(f -> (f.length() >= min && f.length() <= max))
                    .map(String::toLowerCase)
                    .forEach(wordConsumer);
            line = linesSupplier.get();
        }
    }

    @Override
    public void processLines() {
        try {

            for (Thread thread : lineThreads) {
                thread.join(Duration.of(30, ChronoUnit.MINUTES).toMillis());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private String strip(String text, char sym) {
        int firstNs = 0;
        int lastS = text.length();
        for (int i = 0; i < lastS; i++) {
            if (text.charAt(i) == sym) {
                firstNs = i + 1;
            } else {
                break;
            }
        }
        if (firstNs == lastS) {
            return "";
        }
        for (int i = lastS - 1; i >= 0; i--) {
            if (text.charAt(i) == sym) {
                lastS = i;
            } else {
                break;
            }
        }
        return firstNs == 0 && lastS == text.length() ?
                text : text.substring(firstNs, lastS);
    }
}
