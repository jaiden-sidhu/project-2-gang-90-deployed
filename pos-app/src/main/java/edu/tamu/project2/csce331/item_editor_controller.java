package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class item_editor_controller {

    @FXML private TextField name_field;
    @FXML private TextField popularity_field;
    @FXML private TextField price_field;
    @FXML private TextField quantity_field;

    @FXML private TableView<Ingredient> ingredients_table;
    @FXML private TableColumn<Ingredient, String> ing_name_col;
    @FXML private TableColumn<Ingredient, Number> ing_qty_col;
    @FXML private TableColumn<Ingredient, String> ing_cat_col;

    @FXML private TableView<Ingredient> item_ingredients_table;
    @FXML private TableColumn<Ingredient, String> item_ing_name_col;
    @FXML private TableColumn<Ingredient, Number> item_ing_qty_col;
    @FXML private TableColumn<Ingredient, String> item_ing_cat_col;

    private products_controller.Product product_ref;
    private Queries queries_ref;
    private boolean seasonal;

    private final ObservableList<Ingredient> all_ingredients = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> item_ingredients = FXCollections.observableArrayList();

    public void set_context(products_controller.Product product, Queries queries, boolean is_seasonal) 
    {
        this.product_ref = product;
        this.queries_ref = queries;
        this.seasonal = is_seasonal;

        name_field.setText(product_ref.get_name());
        popularity_field.setText(Integer.toString(product_ref.get_quantity()));
        price_field.setText(Double.toString(product_ref.get_price()));
        quantity_field.setText(Integer.toString(product_ref.get_quantity()));

        ing_name_col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get_ingredient_name()));
        ing_qty_col.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().get_quantity()));
        ing_cat_col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get_category()));

        item_ing_name_col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get_ingredient_name()));
        item_ing_qty_col.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().get_quantity()));
        item_ing_cat_col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get_category()));


        try 
        {
            all_ingredients.setAll(queries_ref.get_ingredients());
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, "Failed to load ingredients", ButtonType.OK).showAndWait();
        }

        try 
        {
            item_ingredients.setAll(queries_ref.get_item_ingredients(product_ref.get_id()));
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
        }

        ingredients_table.setItems(all_ingredients);
        item_ingredients_table.setItems(item_ingredients);
    }

    @FXML
    public void handle_add_ingredient() 
    {
        Ingredient sel = ingredients_table.getSelectionModel().getSelectedItem();

        if(sel == null)
        {
            return;
        }
        try 
        {
            queries_ref.add_ingredient_to_item(product_ref.get_id(), sel.get_ingredient_id());

            if (!item_ingredients.contains(sel)) 
            {
                item_ingredients.add(sel);
            }
        } 
        catch(Exception e) 
        {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, "Failed to add ingredient", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void handle_remove_ingredient() 
    {
        Ingredient sel = item_ingredients_table.getSelectionModel().getSelectedItem();

        if (sel == null) 
        {
            return;
        }
        try 
        {
            queries_ref.remove_ingredient_from_item(product_ref.get_id(), sel.get_ingredient_id());
            item_ingredients.remove(sel);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, "Failed to remove ingredient", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void handle_save() 
    {
        String name = name_field.getText() == null ? "" : name_field.getText().trim();
        String pop_str = popularity_field.getText() == null ? "" : popularity_field.getText().trim();
        String price_str = price_field.getText() == null ? "" : price_field.getText().trim();
        String qty_str = quantity_field.getText() == null ? "" : quantity_field.getText().trim();

        if (name.isEmpty() || pop_str.isEmpty() || price_str.isEmpty() || qty_str.isEmpty()) 
        {
            new Alert(Alert.AlertType.INFORMATION, "Missing fields", ButtonType.OK).showAndWait();
            return;
        }
        try 
        {
            int popularity = Integer.parseInt(pop_str);
            double price = Double.parseDouble(price_str);
            int quantity = Integer.parseInt(qty_str);

            product_ref.name_property().set(name);
            product_ref.price_property().set(price);
            product_ref.quantity_property().set(quantity);

            try 
            {
                queries_ref.update_menu_price(product_ref.get_id(), price);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            ((Stage) name_field.getScene().getWindow()).close();
        } 
        catch (NumberFormatException nfe) 
        {
            new Alert(Alert.AlertType.INFORMATION, "Invalid types", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void handle_cancel() 
    {
        ((Stage) name_field.getScene().getWindow()).close();
    }
}
