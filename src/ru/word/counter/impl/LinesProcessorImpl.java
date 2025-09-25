package ru.word.counter.impl;

import ru.word.counter.LinesProcessor;
import ru.word.counter.util.StringUtils;
import ru.word.counter.util.ThreadUtils;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LinesProcessorImpl implements LinesProcessor {

    private final List<Thread> lineThreads;
    private final List<Thread> wordThreads;
    private final Consumer<String> wordConsumer;
    private final Supplier<String> linesSupplier;
    private final StringUtils stringUtils;

    private static final BlockingQueue<String> WORDS_Q = new LinkedBlockingQueue<>();

    public LinesProcessorImpl(Supplier<String> linesSupplier,
                              Consumer<String> wordConsumer,
                              int threadsCount, int min, int max) {
        this.wordConsumer = wordConsumer;
        this.stringUtils = new StringUtils(min, max);
        this.linesSupplier = linesSupplier;
        this.lineThreads = ThreadUtils.startThreads(threadsCount, this::startProcessLines);
        this.wordThreads = ThreadUtils.startThreads(threadsCount, this::processWord);

    }

    private void startProcessLines() {
        String line = linesSupplier.get();
        while (line != null) {
            Arrays.stream(line.split("[^\\p{L}]+"))
                    .map(StringUtils::trimToNull)
                    .filter(Objects::nonNull)
                    .forEach(this::offer);
            line = linesSupplier.get();
        }
    }

    private void processWord() {
        String word = poll();
        while (word != null) {
            stringUtils.parseWords(word).stream()
                    .filter(Objects::nonNull)
                    .forEach(wordConsumer);
            word = poll();
        }
    }

    @Override
    public void processLines() {
        try {
            for (Thread thread : wordThreads) {
                thread.join(Duration.of(30, ChronoUnit.MINUTES));
            }
            for (Thread thread : lineThreads) {
                thread.join(Duration.of(30, ChronoUnit.MINUTES));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private String poll() {
        try {
            return WORDS_Q.poll(1, TimeUnit.of(ChronoUnit.SECONDS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private void offer(String word) {
        try {
            if (!WORDS_Q.offer(word, 1, TimeUnit.SECONDS)) {
                throw new RuntimeException("failed to offer next word to queue");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
