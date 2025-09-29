package ru.word.counter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parameters {
    private static final String TXT = ".txt";

    private int min = 0;
    private int max = 0;
    private int threads = 1;
    private final List<Path> filePaths;

    public Parameters(String... args) {
        List<String> pathNames = new ArrayList<>();

        boolean nextAnyFile = false;
        for (String arg : args) {
            if (nextAnyFile) {
                pathNames.add(arg);
                continue;
            }
            if (arg.startsWith("--min=")) {
                this.min = Integer.parseInt(arg.substring("--min=".length()));
            } else if (arg.startsWith("--max=")) {
                this.max = Integer.parseInt(arg.substring("--max=".length()));
            } else if (arg.startsWith("--threads=")) {
                this.threads = Integer.parseInt(arg.substring("--threads=".length()));
            } else if (arg.startsWith("--files")) {
                nextAnyFile = true;
            }
        }
        this.filePaths = pathNames.stream()
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
                .toList();

    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getThreads() {
        return threads;
    }

    public List<Path> getFilePaths() {
        return filePaths;
    }
}
