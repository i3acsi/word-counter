package ru.word.counter.impl;

import ru.word.counter.FileLinesReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileLinesReaderImpl implements FileLinesReader {

    private final List<Stream<String>> streams;
    private final Queue<String> queue;

    public FileLinesReaderImpl(List<Path> filePaths) {
        streams = new ArrayList<>();
        try {
            for (Path file : filePaths) {
                Stream<String> stringStream = Files.lines(file);
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
