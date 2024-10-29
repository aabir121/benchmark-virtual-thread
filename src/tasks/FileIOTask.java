package tasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class FileIOTask implements Task {

    @Override
    public void prepareTaskData() {
        // No specific data preparation needed for this task
    }

    @Override
    public void runTask() {
        Path filePath = null;
        try {
            String data = "This is a test line.\n";
            filePath = Files.createTempFile("tempFile_" + UUID.randomUUID(), ".txt"); // Unique temp file
            Files.write(filePath, data.getBytes(), StandardOpenOption.APPEND);
            Files.readAllLines(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Clean up the temp file after the task
            if (filePath != null) {
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getName() {
        return "File IO Task";
    }
}
