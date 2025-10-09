package edu.tamu.project2.csce331;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;

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

  public ArrayList<Employee> get_employee() throws SQLException {
    String sql = "SELECT * FROM personnel;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Employee> employees = new ArrayList<>();

      while (rs.next()) {
        int id = rs.getInt("employee_id");
        String name = rs.getString("name");
        String role = rs.getString("role");
        double pay = rs.getDouble("pay");
        employees.add(new Employee(id, name, role, pay));
      }
      return employees;
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

  public void update_employee(int id, String name, String role, double pay) throws SQLException {
    String sql = "UPDATE personnel SET name = ?, role = ?, pay = ? WHERE employee_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      stmt.setString(2, role);
      stmt.setDouble(3, pay);
      stmt.setInt(4, id);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Employee not found with ID: " + id);
      }
    }
  }

  public void update_employee_role(int id, String role) throws SQLException {
    String sql = "UPDATE personnel SET role = ? WHERE employee_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, role);
      stmt.setInt(2, id);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Employee not found with ID: " + id);
      }
    }
  }

  public void update_employee_pay(int id, double pay) throws SQLException {
    String sql = "UPDATE personnel SET pay = ? WHERE employee_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, pay);
      stmt.setInt(2, id);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Employee not found with ID: " + id);
      }
    }
  }

  public void add_employee(String name, String role, double pay) throws SQLException {
    String sql = "INSERT INTO personnel (name, role, pay) VALUES (?, ?, ?);";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, name);
      stmt.setString(2, role);
      stmt.setDouble(3, pay);
      stmt.executeUpdate();
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

  public ArrayList<Transaction> get_transactions(int page, int pageSize) throws SQLException {
    if (page < 0 || pageSize <= 0) {
      throw new IllegalArgumentException("Page must be >= 0 and pageSize > 0");
    }
    int offset = page * pageSize;
    String sql = "SELECT * FROM transactions ORDER BY transaction_time DESC LIMIT ? OFFSET ?;";
    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, pageSize);
      stmt.setInt(2, offset);
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

  public int count_transactions() throws SQLException {
    String sql = "SELECT COUNT(*) AS cnt FROM transactions;";
    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        return rs.getInt("cnt");
      }
      return 0;
    }
  }

  public int count_employees() throws SQLException {
    String sql = "SELECT COUNT(*) AS cnt FROM personnel;";
    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        return rs.getInt("cnt");
      }
      return 0;
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

  public ArrayList<Transaction> get_transactions(Timestamp time) throws SQLException {
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

  private int add_transaction(
      String customer_name,
      Timestamp transaction_time,
      int employee_id,
      double total_price,
      Connection conn)
      throws SQLException {
    String sql =
        "INSERT INTO transactions (customer_name, transaction_time, employee_id, total_price)"
            + " VALUES (?, ?, ?, ?)";

    try (PreparedStatement ps =
        conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, customer_name);
      ps.setTimestamp(2, transaction_time);
      ps.setInt(3, employee_id);
      ps.setDouble(4, total_price);
      ps.executeUpdate();

      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
          int transaction_id = keys.getInt(1);
          return transaction_id;
        } else {
          throw new SQLException("Creating transaction failed: no ID obtained.");
        }
      }
    }
  }

  public void add_transaction_and_details(Transaction transaction, ArrayList<Item> items)
      throws SQLException {
    try (Connection conn = Database.getConnection()) {
      try {
        conn.setAutoCommit(false);
        // Generate a new transaction and store its ID
        int transaction_id =
            add_transaction(
                transaction.customer_name,
                transaction.transaction_time,
                transaction.employee_id,
                transaction.total_price,
                conn);

        // Insert each item into transaction_details and add to batch
        String sql = "INSERT INTO transaction_details (transaction_id, item_id) VALUES (?, ?);";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
          for (Item item : items) {
            ps.setInt(1, transaction_id);
            ps.setInt(2, item.get_id());
            ps.addBatch();
          }
          ps.executeBatch();
        }
        conn.commit();
      } catch (SQLException e) {
        conn.rollback();
        throw new SQLException("Failed to add transaction: " + e.getMessage());
      }
    }
  }

  private ArrayList<Integer> get_ingredients_for_item(int item_id, Connection conn)
      throws SQLException {
    String sql = "SELECT ingredient_id FROM ingredients_map WHERE item_id = ?;";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, item_id);
      try (ResultSet rs = stmt.executeQuery()) {
        ArrayList<Integer> ingredients = new ArrayList<>();
        while (rs.next()) {
          ingredients.add(rs.getInt("ingredient_id"));
        }
        return ingredients;
      }
    }
  }

  public ArrayList<Item> get_menu() throws SQLException {
    String sql = "SELECT * FROM menu;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Item> menu = new ArrayList<>();

      while (rs.next()) {
        int id = rs.getInt("item_id");
        String name = rs.getString("item_name");
        int popularity = rs.getInt("item_popularity");
        double price = rs.getDouble("price");
        // Fetch ingredients for the item
        ArrayList<Integer> ingredients = get_ingredients_for_item(id, conn);
        menu.add(new Item(id, name, popularity, price, ingredients));
      }
      return menu;
    }
  }

  public void refill_inventory(String ingredient_name, int quantity) throws SQLException {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }

    String sql = "UPDATE ingredients SET quantity = quantity + ? WHERE ingredient_name = ?;";

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

  public void decrease_inventory(int ingredient_id, int quantity) throws SQLException {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    }

    String sql = "UPDATE ingredients SET quantity = quantity - ? WHERE ingredient_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, quantity);
      stmt.setInt(2, ingredient_id);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Ingredient not found with ID: " + ingredient_id);
      }
    }
  }

  public void delete_employee(int id) throws SQLException {
    String sql = "DELETE FROM personnel WHERE employee_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      int rowsAffected = stmt.executeUpdate();
      if (rowsAffected == 0) {
        throw new SQLException("Employee not found with ID: " + id);
      }
    }
  }

  public int add_menu_item(Item added_item) throws SQLException {
    String sql = "INSERT INTO menu (item_name, item_popularity, price) VALUES (?, ?, ?);";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, added_item.get_name());
      stmt.setInt(2, added_item.get_popularity());
      stmt.setDouble(3, added_item.get_price());
      stmt.executeUpdate();
      try (ResultSet keys = stmt.getGeneratedKeys()) {
        if (keys.next()) {
          int transaction_id = keys.getInt(1);
          return transaction_id;
        } else {
          throw new SQLException("Creating transaction failed: no ID obtained.");
        }
    }
  }
}

  public void add_ingredient_map(int item_id, ArrayList<Integer> ingredient_id_list)
      throws SQLException {
    String sql = "INSERT INTO ingredients_map (ingredients_id, item_id) VALUES (?, ?);";
    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      for (int i = 0; i < ingredient_id_list.size(); i++) {
        stmt.setInt(1, item_id);
        stmt.setInt(2, ingredient_id_list.get(i));
        stmt.executeUpdate();
      }
    }
  }

  public void delete_item(int id) throws SQLException {
    String sql = "DELETE FROM menu ingredients_map WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  public void update_menu_price(int id, double price) throws SQLException {
    String sql = "UPDATE  menu SET price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, price);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    }
  }

  public void update_menu_items(Item update_item) throws SQLException {
    String sql = "UPDATE  menu SET item_name = ? item_popularity = ? price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, update_item.get_name());
      stmt.setInt(2, update_item.get_popularity());
      stmt.setDouble(3, update_item.get_popularity());
      stmt.setInt(4, update_item.get_id());
      stmt.executeUpdate();
    }
  }

  public ArrayList<Ingredient> get_ingredients() throws SQLException {
    String sql = "SELECT * FROM ingredients";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Ingredient> ingredients_list = new ArrayList<>();

      while (rs.next()) {
        int ingredient_id = rs.getInt("ingredient_id");
        String ingredient_name = rs.getString("ingredient_name");
        int quantity = rs.getInt("quantity");
        String category = rs.getString("category");
        // Fetch ingredients for the item
        ingredients_list.add(new Ingredient(ingredient_name, quantity, category, ingredient_id));
      }
      return ingredients_list;
    }
  }

  // need to add ingredits
  public void add_ingredients(Ingredient update_ingredients) throws SQLException {
    String sql =
        "INSERST INTO  menu (ingredient_name, quantity, category, ingredient_id) VALUE"
            + " ($1,$2,$3,$4)";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, update_ingredients.get_ingredient_name());
      stmt.setInt(2, update_ingredients.get_quantity());
      stmt.setString(3, update_ingredients.get_category());
      stmt.setInt(4, update_ingredients.get_ingredient_id());
      stmt.executeUpdate();
    }
  }

  // need to alter ingredints
  public void update_ingredients(Ingredient update_ingredients) throws SQLException {
    String sql =
        "UPDATE  ingredients SET item_name = ? item_popularity = ? price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, update_ingredients.get_ingredient_name());
      stmt.setInt(2, update_ingredients.get_quantity());
      stmt.setString(3, update_ingredients.get_category());
      stmt.setInt(4, update_ingredients.get_ingredient_id());
      stmt.executeUpdate();
    }
  }

  // need to delete ingredints

  public void delete_ingredients(int id)
    throws SQLException {
    String sql = "DELETE FROM ingredients WHERE ingredient_id = $1";

    try (Connection conn = Database.getConnection(); 
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1,id);
      stmt.executeUpdate();
    }
  }
}
