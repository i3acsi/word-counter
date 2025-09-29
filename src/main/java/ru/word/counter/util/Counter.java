package ru.word.counter.util;

public class Counter {

    private long count = 1;

    public Counter incrementAndGet(){
        ++count;
        return this;
    }

    public long getCount(){
        return count;
    }
}
