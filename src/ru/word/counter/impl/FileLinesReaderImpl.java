package ru.word.counter.impl;

import ru.word.counter.FileLinesReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLinesReaderImpl implements FileLinesReader, AutoCloseable {

    private final List<Stream<String>> streams;
    private final Queue<String> queue;

    public FileLinesReaderImpl(String[] fileNames) {
        streams = new ArrayList<>(fileNames.length);
        try {
            for (String fileName : fileNames) {
                Stream<String> stringStream = Files.lines(new File(fileName).toPath());
                streams.add(stringStream);
            }
            queue = streams.stream()
                    .reduce(Stream.empty(), Stream::concat)
                    .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String nextLine() {
        return queue.poll();
    }

    @Override
    public void close() {
        for (Stream<?> value : streams) {
            value.close();
        }
    }
}
