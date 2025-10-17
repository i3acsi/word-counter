package ru.word.counter;

import java.nio.file.Path;
import java.util.List;

public interface Parameters {
    int getMin();

    int getMax();

    int getThreads();

    List<Path> getFilePaths();

    boolean getShowDuration();
}
