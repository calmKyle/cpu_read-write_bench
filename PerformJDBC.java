import java.io.File;
import java.sql.*;

public class PerformJDBC {
    public static void main(String[] args) {
        String dbFileName = "testdb.db"; // Database file name
        String url = "jdbc:sqlite:" + dbFileName; // SQLite connection URL
        String tableName = "TestTable";
        int RUNS = 100; // Number of repetitions
        int ROWS = 1000; // Number of rows to insert/read

        // Remove the database file if it exists
        File dbFile = new File(dbFileName);
        if (dbFile.exists() && !dbFile.delete()) {
            System.err.println("Failed to delete database file: " + dbFileName);
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url)) {
                System.out.println("Connected to the database.");

                // Create table
                String createTableSQL = "CREATE TABLE " + tableName +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, value TEXT)";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                }

                // Measure write performance
                long totalWriteTime = 0; // To calculate total write time
                String insertSQL = "INSERT INTO " + tableName + " (value) VALUES (?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    for (int r = 0; r < RUNS; r++) {
                        long batchStart = System.nanoTime(); // Start time for the batch
                        for (int i = 1; i <= ROWS; i++) {
                            pstmt.setString(1, "Value " + ((r * ROWS) + i));
                            pstmt.addBatch();
                        }
                        pstmt.executeBatch();
                        long batchEnd = System.nanoTime(); // End time for the batch

                        // Calculate time per batch
                        long batchTime = batchEnd - batchStart;
                        totalWriteTime += batchTime;

                        // Print progress and time for each batch
                        System.out.printf("\rRun %d/%d - Writing... Progress: %d%%, Time per batch: %.3f ms",
                                r + 1, RUNS, (r * 100) / RUNS, batchTime / 1_000_000.0);
                    }
                }

                // Measure read performance
                long readStart = System.nanoTime();
                String selectSQL = "SELECT * FROM " + tableName;
                try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                        ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String value = rs.getString("value");
                    }
                }
                long readEnd = System.nanoTime();

                // Calculate and display performance
                double writeTimePerOp = totalWriteTime / (double) (RUNS * ROWS);
                double readTimePerOp = (readEnd - readStart) / (double) (RUNS * ROWS);

                System.out.printf("\nWrite time per row: %.3f ns%n", writeTimePerOp);
                System.out.printf("Read time per row: %.3f ns%n", readTimePerOp);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

