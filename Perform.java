import java.util.ArrayList;
import java.util.List;

public class Perform {
    public static void main(String[] args) {
        long overallStart = System.nanoTime();
        int RUNS = 200;
        int SIZE = 100000;

        Integer[] ints = new Integer[SIZE];
        for (int j = 0; j < SIZE; j++) {
            ints[j] = j; // Autoboxing simplifies this.
        }

        List<Integer> list = new ArrayList<>(SIZE);

        for (int r = 0; r < RUNS; r++) {
            // Adding elements
            long startAdd = System.nanoTime();
            for (int j = 0; j < SIZE; j++) {
                list.add(ints[j]);
            }
            long endAdd = System.nanoTime();

            // Removing elements
            long startRemove = System.nanoTime();
            for (int j = 0; j < SIZE; j++) {
                list.remove(list.size() - 1);
            }
            long endRemove = System.nanoTime();

            // Progress updates (outside timing logic)
            System.out.printf(
                    "\rRun %d/%d - Adding elements... Done in %.3f ms. Removing elements...  Done in %.3f ms.",
                    r + 1, RUNS, (endAdd - startAdd) / 1_000_000.0, (endRemove - startRemove) /
                            1_000_000.0);

        }

        long overallEnd = System.nanoTime();
        double totalTimePerOperation = (overallEnd - overallStart) / (double) (RUNS * SIZE * 2); // 2 ops (add + remove)
                                                                                                 // per element.
        System.out.printf("\nTotal time per operation: %.3f ns%n", totalTimePerOperation);
    }
}
