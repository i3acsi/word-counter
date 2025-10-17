package ru.word.counter.util;

import java.lang.Runnable;
import java.util.ArrayList;
import java.util.List;

public class ThreadUtils {

    public static List<Thread> startThreads(int threadCont, Runnable runnable) {
        List<Thread> threads = new ArrayList<>(threadCont);
        for (int i = 0; i < threadCont; i++) {
            Thread thread = new Thread(runnable);
            thread.start();
            threads.add(thread);
        }
        return threads;
    }
}
