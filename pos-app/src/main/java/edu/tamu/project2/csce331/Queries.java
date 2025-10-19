package edu.tamu.project2.csce331;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// import javafx.beans.property.IntegerProperty;

/**
 * Data-access facade for the POS application.
 *
 * <p>Provides read/write operations over the core schema: {@code personnel}, {@code menu}, {@code
 * seasonal_menu}, {@code ingredients}, {@code ingredients_map}, {@code transactions}, and {@code
 * transaction_details}. Methods use JDBC (try-with-resources), acquire connections via {@link
 * Database#getConnection()}, and throw {@link java.sql.SQLException} on database errors.
 *
 * <p><strong>Organization:</strong>
 *
 * <ul>
 *   <li><em>EMPLOYEES</em> – get/add/update/delete/count employees
 *   <li><em>MENU & ITEMS</em> – menu fetch, item lookups, item–ingredient mapping, CRUD
 *   <li><em>SEASONAL MENU</em> – seasonal menu fetch/add/delete
 *   <li><em>INGREDIENTS</em> – ingredient CRUD, inventory adjustments, lookups
 *   <li><em>TRANSACTIONS</em> – list/count/get transactions, add with details
 *   <li><em>HELPERS</em> – private utilities used by other methods
 * </ul>
 *
 * <p><strong>Notes:</strong>
 *
 * <ul>
 *   <li>Queries target a PostgreSQL schema and rely on the defined foreign keys/indexes.
 *   <li>Transactional boundaries are caller-controlled unless otherwise documented.
 *   <li>Return types favor order-preserving collections (e.g., {@link java.util.LinkedHashMap})
 *       when SQL specifies ordering.
 * </ul>
 */
public class Queries {
  /* Index:
   * EMPLOYEES: get/add/update/delete/count employees
   * MENU & ITEMS: menu fetch, item lookups, item–ingredient mapping, CRUD
   * SEASONAL MENU: seasonal menu fetch/add/delete
   * INGREDIENTS: ingredient CRUD, inventory adjustments, lookups
   * TRANSACTIONS: list/count/get transactions, add with details
   * HELPERS: private utilities used by other methods
   */

  // ============================== EMPLOYEES ==============================

  /**
   * Retrieves all employees whose role is {@code 'manager'} from the {@code personnel} table.
   *
   * <p>This method executes a simple SELECT filtered on {@code role = 'manager'} and constructs
   * {@link Employee} instances for each matching row. The list preserves the database iteration
   * order.
   *
   * @return a list of {@link Employee} objects representing all managers
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Retrieves all employees from the {@code personnel} table.
   *
   * @return a list of all {@link Employee} records
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Inserts a new employee into the {@code personnel} table.
   *
   * @param name the employee's name
   * @param role the employee's role (e.g., cashier, manager)
   * @param pay the employee's pay rate
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Updates an existing employee's name, role, and pay.
   *
   * @param id the employee's identifier
   * @param name the new name
   * @param role the new role
   * @param pay the new pay rate
   * @throws SQLException if a database access error occurs or the employee does not exist
   */
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

  /**
   * Updates an employee's role.
   *
   * @param id the employee's identifier
   * @param role the new role value
   * @throws SQLException if a database access error occurs or the employee does not exist
   */
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

  /**
   * Updates an employee's pay.
   *
   * @param id the employee's identifier
   * @param pay the new pay rate
   * @throws SQLException if a database access error occurs or the employee does not exist
   */
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

  /**
   * Deletes an employee by identifier.
   *
   * @param id the employee's identifier
   * @throws SQLException if a database access error occurs or the employee does not exist
   */
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

  /**
   * Counts the number of rows in the {@code personnel} table.
   *
   * @return the total number of employees
   * @throws SQLException if a database access error occurs
   */
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

  // ============================== MENU & ITEMS ==============================

  /**
   * Retrieves all menu items and their ingredient ids.
   *
   * <p>For each menu row, this method also loads ingredient identifiers via a helper query so that
   * {@link Item} contains its associated ingredient ids.
   *
   * @return a list of all {@link Item} records in the menu
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Looks up an item's identifier by its name.
   *
   * @param item the item name to search for
   * @return the corresponding {@code item_id}
   * @throws SQLException if the item does not exist or a database access error occurs
   */
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

