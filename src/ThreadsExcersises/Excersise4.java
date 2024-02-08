package ThreadsExcersises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Excersise4 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool();

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            final char finalCh = ch;
            executorService.submit(() -> System.out.println(Thread.currentThread().getName() + ": " + finalCh));
        }

        executorService.shutdown();
    }
}
