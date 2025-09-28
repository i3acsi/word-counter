package ru.word.counter;

public interface FileLinesReader extends AutoCloseable {

    String nextLine();
}
