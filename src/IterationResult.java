class IterationResult {
    String taskName;
    int threadCount;
    long timeTakenRegular;
    long timeTakenVirtual;
    long peakThreadCountRegular;
    long peakThreadCountVirtual;

    IterationResult(String taskName, int threadCount,
                    long timeTakenRegular, long timeTakenVirtual,
                    long peakThreadCountRegular, long peakThreadCountVirtual) {
        this.taskName = taskName;
        this.threadCount = threadCount;
        this.timeTakenRegular = timeTakenRegular;
        this.timeTakenVirtual = timeTakenVirtual;
        this.peakThreadCountRegular = peakThreadCountRegular;
        this.peakThreadCountVirtual = peakThreadCountVirtual;
    }
}