package ThreadsExcersises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Excersise4 {
    public static void main(String[] args) {
        // Determine the number of available processors
        int availableProcessors = Runtime.getRuntime().availableProcessors();

        //Create executerservice based on how many cores we have in pc
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);

        // Submit tasks to print ints to 10.000 or more for longer load??
        for (int ch = 0; ch <= 10000 ; ch++) {
            final int finalCh = ch;
            executorService.submit(() -> System.out.println(Thread.currentThread().getName() + ": " + finalCh));
        }

        // Shutdown the ExecutorService
        executorService.shutdown();
    }
}
