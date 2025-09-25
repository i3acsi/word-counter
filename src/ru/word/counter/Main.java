package ru.word.counter;

import ru.word.counter.impl.FileLinesReaderImpl;
import ru.word.counter.impl.LinesProcessorImpl;
import ru.word.counter.impl.WordCounterImpl;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        int min = 0, max = 0;
        String[] fileNames = new String[0];

        for (String arg : args) {
            if (arg.startsWith("--min=")) {
                min = Integer.parseInt(arg.substring("--min=".length()));
            } else if (arg.startsWith("--max=")) {
                max = Integer.parseInt(arg.substring("--max=".length()));
            } else if (arg.startsWith("--files=")) {
                fileNames = arg.substring("--files=".length()).split(",");
            }
        }

        WordCounter wordCounter = new WordCounterImpl();
        try (FileLinesReader fileLinesReader = new FileLinesReaderImpl(fileNames)) {
            LinesProcessor linesProcessor = new LinesProcessorImpl(
                    fileLinesReader::nextLine,
                    wordCounter::applyWord,
                    10, min, max);
            linesProcessor.processLines();
        }
        Map<String, Long> result = wordCounter.countTopTen();
        System.out.println(result);
    }

}