  /**
   * Retrieves the ingredient details for a given menu item.
   *
   * @param item_id the menu item's identifier
   * @return a list of {@link Ingredient} records used by the item
   * @throws SQLException if a database access error occurs
   */
  public java.util.ArrayList<Ingredient> get_item_ingredients(int item_id)
      throws java.sql.SQLException {
    String sql_string =
        "SELECT i.ingredient_id, i.ingredient_name, i.quantity, i.category "
            + "FROM ingredients i "
            + "JOIN ingredients_map m ON m.ingredient_id = i.ingredient_id "
            + "WHERE m.item_id = ?;";

    try (java.sql.Connection conn = Database.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql_string)) {
      stmt.setInt(1, item_id);

      try (java.sql.ResultSet rs = stmt.executeQuery()) {
        java.util.ArrayList<Ingredient> list = new java.util.ArrayList<>();

        while (rs.next()) {
          int ingredient_id = rs.getInt("ingredient_id");
          String ingredient_name = rs.getString("ingredient_name");
          int quantity = rs.getInt("quantity");
          String category = rs.getString("category");
          list.add(new Ingredient(ingredient_name, quantity, category, ingredient_id));
        }
        return list;
      }
    }
  }

  /**
   * Adds a single ingredient mapping to a menu item.
   *
   * @param item_id the menu item's identifier
   * @param ingredient_id the ingredient's identifier to associate
   * @throws SQLException if a database access error occurs
   */
  public void add_ingredient_to_item(int item_id, int ingredient_id) throws java.sql.SQLException {
    String sql_string = "INSERT INTO ingredients_map (ingredient_id, item_id) VALUES (?, ?);";

    try (java.sql.Connection conn = Database.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql_string)) {
      stmt.setInt(1, ingredient_id);
      stmt.setInt(2, item_id);
      stmt.executeUpdate();
    }
  }

  /**
   * Removes an ingredient mapping from a menu item.
   *
   * @param item_id the menu item's identifier
   * @param ingredient_id the ingredient's identifier to disassociate
   * @throws SQLException if a database access error occurs
   */
  public void remove_ingredient_from_item(int item_id, int ingredient_id)
      throws java.sql.SQLException {
    String sql_string = "DELETE FROM ingredients_map WHERE item_id = ? AND ingredient_id = ?;";

    try (java.sql.Connection conn = Database.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql_string)) {
      stmt.setInt(1, item_id);
      stmt.setInt(2, ingredient_id);
      stmt.executeUpdate();
    }
  }

  /**
   * Adds multiple ingredient mappings for a menu item.
   *
   * @param item_id the menu item's identifier
   * @param ingredient_id_list a list of ingredient identifiers to associate
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Inserts a new menu item and returns the generated {@code item_id}.
   *
   * @param added_item the item to insert (name, popularity, price are used)
   * @return the generated {@code item_id}
   * @throws SQLException if the insert fails or a database access error occurs
   */
  public int add_menu_item(Item added_item) throws java.sql.SQLException {

    String sql_string =
        "INSERT INTO menu (item_name, item_popularity, price) "
            + "VALUES (?, ?, ?) RETURNING item_id;";

    try (java.sql.Connection conn = Database.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql_string)) {
      stmt.setString(1, added_item.get_name());
      stmt.setInt(2, added_item.get_popularity());
      stmt.setDouble(3, added_item.get_price());
      try (java.sql.ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int new_id = rs.getInt("item_id");
          added_item.set_id(new_id);
          return new_id;
        }
      }
    }
    throw new java.sql.SQLException("Failed to insert menu item properly");
  }

  /**
   * Updates the price of a menu item.
   *
   * @param id the item's identifier
   * @param price the new price
   * @throws SQLException if a database access error occurs
   */
  public void update_menu_price(int id, double price) throws SQLException {
    String sql = "UPDATE  menu SET price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, price);
      stmt.setInt(2, id);
      stmt.executeUpdate();
    }
  }

  /**
   * Updates a menu item's fields.
   *
   * @param update_item the item containing new values (name, popularity, price, id)
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Deletes a menu item by identifier.
   *
   * @param id the item's identifier
   * @throws SQLException if a database access error occurs
   */
  public void delete_item(int id) throws SQLException {
    String sql = "DELETE FROM menu ingredients_map WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  // ============================== SEASONAL MENU ==============================

  /**
   * Retrieves all items from the seasonal menu, including their ingredient ids.
   *
   * @return a list of seasonal {@link Item} entries
   * @throws SQLException if a database access error occurs
   */
  public ArrayList<Item> get_seasonal_menu() throws SQLException {
    String sql_string = "SELECT * FROM seasonal_menu;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql_string);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Item> menu = new ArrayList<>();

      while (rs.next()) {
        int item_id = rs.getInt("item_id");
        String item_name = rs.getString("item_name");
        int item_popularity = rs.getInt("item_popularity");
        double price = rs.getDouble("price");
        ArrayList<Integer> ingredients = get_ingredients_for_item(item_id, conn);
        menu.add(new Item(item_id, item_name, item_popularity, price, ingredients));
      }

      return menu;
    }
  }

  /**
   * Inserts a new seasonal menu item and returns its generated identifier.
   *
   * @param added_item the seasonal item to insert
   * @return the generated seasonal {@code item_id}
   * @throws SQLException if the insert fails or a database access error occurs
   */
  public int add_seasonal_menu_item(Item added_item) throws java.sql.SQLException {
    String sql_string =
        "INSERT INTO seasonal_menu (item_name, item_popularity, price, start_time, end_time) "
            + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '90 days') "
            + "RETURNING item_id;";

    try (java.sql.Connection conn = Database.getConnection();
        java.sql.PreparedStatement stmt = conn.prepareStatement(sql_string)) {
      stmt.setString(1, added_item.get_name());
      stmt.setInt(2, added_item.get_popularity());
      stmt.setDouble(3, added_item.get_price());
      try (java.sql.ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int new_id = rs.getInt("item_id");
          added_item.set_id(new_id);
          return new_id;
        }
      }
    }
    throw new java.sql.SQLException("No ID returned from INSERT");
  }

  /**
   * Deletes a seasonal menu item by identifier.
   *
   * @param id the item's identifier
   * @throws SQLException if a database access error occurs
   */
  public void delete_seasonal_item(int id) throws SQLException {
    String sql = "DELETE FROM seasonal_menu WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  // ============================== INGREDIENTS ==============================

  /**
   * Retrieves all ingredients.
   *
   * @return a list of {@link Ingredient} records
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Looks up an ingredient id by its name.
   *
   * @param ingredient the ingredient name
   * @return the corresponding {@code ingredient_id}
   * @throws SQLException if the ingredient does not exist or a database access error occurs
   */
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

  /**
   * Increases an ingredient's inventory quantity by the specified amount.
   *
   * @param ingredient_name the ingredient name to update
   * @param quantity the amount to add (must be non-negative)
   * @throws SQLException if a database access error occurs or the ingredient is not found
   */
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

  /**
   * Decreases an ingredient's inventory quantity by the specified amount.
   *
   * @param ingredient_id the ingredient id to update
   * @param quantity the amount to subtract (must be non-negative)
   * @throws SQLException if a database access error occurs or the ingredient is not found
   */
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

  // need to add ingredits
  /**
   * Adds a new ingredient record.
   *
   * @param update_ingredients the ingredient data to insert
   * @throws SQLException if a database access error occurs
   */
  public void add_ingredients(Ingredient update_ingredients) throws SQLException {
    String sql =
        "INSERT INTO  menu (ingredient_name, quantity, category, ingredient_id) VALUE"
            + " (?,?,?,?)";

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
  /**
   * Updates fields on an existing ingredient record.
   *
   * @param update_ingredients the ingredient data containing new values
   * @throws SQLException if a database access error occurs
   */
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
  /**
   * Deletes an ingredient by identifier.
   *
   * @param id the ingredient id
   * @throws SQLException if a database access error occurs
   */
  public void delete_ingredients(int id) throws SQLException {
    String sql = "DELETE FROM ingredients WHERE ingredient_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves a mapping of ingredient names to the number of times each ingredient was used in
   * transactions within a specified time range.
   *
   * <p>This method queries the database by joining the {@code transactions}, {@code
   * transaction_details}, {@code ingredients_map}, and {@code ingredients} tables. Each transaction
   * within the given timestamp range contributes counts for its associated ingredients. The results
   * are ordered by usage frequency (descending) and ingredient name, and returned as a {@link
   * LinkedHashMap} to preserve that order.
   *
   * @param start the inclusive lower bound of the timestamp range
   * @param end the inclusive upper bound of the timestamp range
   * @return a map where each key is an ingredient name and each value is the number of times that
   *     ingredient was used during the period
   * @throws SQLException if a database access error occurs
   */
  public ArrayList<IngredientUsage> get_ingredient_usage(Timestamp start, Timestamp end)
      throws SQLException {
    String sql =
        "SELECT i.ingredient_name, COUNT(*) AS times_used "
            + "FROM transactions t "
            + "JOIN transaction_details td ON td.transaction_id = t.transaction_id "
            + "JOIN ingredients_map im ON im.item_id = td.item_id "
            + "JOIN ingredients i ON i.ingredient_id = im.ingredient_id "
            + "WHERE t.transaction_time >= ? AND t.transaction_time <= ? "
            + "GROUP BY i.ingredient_name "
            + "ORDER BY times_used DESC, i.ingredient_name";

    // Create new object to store values later
    ArrayList<IngredientUsage> res = new ArrayList<>();

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setTimestamp(1, start);
      stmt.setTimestamp(2, end);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          String name = rs.getString("ingredient_name");
          Integer count = rs.getInt("times_used");
          IngredientUsage curr = new IngredientUsage(name, count);
          res.add(curr);
        }
      }
    }
    return res;
  }

  /**
   * Produces a reverse-chronological “sales tape” of items sold within a time window.
   *
   * <p>Each row in the result represents one sold item (one row in {@code transaction_details}),
   * joined to its transaction timestamp and menu name. Results are ordered by {@code
   * transactions.transaction_time} descending, then {@code menu.item_name} ascending for stable
   * per-timestamp ordering.
   *
   * @param start inclusive lower bound for {@code transactions.transaction_time}
   * @param end inclusive upper bound for {@code transactions.transaction_time}
   * @return a list of {@link TimeItemName} pairs, one per item sold in the window
   * @throws SQLException if a database access error occurs
   *     <p><strong>Notes:</strong>
   *     <ul>
   *       <li>Assumes {@code transaction_details(transaction_id)} and {@code menu(item_id)} foreign
   *           keys.
   *       <li>For large ranges, consider paging (LIMIT/OFFSET) or streaming the ResultSet.
   *       <li>Indexes on {@code transactions(transaction_time)}, {@code
   *           transaction_details(transaction_id)}, and {@code transaction_details(item_id)} will
   *           improve performance.
   *     </ul>
   */
  public ArrayList<TimeItemName> get_sales_report(Timestamp start, Timestamp end)
      throws SQLException {
    final String sql =
        "SELECT m.item_name, t.transaction_time "
            + "FROM transactions t "
            + "JOIN transaction_details td ON td.transaction_id = t.transaction_id "
            + "JOIN menu m ON td.item_id = m.item_id "
            + "WHERE t.transaction_time >= ? AND t.transaction_time <= ? "
            + "ORDER BY t.transaction_time DESC, m.item_name ASC";

    ArrayList<TimeItemName> res = new ArrayList<>();

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setTimestamp(1, start);
      stmt.setTimestamp(2, end);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Timestamp time = rs.getTimestamp("transaction_time");
          String itemName = rs.getString("item_name");
          res.add(new TimeItemName(time, itemName));
        }
      }
    }
    return res;
  }

  // ============================== TRANSACTIONS ==============================

  /**
   * Retrieves a paged list of transactions using an absolute offset.
   *
   * @param offset the starting row offset (must be non-negative)
   * @return up to 50 {@link Transaction} records beginning at the offset
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Retrieves a paged list of transactions using page and page-size.
   *
   * @param page the zero-based page number
   * @param pageSize the page size (must be > 0)
   * @return a list of {@link Transaction} records for the requested page
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Counts the number of rows in the {@code transactions} table.
   *
   * @return the total number of transactions
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Retrieves a single transaction by identifier.
   *
   * @param transaction_id the transaction identifier
   * @return the matching {@link Transaction}
   * @throws SQLException if the transaction does not exist or a database access error occurs
   */
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

  /**
   * Retrieves all transactions that occurred at an exact timestamp.
   *
   * @param time the exact timestamp to match
   * @return a list of {@link Transaction} records matching the timestamp
   * @throws SQLException if a database access error occurs
   */
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

  /**
   * Inserts a transaction row and returns its generated identifier.
   *
   * @param customer_name the customer's name
   * @param transaction_time the time of the transaction
   * @param employee_id the employee responsible for the transaction
   * @param total_price the total amount charged
   * @param conn an open SQL connection (transactional context)
   * @return the generated {@code transaction_id}
   * @throws SQLException if the insert fails or no id is generated
   */
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

  /**
   * Inserts a transaction header and its associated item detail rows atomically.
   *
   * <p>This method begins a transaction, creates the {@code transactions} row, then inserts
   * corresponding {@code transaction_details} rows for each provided item. All changes are
   * committed together or rolled back on failure.
   *
   * @param transaction the transaction header data (customer, time, employee, total)
   * @param items the list of items sold in the transaction
   * @throws SQLException if any database operation fails
   */
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

  // ============================== HELPERS ==============================

  /**
   * Helper that returns ingredient identifiers for a given menu item.
   *
   * @param item_id the menu item's identifier
   * @param conn an open SQL connection to use
   * @return a list of ingredient ids associated with the item
   * @throws SQLException if a database access error occurs
   */
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
}
