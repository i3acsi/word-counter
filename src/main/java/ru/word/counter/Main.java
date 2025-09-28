package ru.word.counter;

import ru.word.counter.impl.FileLinesReaderImpl;
import ru.word.counter.impl.LinesProcessorImpl;
import ru.word.counter.impl.WordCounterImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final String TXT = ".txt";

    public static void main(String[] args) throws Exception {
        int min = 0, max = 0, threads = 1;
        List<String> pathNames = new ArrayList<>();

        boolean nextAnyFile = false;
        for (String arg : args) {
            if (nextAnyFile) {
                pathNames.add(arg);
                continue;
            }
            if (arg.startsWith("--min=")) {
                min = Integer.parseInt(arg.substring("--min=".length()));
            } else if (arg.startsWith("--max=")) {
                max = Integer.parseInt(arg.substring("--max=".length()));
            } else if (arg.startsWith("--threads=")) {
                threads = Integer.parseInt(arg.substring("--threads=".length()));
            } else if (arg.startsWith("--files")) {
                nextAnyFile = true;
            }
        }
        var filePaths = pathNames.stream()
                .map(Path::of)
                .flatMap(p -> {
                    try {
                        return Files.walk(p);
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .filter(Files::isRegularFile)
                .filter(f -> f.getFileName().toString().endsWith(TXT))
                .collect(Collectors.toList());

        WordCounter wordCounter = new WordCounterImpl();
        try (FileLinesReader fileLinesReader = new FileLinesReaderImpl(filePaths)) {
            LinesProcessor linesProcessor = new LinesProcessorImpl(
                    fileLinesReader::nextLine,
                    wordCounter::applyWord,
                    threads, min, max);
            linesProcessor.processLines();
        }
        Map<String, Long> result = wordCounter.countTopTen();
        System.out.println(result);
    }

}