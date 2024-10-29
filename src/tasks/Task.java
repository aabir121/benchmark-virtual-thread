package tasks;

import java.io.IOException;

public interface Task {
    void runTask();
    void prepareTaskData() throws IOException;
    String getName();
}