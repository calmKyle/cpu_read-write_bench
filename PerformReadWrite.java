import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PerformReadWrite {
    public static void main(String[] args) {
        int RUNS = 100; // Number of repetitions
        int SIZE = 1000000; // Size of data to write in bytes
        String fileName = "testfile.dat"; // Test file name

        // Create data to write
        byte[] data = new byte[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = (byte) (i % 256); // Fill with some pattern
        }

        try {
            // Measure write performance
            long writeStart = System.nanoTime();
            for (int r = 0; r < RUNS; r++) {
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    fos.write(data);
                }
                // Print progress for writing
                if (r % (RUNS / 100) == 0) { // Update every 1%
                    System.out.printf("\rRun %d/%d - Writing... Progress: %d%%", r + 1, RUNS, (r * 100) / RUNS);
                }
            }
            long writeEnd = System.nanoTime();

            // Measure read performance
            long readStart = System.nanoTime();
            for (int r = 0; r < RUNS; r++) {
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    while (fis.read(data) != -1) {
                        // Reading in chunks, no processing required for this test
                    }
                }
                // Print progress for reading
                if (r % (RUNS / 100) == 0) { // Update every 1%
                    System.out.printf("\rRun %d/%d - Reading... Progress: %d%%", r + 1, RUNS, (r * 100) / RUNS);
                }
            }
            long readEnd = System.nanoTime();

            // Calculate and display performance
            double writeTimePerOp = (writeEnd - writeStart) / (double) (RUNS * SIZE);
            double readTimePerOp = (readEnd - readStart) / (double) (RUNS * SIZE);

            System.out.printf("\nWrite time per byte: %.3f ns%n", writeTimePerOp);
            System.out.printf("Read time per byte: %.3f ns%n", readTimePerOp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

