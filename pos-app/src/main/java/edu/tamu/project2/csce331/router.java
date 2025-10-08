package edu.tamu.project2.csce331;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Router {
  final String database_name = "gang_90_db";
  final String database_user = "gang_90";
  final String database_password = "gang_90";
  final String database_url =
      String.format("jdbc:postgresql://csce-315-db.engr.tamu.edu/%s", database_name);

  public Router() {}

  private ResultSet connect_exicute(String sql_string) {

    // Building the connection

    Connection conn = null;
    try {

      // open connection

      conn = DriverManager.getConnection(database_url, database_user, database_password);

      //

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
      return null;

      // how will this exit effect the program

    }

    Statement make_statment = null;
    try {

      // prepare sql to take a connection

      make_statment = conn.createStatement();
    } catch (Exception e) {

      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
      return null;
    }
    ResultSet result = null;
    try {

      // exicute sql statment

      result = make_statment.executeQuery(sql_string);
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
      return null;
    }

    try {

      // close connection

      conn.close();
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
      return null;
    }
  }

  // DONE
  public ResultSet get_managers() {

    String sql_string = "SELECT * FROM personel WHERE role = 'manager';";
    // may return null if error
    return connect_exicute(sql_string);
  }

  // DONE
  public ResultSet get_empolyee() {

    String sql_string = "SELECT * FROM personel;";
    // may return null if error
    return connect_exicute(sql_string);
  }

  // DONE
  public ResultSet get_item_id() {

    String sql_string = "SELECT item_id, item_name FROM menu;";
    // may return null if error
    return connect_exicute(sql_string);
  }

  // DONE
  public ResultSet update_employee(int employee_id, String name, String role, double pay) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE update_empoylee (integer, Varchar(255), varchar(255),NUMERIC(10, 2)) AS
            UPDATE personnel SET  name = $2 role = $3 pay = $4
            WHERE employee_id = $1;

            EXECUTE update_empoylee (%d, '%s','%s', %f);

            """,
            employee_id, name, role, pay);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // add employee
  // DONE
  public ResultSet add_employee(int employee_id, String name, String role, double pay) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE insert_empoylee (integer, Varchar(255), varchar(255),NUMERIC(10, 2)) AS
            INSERT INTO personnel (employee_id, name, role, pay) VALUES($1,$2, $3, $4);

            EXECUTE insert_empoylee (%d, '%s','%s', %f);

            """,
            employee_id, name, role, pay);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // DONE as get_transactions()
  // select all transactions grab 50 pass in a offset
  public ResultSet select_transaction(int offset) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    // consider sending these in seperate querys and only making one Prepare statment
    String sql_string =
        String.format(
            """
            PREPARE select_transaction (integer) AS
            SELECT * FROM transactions
            LIMIT 50 OFFSET $1;

            EXECUTE select_transaction (%d);
            """,
            offset);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // select transation details given transaction id
  // DONE as get_transaction();
  public ResultSet select_transaction_details_id(int id) {
    String sql_string =
        String.format(
            """
            PREPARE select_transaction_details_id (integer) AS
            SELECT * FROM transaction_details
            WHERE  transaction_id = $1;


            EXECUTE select_transaction_details_id (%d);
            """,
            id);

    return connect_exicute(sql_string);
  }

  // select by time
  // DONE as get_transactions();
  public ResultSet select_transaction_time(String time) {
    String sql_string =
        String.format(
            """
            PREPARE select_transaction_time (integer) AS
            SELECT * FROM transactions
            WHERE  transaction_time = $1;


            EXECUTE select_transaction_time (%s);
            """,
            time);

    return connect_exicute(sql_string);
  }

  // select by order Id
  // DONE, as its the exact same as select_transaction_details_id()
  public ResultSet select_transaction_id(int id) {
    String sql_string =
        String.format(
            """
            PREPARE select_transaction_id (integer) AS
            SELECT * FROM transactions
            WHERE  transaction_id = $1;


            EXECUTE select_transaction_time (%d);
            """,
            id);

    return connect_exicute(sql_string);
  }

  // transation details click on transaction display the details pass in transaction id

  // get menu items
  // DONE as get_menu()
  public ResultSet select_menu() {
    String sql_string =
        String.format(
            """
            SELECT * FROM menu;
            """);

    return connect_exicute(sql_string);
  }

  // inset transations
  // DONE as add_transaction()
  public ResultSet add_transaction(
      String customer_name, String transaction_time, int employee_id, double total_price) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE add_transaction ( Varchar(255), TIMESTAMP, integer,NUMERIC(10, 2)) AS
            INSERT INTO transaction (customer_name, transaction_time, employee_id, total_price) VALUES($1,$2, $3, $4);

            EXECUTE add_transaction ('%s', %s,%d, %f);

            """,
            customer_name, transaction_time, employee_id, total_price);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // insert transation details
  // DONE as add_transaction()
  public ResultSet add_transaction_details(
      String customer_name, String transaction_time, int employee_id, double total_price) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE add_transaction_details ( Varchar(255), TIMESTAMP, integer,NUMERIC(10, 2)) AS
            INSERT INTO transaction_details (customer_name, transaction_time, employee_id, total_price) VALUES($1,$2, $3, $4);

            EXECUTE add_transaction_details ('%s', %s,%d, %f);

            """,
            customer_name, transaction_time, employee_id, total_price);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // update inventory quntitys
  // DONE as refill_inventory()
  public ResultSet refill_inventory(String ingredient_name, int quantity) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE update_inventory ( Varchar(255), integer) AS
            UPDATE ingredintes SET quantity = quantity + $2
            WHERE ingredients = $1;

            EXECUTE update_inventory ('%s', %d);

            """,
            ingredient_name, quantity);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // decrement inventory qauntitys
  // DONE as decrease_inventory()
  public ResultSet deacrese_inventory(String ingredient_name, int quantity) {

    // i am considering doing a rollback but idk if it will be worth it
    // This prepare satment is used to prevent sql injections in postgress
    String sql_string =
        String.format(
            """
            PREPARE deacrese_inventory ( Varchar(255), integer) AS
            UPDATE ingredintes SET quantity = quantity - $2
            WHERE ingredients = $1;

            EXECUTE deacrese_inventory ('%s', %d);

            """,
            ingredient_name, quantity);

    // may return null if error
    return connect_exicute(sql_string);
  }

  // insert inventory

  // insert transaction details and transactions
  // DONE as add_transaction_and_details()
  public ResultSet add_transaction_and_details(
      Transaction transaction, TransactionDetails[] details_list) {
    int transaction_id = handle_transaction(transaction);
    ResultSet result = handle_details(transaction_id, details_list);

    return connect_exicute(null);
  }

  // DONE as add_transaction_and_details()
  private int handle_transaction(Transaction transaction) {
    String sql_string =
        String.format(
            """
            PREPARE add_transaction ( Varchar(255), TIMESTAMP, integer,NUMERIC(10, 2)) AS
            INSERT INTO transactions (customer_name, transaction_time, employee_id, total_price) VALUES($1,$2, $3, $4) RETURNING transaction_id;

            EXECUTE add_transaction ('%s', '%s',%d, %f);

            """,
            transaction.customer_name,
            transaction.transaction_time,
            transaction.employee_id,
            transaction.total_price);

    // may return null if error
    int result = 0;
    try {
      result = connect_exicute(sql_string).getInt("transation_id");
    } catch (Exception e) {
      System.out.println("error: " + e);
    }
    return result;
  }

  //DONE as add_transaction_and_details()
  private ResultSet handle_details(int id, TransactionDetails[] details_list) {
    String sql_string =
        """
        PREPARE add_details (Integer, Integer) AS
        INSERT INTO transaction_details (transaction_id, item_id) VALUES ($1,$2);


        """;
    ;
    for (int i = 0; i < details_list.length; i++) {

      sql_string +=
          String.format(
              """
              EXECUTE add_details (%d, %d);

              """,
              id, details_list[i].item_id);
    }

    return connect_exicute(sql_string);
  }

  // DONE as delete_employee()
  public void delete_employee(int employee_id) throws SQLException {
    String sql = "DELETE FROM personnel WHERE employee_id = ?;";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, employee_id);
      stmt.executeUpdate();
    }
  }


  public void added_menu_item(Item added_item) throws SQLException {
    String sql = "INSERT INTO menu (item_name, item_popularity, price) VALUES (?, ?, ?, ?);";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, added_item.get_name());
      stmt.setInt(2, added_item.get_popularity());
      stmt.setDouble(3, added_item.get_price());
      stmt.executeUpdate();
    }
  }

  public void add_ingredent_map(int item_id, ArrayList<Integer> ingrednent_id_list)
      throws SQLException {
    String sql = "INSERT INTO ingredients_map (ingredients_id, item_id) VALUES (?, ?);";
    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      for (int i = 0; i < ingrednent_id_list.size(); i++) {
        stmt.setInt(1, item_id);
        stmt.setInt(2, ingrednent_id_list.get(i));
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

  // need to alter menu prices
  public void update_menu_price(int id, double price) throws SQLException {
    String sql = "UPDATE  menu SET price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setDouble(1, price);
      stmt.setInt(2, id);
      stmt.executeUpdate();
      
    }
  }



  // need to alter items on menu

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


  //need to view ingredints

  public ArrayList<Ingerdient> get_ingredints()
    throws SQLException {
    String sql = "SELECT * FROM ingredients";

    try (Connection conn = Database.getConnection(); 
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {
      ArrayList<Ingerdient> ingredints_list = new ArrayList<>();

      while (rs.next()) {
        int ingredient_id = rs.getInt("ingredient_id");
        String ingredient_name = rs.getString("ingredient_name");
        int quantity = rs.getInt("quantity");
        String category = rs.getString("category");
        // Fetch ingredients for the item
        ingredints_list.add(new Ingerdient(ingredient_name,  quantity,category, ingredient_id));
      }
      return ingredints_list;

    }
  }

  // need to alter ingredits




  // need to add ingredints
  public void update_ingredints(Ingerdient update_ingrediants)
    throws SQLException {
    String sql = "UPDATE  menu SET item_name = ? item_popularity = ? price = ? WHERE item_id = ?";

    try (Connection conn = Database.getConnection(); 
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1,update_ingrediants.get_ingredient_name());
      stmt.setInt(2,update_ingrediants.get_quantity());
      stmt.setString(3,update_ingrediants.get_category());
      stmt.setInt(4,update_ingrediants.get_ingredient_id());
      stmt.executeUpdate();
    }




  }
  // need to delete ingredints

}
