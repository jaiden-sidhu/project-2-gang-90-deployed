package edu.tamu.project2.csce331;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Queries {

  public ArrayList<Employee> get_managers() throws SQLException {
    String sql = "SELECT * FROM personnel WHERE role = 'manager';";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Employee> managers = new ArrayList<>();

      while (rs.next()) {
        int id = rs.getInt("employee_id");
        String name = rs.getString("name");
        String role = rs.getString("role");
        double pay = rs.getDouble("pay");
        managers.add(new Employee(id, name, role, pay));
      }
      return managers;
    }
  }

  public int get_item_id(String item) throws SQLException {
    String sql = "SELECT item_id, item_name FROM menu WHERE item_name = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, item);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("item_id");
        } else {
          throw new SQLException("Item not found: " + item);
        }
      }
    }
  }

  public int get_ingredient_id(String ingredient) throws SQLException {
    String sql = "SELECT item_id, item_name FROM ingredients WHERE ingredient_name = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, ingredient);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt("ingredient_id");
        } else {
          throw new SQLException("Item not found: " + ingredient);
        }
      }
    }
  }

  public void add_employee(int employee_id, String name, String role, double pay)
      throws SQLException {
    String sql = "INSERT INTO personnel (employee_id, name, role, pay) VALUES (?, ?, ?, ?);";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, employee_id);
      stmt.setString(2, name);
      stmt.setString(3, role);
      stmt.setDouble(4, pay);
      stmt.executeUpdate();
    }
  }

  public ArrayList<Transaction> get_transactions(int offset) throws SQLException {
    if (offset < 0) {
      throw new IllegalArgumentException("Offset cannot be negative.");
    }

    String sql = "SELECT * FROM transactions LIMIT 50 OFFSET ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, offset);
      try (ResultSet rs = stmt.executeQuery()) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        while (rs.next()) {
          int transaction_id = rs.getInt("transaction_id");
          String customer_name = rs.getString("customer_name");
          java.sql.Timestamp transaction_time = rs.getTimestamp("transaction_time");
          int employee_id = rs.getInt("employee_id");
          double total_price = rs.getDouble("total_price");
          transactions.add(
              new Transaction(
                  transaction_id, customer_name, transaction_time, employee_id, total_price));
        }
        return transactions;
      }
    }
  }

  public Transaction get_transaction(int transaction_id) throws SQLException {
    if (transaction_id < 0) {
      throw new IllegalArgumentException("Transaction ID cannot be negative.");
    }

    String sql = "SELECT * FROM transactions WHERE transaction_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, transaction_id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String customer_name = rs.getString("customer_name");
          java.sql.Timestamp transaction_time = rs.getTimestamp("transaction_time");
          int employee_id = rs.getInt("employee_id");
          double total_price = rs.getDouble("total_price");
          return new Transaction(
              transaction_id, customer_name, transaction_time, employee_id, total_price);
        } else {
          throw new SQLException("Transaction not found with ID: " + transaction_id);
        }
      }
    }
  }

  // Pass in a string time format "YYYY-MM-DD HH:MM:SS"
  public ArrayList<Transaction> select_transaction_time(Timestamp time) throws SQLException {
    String sql = "SELECT * FROM transactions WHERE  transaction_time = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setTimestamp(1, time);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          ArrayList<Transaction> transactions = new ArrayList<>();
          do {
            int transaction_id = rs.getInt("transaction_id");
            String customer_name = rs.getString("customer_name");
            Timestamp transaction_time = rs.getTimestamp("transaction_time");
            int employee_id = rs.getInt("employee_id");
            double total_price = rs.getDouble("total_price");
            transactions.add(
                new Transaction(
                    transaction_id, customer_name, transaction_time, employee_id, total_price));
          } while (rs.next());
          return transactions;
        } else {
          throw new SQLException("Transaction not found with time: " + time);
        }
      }
    }
  }

  public void add_transaction_details(
      String customer_name, Timestamp transaction_time, int employee_id, double total_price)
      throws SQLException {
    String sql =
        "INSERT INTO transactions (customer_name, transaction_time, employee_id, total_price)"
            + " VALUES (?, ?, ?, ?);";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, customer_name);
      stmt.setTimestamp(2, transaction_time);
      stmt.setInt(3, employee_id);
      stmt.setDouble(4, total_price);
      stmt.executeUpdate();
    }
  }

  public void refill_inventory(String ingredient_name, int quantity) throws SQLException {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }

    String sql =
        "UPDATE ingredients SET quantity = quantity + ? WHERE ingredient_name = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, quantity);
      stmt.setString(2, ingredient_name);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Ingredient not found: " + ingredient_name);
      }
    }
  }

  private void 
}
