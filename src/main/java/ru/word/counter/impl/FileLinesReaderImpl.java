package ru.word.counter.impl;

import ru.word.counter.FileLinesReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class FileLinesReaderImpl implements FileLinesReader {

    private final Stream<String> combined;
    private final Iterator<String> iterator;

    public FileLinesReaderImpl(List<Path> filePaths) {
        this.combined = filePaths.stream()
                .map(path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce(Stream.empty(), Stream::concat);
        this.iterator = combined.iterator();
    }

    @Override
    public synchronized String nextLine() {
        return iterator.hasNext() ? iterator.next() : null;
    }

    @Override
    public void close() {
        combined.close();
    }
}
