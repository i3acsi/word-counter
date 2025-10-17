package ru.word.counter;

import ru.word.counter.impl.ArgsParameters;
import ru.word.counter.impl.FileLinesReaderImpl;
import ru.word.counter.impl.LinesProcessorImpl;
import ru.word.counter.impl.WordCounterImpl;

import java.util.Map;

public class Main {


    public static void main(String[] args) throws Exception {
        Parameters parameters = new ArgsParameters(args);
        long start = System.currentTimeMillis();

        WordCounter wordCounter = new WordCounterImpl();
        try (FileLinesReader fileLinesReader = new FileLinesReaderImpl(parameters.getFilePaths())) {
            LinesProcessor linesProcessor = new LinesProcessorImpl(
                    fileLinesReader::nextLine,
                    wordCounter::applyWord,
                    parameters.getThreads(), parameters.getMin(), parameters.getMax());
            linesProcessor.processLines();
        }
        Map<String, Long> result = wordCounter.countTopTen();
        System.out.println(result);

        if (parameters.getShowDuration()) {
            System.out.printf("Total time for parsing: %d milliseconds.%n",
                    System.currentTimeMillis() - start);

        }
    }

}