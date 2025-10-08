package edu.tamu.project2.csce331;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

  @Test
  void verify_connection() throws Exception {
    // Init the connection pool
    try (Connection conn = Database.getConnection()) {
      // Verify connection is valid
      assertNotNull(conn, "Connection should not be null");
      assertFalse(conn.isClosed(), "Connection should be open");

      // Trying a simple SQL query to make sure that it works
      try (PreparedStatement ps = conn.prepareStatement("SELECT 1");
          ResultSet rs = ps.executeQuery()) {
        assertTrue(rs.next(), "Should return a row");
        assertEquals(1, rs.getInt(1), "SELECT 1 should return 1");
      }
    }
  }

  // @Test
  void transactions_table_exists_and_accessible() throws Exception {
    try (Connection conn = Database.getConnection()) {
      assertNotNull(conn, "Connection should not be null");
      assertFalse(conn.isClosed(), "Connection should be open");

      // Query up to 10 rows from the table
      try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM transactions LIMIT 10");
          ResultSet rs = ps.executeQuery()) {

        ResultSetMetaData meta = rs.getMetaData();
        assertNotNull(meta, "ResultSetMetaData should not be null (table likely exists)");

        int column_count = meta.getColumnCount();
        System.out.println("\n'transactions' table exists.");
        System.out.println("Displaying up to 10 rows:\n");

        // Print column headers
        for (int i = 1; i <= column_count; i++) {
          System.out.print(meta.getColumnName(i) + "\t");
        }
        System.out.println();

        // Try to print the rows, track if any found
        int row_count = 0;
        while (rs.next()) {
          row_count++;
          for (int i = 1; i <= column_count; i++) {
            System.out.print(rs.getString(i) + "\t");
          }
          System.out.println();
        }

        if (row_count == 0) {
          System.out.println("(No rows found in the transactions table)");
        } else {
          System.out.println("\nDisplayed " + row_count + " row(s).");
        }
      }
    }
  }

  // Helper: check if a table exists in the current schema
  private boolean tableExists(Connection conn, String table) throws SQLException {
    final String sql =
        "SELECT EXISTS (\n"
            + "  SELECT 1 FROM information_schema.tables\n"
            + "  WHERE table_schema = current_schema() AND table_name = ?\n"
            + ")";
    // Prepare statement and execute to check whether a specific table exists
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, table);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getBoolean(1);
      }
    }
  }

  @Test
  void all_expected_tables_exist() throws Exception {
    String[] expected_tables =
        new String[] {
          "ingredients",
          "ingredients_map",
          "menu",
          "personnel",
          "transaction_details",
          "transactions"
        };

    // Connect to current schema
    try (Connection conn = Database.getConnection()) {
      String current_schema;
      try (PreparedStatement s = conn.prepareStatement("SELECT current_schema()")) {
        try (ResultSet rs = s.executeQuery()) {
          rs.next();
          current_schema = rs.getString(1);
        }
      }

      // Check each expected table
      System.out.println("\nChecking required tables in schema '" + current_schema + "':");
      for (String table : expected_tables) {
        boolean exists = tableExists(conn, table);
        System.out.println(" - " + table + ": " + (exists ? "FOUND" : "MISSING"));
        assertTrue(exists, "Missing required table: " + table);
      }
    }
  }
}
