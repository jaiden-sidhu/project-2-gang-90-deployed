package edu.tamu.project2.csce331;

import java.io.IOException;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import edu.tamu.project2.csce331.Item;
import edu.tamu.project2.csce331.Queries;

/**
    * Documentation for products_controller class.
    * @author Michael Ramirez
    * @version 4.2
*/

public class products_controller 
{

    private final Queries queries = new Queries();

    @FXML private TableView<Product> products_table;
    @FXML private TableColumn<Product, String> name_col;
    @FXML private TableColumn<Product, Double> price_col;
    @FXML private TableColumn<Product, Integer> quantity_col;
    @FXML private TableColumn<Product, Void> actions_col;

    @FXML private TextField name_field;
    @FXML private TextField price_field;
    @FXML private TextField quantity_field;

    private final ObservableList<Product> drinks = FXCollections.observableArrayList();
    private boolean season = false;

    /**
     * Initializes the controller after FXML loading.
     * <p>Sets up table cell value factories, loads items from the database (regular menu by
     * default), and wires up the per-row Modify/Remove buttons.</p>
     * <p><strong>Side effects:</strong> Populates {@link #drinks} and assigns it to
     * {@link #products_table}.</p>
     */
    @FXML
    public void initialize() 
    {
        name_col.setCellValueFactory(data -> data.getValue().name_property());
        price_col.setCellValueFactory(data -> data.getValue().price_property().asObject());
        quantity_col.setCellValueFactory(data -> data.getValue().quantity_property().asObject());

        try 
        {
            for (Item item : queries.get_menu()) 
            {
                drinks.add(new Product(item.get_name(), item.get_price(), item.get_popularity(), item.get_id()));
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            show_info("Something went wrong with the database.");
        }

        actions_col.setCellFactory(col -> new TableCell<>() 
        {
            private final Button modify_btn = new Button("Modify");
            private final Button remove_btn = new Button("Remove");
            private final HBox box = new HBox(6, modify_btn, remove_btn);

            {
                modify_btn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    String price_str = safe_trim(price_field.getText());
                    try 
                    {
                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/item_editor.fxml"));
                        javafx.scene.Parent root = loader.load();
                        edu.tamu.project2.csce331.item_editor_controller c = loader.getController();
                        c.set_context(p, queries, season);
                        javafx.stage.Stage dialog = new javafx.stage.Stage();
                        dialog.setTitle("Edit Item");
                        dialog.initOwner(name_field.getScene().getWindow());
                        dialog.initModality(javafx.stage.Modality.WINDOW_MODAL);
                        dialog.setScene(new javafx.scene.Scene(root));
                        dialog.showAndWait();
                        products_table.refresh();
                    } 
                    catch (Exception ex) 
                    {
                        ex.printStackTrace();
                        show_info("Failed to update price.");
                    }
                    //populate_form(p);
                });

                remove_btn.setOnAction(e -> {
                    Product p = getTableView().getItems().get(getIndex());
                    try 
                    {
                        if (season)
                        {
                            queries.delete_seasonal_item(p.get_id());
                        }
                        else
                        {
                            queries.delete_item(p.get_id());
                        }
                        drinks.remove(p);
                        products_table.refresh();
                    } 
                    catch (Exception ex) 
                    {
                        ex.printStackTrace();
                        show_info("Failed to delete " + p.get_name());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) 
            {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        products_table.setItems(drinks);
    }

    /**
     * Adds a new product using the values in the input fields.
     * <p>Validates non-empty fields and numeric types, writes the item to the appropriate
     * menu (regular or seasonal), and appends it to the table.</p>
     * @implNote Quantity is stored only in the table model for display; the database
     * write uses {@link Item} which does not include quantity.
     * @see #clear_inputs()
     */
    @FXML
    public void handle_save() 
    {
        String name = safe_trim(name_field.getText());
        String price_str = safe_trim(price_field.getText());
        String qty_str = safe_trim(quantity_field.getText());

        if (name.isEmpty() || price_str.isEmpty() || qty_str.isEmpty()) 
        {
            show_info("Missing fields");
            return;
        }

        double price;
        int quantity;
        try 
        {
            price = Double.parseDouble(price_str);
            quantity = Integer.parseInt(qty_str);
        } 
        catch (NumberFormatException nfe) 
        {
            show_info("Invalid  types");
            return;
        }

        try 
        {
            Item added_item = new Item(0, name, 0, price);
            int new_id;

            if (season)
            {
                new_id = queries.add_seasonal_menu_item(added_item);
            }
            else
            {
                new_id = queries.add_menu_item(added_item);
            }

            Product new_drink = new Product(name, price, quantity, new_id);
            drinks.add(new_drink);
            products_table.refresh();
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        clear_inputs();
    }

    /**
     * Clears all input fields (name, price, quantity).
     * <p><strong>Side effects:</strong> Mutates text fields; no database I/O.</p>
     */
    @FXML
    public void handle_clear() 
    { 
        clear_inputs(); 
    }

    /**
     * Navigates to the Manager → Products scene.
     * @throws RuntimeException if the FXML fails to load (wrapped {@link IOException}).
     */
    @FXML
    public void go_products() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/manager_products.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Sales History scene.
    */
    @FXML
    public void go_sales() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/transactions_history.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Transactions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Cashier Menu scene.
    */
    @FXML
    public void go_cashier() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/cashier_menu.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Manager Employees scene.
    */
    @FXML
    public void go_employees() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employees");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Manager X Report scene.
    */
    @FXML
    public void go_x_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/x_report.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - X Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Manager Usage Chart scene.
    */
    @FXML
    public void go_usage_chart() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/usage_chart.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Usage Chart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Manager Sales Report scene.
    */
    @FXML
    public void go_sales_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/sales_report.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Sale Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Navigates to the Manager Z Report scene.
    */
    @FXML
    public void go_z_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/z_report.fxml"));
            Stage stage = (Stage) name_field.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Z Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * (Deprecated) Previously used to populate the Modify form directly.
    * @param p the product to mirror into the text fields; no-op if {@code null}
    * @deprecated this method is kept for reference.
    */
    @Deprecated
    private void populate_form(Product p) 
    {
        if (p == null) return;
        name_field.setText(p.get_name());
        price_field.setText(Double.toString(p.get_price()));
        quantity_field.setText(Integer.toString(p.get_quantity()));
    }

    /**
    * Clears the name, price, and quantity inputs.
    */
    private void clear_inputs() 
    {
        name_field.clear();
        price_field.clear();
        quantity_field.clear();
    }

    /**
    * Clears the name, price, and quantity inputs.
    */
    private static String safe_trim(String s) 
    { 
        return s == null ? "" : s.trim(); 
    }

    private void show_info(String msg) 
    {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    /**
    * Immutable table model for products displayed in {@link #products_table}.
    * <p><strong>Note:</strong> Quantity here represents the display quantity/popularity
    * used in the UI. Persistence of quantity depends on the backing schema; the {@link Item}
    * entity used for writes does not include quantity.</p>
    */
    public static class Product 
    {
        private final javafx.beans.property.SimpleIntegerProperty id;
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleDoubleProperty price;
        private final javafx.beans.property.SimpleIntegerProperty quantity;

        public Product(String name, double price, int quantity, int id) 
        {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.price = new javafx.beans.property.SimpleDoubleProperty(price);
            this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
            this.id = new javafx.beans.property.SimpleIntegerProperty(id);
        }

        public int get_id() 
        { 
            return id.get(); 
        }
        public javafx.beans.property.IntegerProperty id_property() 
        { 
            return id; 
        }

        public String get_name() 
        { 
            return name.get(); 
        }
        public javafx.beans.property.StringProperty name_property() 
        { 
            return name; 
        }

        public double get_price() 
        { 
            return price.get(); 
        }
        public javafx.beans.property.DoubleProperty price_property() 
        { 
            return price; 
        }

        public int get_quantity() 
        { 
            return quantity.get(); 
        }
        public javafx.beans.property.IntegerProperty quantity_property() 
        { 
            return quantity; 
        }
    }

    /**
    * Handles the seasonal view toggle action.
    */
    @FXML
    public void do_seasonal_view()
    {
        name_col.setCellValueFactory(data -> data.getValue().name_property());
        price_col.setCellValueFactory(data -> data.getValue().price_property().asObject());
        quantity_col.setCellValueFactory(data -> data.getValue().quantity_property().asObject());
        
        if(season)
        {
            drinks.clear();
            try
            {
                for (Item item : queries.get_menu()) 
                {
                    drinks.add(new Product(item.get_name(), item.get_price(), item.get_popularity(), item.get_id()));
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
                show_info("Something went wrong with the database.");
            }
            season = false;
        }
        else
        {
            drinks.clear();
            try
            {
                for (Item item : queries.get_seasonal_menu()) 
                {
                    int season_id = -item.get_id();
                    drinks.add(new Product(item.get_name(), item.get_price(), item.get_popularity(), season_id));
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
                show_info("That is not very festive:(");
            }
            season = true;
        }
    }
}

