import tasks.ComputationTask;
import tasks.FileIOTask;
import tasks.Task;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final int INITIAL_THREAD_COUNT = 10;
    private static final int MAX_THREAD_COUNT = 50000;
    private List<Task> tasks = List.of(new FileIOTask(), new ComputationTask());
    private List<IterationResult> results = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException, IOException {
        new Main().startTest();
    }

    private void startTest() throws InterruptedException, IOException {
        int threadCount = INITIAL_THREAD_COUNT;

        while (threadCount <= MAX_THREAD_COUNT) {
            System.out.println("\nStarting tests with " + threadCount + " threads:");
            for (Task task : tasks) {
                runTaskTest(task, threadCount);
            }
            if (threadCount >= 10000) {
                threadCount += 10000;
            } else {
                threadCount *= 10;
            }
        }
        printResults();
    }

    private void runTaskTest(Task task, int threadCount) throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(threadCount);
        task.prepareTaskData();

        // Regular threads
        ManagementFactory.getThreadMXBean().resetPeakThreadCount();

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            CountDownLatch finalLatch = latch;
            new Thread(() -> {
                task.runTask();
                finalLatch.countDown();
            }).start();
        }
        latch.await();
        long timeTakenRegular = System.currentTimeMillis() - start;
        System.out.println("Time taken by " + task.getName() + " with regular threads: " + timeTakenRegular + "ms");
        long peakThreadCountRegular = ManagementFactory.getThreadMXBean().getPeakThreadCount();
        System.out.println(("Peak thread count " + peakThreadCountRegular));


        // Virtual threads
        ManagementFactory.getThreadMXBean().resetPeakThreadCount();
        latch = new CountDownLatch(threadCount);
        start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            CountDownLatch finalLatch1 = latch;
            Thread.startVirtualThread(() -> {
                task.runTask();
                finalLatch1.countDown();
            });
        }
        latch.await();
        long timeTakenVirtual = System.currentTimeMillis() - start;
        long peakThreadCountVirtual = ManagementFactory.getThreadMXBean().getPeakThreadCount();

        System.out.println("Time taken by " + task.getName() + " with virtual threads: " + timeTakenVirtual + "ms");
        System.out.println(("Peak thread count " + peakThreadCountVirtual));


        results.add(new IterationResult(task.getName(), threadCount,
                timeTakenRegular, timeTakenVirtual,
                peakThreadCountRegular, peakThreadCountVirtual));
    }

    private void printResults() {
        System.out.println("\nTest Results Summary:");
        System.out.printf("%-20s %-15s %-20s %-20s %-15s %-15s\n", "Task", "Threads", "Regular Threads (ms)", "Virtual Threads (ms)", "Regular Peak", "Virtual Peak");
        long totalRegular = 0, totalVirtual = 0;
        int iterationCount = 0;

        for (IterationResult result : results) {
            System.out.printf("%-20s %-15d %-20d %-20d %-15d %-15d\n", result.taskName, result.threadCount,
                    result.timeTakenRegular, result.timeTakenVirtual,
                    result.peakThreadCountRegular, result.peakThreadCountVirtual);
            totalRegular += result.timeTakenRegular;
            totalVirtual += result.timeTakenVirtual;
            iterationCount++;
        }

        System.out.println("\nAverage Time Taken:");
        System.out.printf("Regular Threads: %d ms, Virtual Threads: %d ms\n", totalRegular / iterationCount, totalVirtual / iterationCount);
    }
}