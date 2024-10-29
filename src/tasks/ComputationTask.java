package tasks;

import java.util.Arrays;
import java.util.Random;

public class ComputationTask implements Task {
    private int[] numbers;

    @Override
    public void prepareTaskData() {
        numbers = createRandomNumbersArray();
    }

    @Override
    public void runTask() {
        int[] numbersCopy = Arrays.copyOf(numbers, numbers.length);
        for (int number : numbersCopy) {
            if (number == 1000000) {
                break;
            }
        }
    }

    private static int[] createRandomNumbersArray() {
        int[] numbers = new int[1000000];
        Random random = new Random();
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = 1 + random.nextInt(Integer.MAX_VALUE - 1);
        }
        return numbers;
    }

    @Override
    public String getName() {
        return "Computation Task";
    }
}
