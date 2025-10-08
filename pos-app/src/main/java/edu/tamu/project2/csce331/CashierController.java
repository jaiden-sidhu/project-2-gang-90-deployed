package edu.tamu.project2.csce331;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;

import java.text.DecimalFormat;

import java.sql.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class CashierController {

    @FXML
    private Label subtotalLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button chargeButton;
    @FXML
    private VBox orderItems;
    @FXML
    private AnchorPane modificationsPopup;
    @FXML
    private AnchorPane chargePopup;
    @FXML
    private TextField customerNameField;
    @FXML
    private GridPane drinkGrid;

    private DecimalFormat df = new DecimalFormat("#0.00");

    private String currentDrinkName;
    private double currentDrinkPrice;
    private List<String> currentModifications = new ArrayList<>();

    private double subtotal = 0;

    private String[] drinkNames;
    private double[] drinkPrices;
    // Database connections and queries are provided by Database and Queries classes

    @FXML
    public void initialize() {
        loadMenuFromDB();
        populateDrinkGrid();
    }

    private void loadMenuFromDB() {
        List<String> namesList = new ArrayList<>();
        List<Double> pricesList = new ArrayList<>();
        // Use Queries.get_menu() to load menu items (uses Database.getConnection internally)
        try {
            edu.tamu.project2.csce331.Queries queries = new edu.tamu.project2.csce331.Queries();
            java.util.ArrayList<edu.tamu.project2.csce331.Item> menu = queries.get_menu();
            for (edu.tamu.project2.csce331.Item it : menu) {
                namesList.add(toTitleCase(it.get_name()));
                pricesList.add(it.get_price());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        drinkNames = namesList.toArray(new String[0]);

        drinkPrices = pricesList.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private String toTitleCase(String input) {
        StringBuilder result = new StringBuilder();
        for (String word : input.split("\\s+")) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }
        return result.toString().trim();
    }

    private void populateDrinkGrid() {
        if (drinkNames == null || drinkNames.length == 0) return;

        int col = 0, row = 0;
        for (int i = 0; i < drinkNames.length; i++) {
            String name = drinkNames[i];
            double price = drinkPrices[i];

            Button btn = new Button(name);
            btn.setPrefSize(100, 100);
            btn.setWrapText(true);
            btn.setOnAction(e -> handleDrinkSelection(name, price));

            drinkGrid.add(btn, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }

    private void handleDrinkSelection(String name, double price) {
        currentDrinkName = name;
        currentDrinkPrice = price;
        currentModifications.clear();
        modificationsPopup.setVisible(true);
    }

    @FXML
    private void selectModification(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String mod = btn.getText();

        // avoid duplicates
        currentModifications.removeIf(m -> m.equals(mod));
        currentModifications.add(mod);
    }

    @FXML
    private void confirmModifications() {
        addDrinkToOrder(currentDrinkName, currentDrinkPrice, new ArrayList<>(currentModifications));
        modificationsPopup.setVisible(false);
    }

    private void addDrinkToOrder(String name, double price, List<String> mods) {
        if (orderItems.getChildren().size() == 1 && orderItems.getChildren().get(0) instanceof Label) {
            orderItems.getChildren().clear();
        }

        VBox itemBox = new VBox(5);
        Label nameLabel = new Label(name + " - $" + df.format(price));
        Label modsLabel = new Label(String.join(", ", mods));
        modsLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #555;");
        itemBox.getChildren().addAll(nameLabel, modsLabel);

        orderItems.getChildren().add(itemBox);

        subtotal += price;
        subtotalLabel.setText("$" + df.format(subtotal));
        totalLabel.setText("$" + df.format(subtotal));
        chargeButton.setText("Charge $" + df.format(subtotal));
    }

    @FXML
    private void openPopup() {
        modificationsPopup.setVisible(true);
    }

    @FXML
    private void closePopup() {
        modificationsPopup.setVisible(false);
    }

    @FXML
    private void openChargePopup() {
        chargePopup.setVisible(true);
    }

    @FXML
    private void closeChargePopup() {
        chargePopup.setVisible(false);
    }

    @FXML
    private void confirmCharge() {
        String name = customerNameField.getText().trim();
        if (name.isEmpty()) return;
        try {
            edu.tamu.project2.csce331.Queries queries = new edu.tamu.project2.csce331.Queries();

            java.util.ArrayList<edu.tamu.project2.csce331.Item> menu = queries.get_menu();
            java.util.Map<String, edu.tamu.project2.csce331.Item> menuByName = new java.util.HashMap<>();
            for (edu.tamu.project2.csce331.Item it : menu) {
                menuByName.put(toTitleCase(it.get_name()), it);
            }

            java.util.ArrayList<edu.tamu.project2.csce331.Item> itemsForTransaction = new java.util.ArrayList<>();
            for (int i = 0; i < orderItems.getChildren().size(); i++) {
                if (!(orderItems.getChildren().get(i) instanceof VBox)) continue;
                VBox itemBox = (VBox) orderItems.getChildren().get(i);
                Label nameLabel = (Label) itemBox.getChildren().get(0);
                String itemText = nameLabel.getText();
                String drinkName = itemText.split(" - ")[0];

                edu.tamu.project2.csce331.Item menuItem = menuByName.get(drinkName);
                if (menuItem == null) {
                    try {
                        int id = queries.get_item_id(drinkName);
                        for (edu.tamu.project2.csce331.Item it : menu) {
                            if (it.get_id() == id) {
                                menuItem = it;
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        // item not found, skip
                    }
                }
                if (menuItem != null) itemsForTransaction.add(menuItem);
            }

        edu.tamu.project2.csce331.Transaction tx = new edu.tamu.project2.csce331.Transaction(
                    0,
                    name,
                    Timestamp.valueOf(LocalDateTime.now()),
                    1,
                    subtotal
            );

            queries.add_transaction_and_details(tx, itemsForTransaction);

            orderItems.getChildren().clear();
            orderItems.getChildren().add(new Label("No items yet."));
            subtotal = 0;
            subtotalLabel.setText("$0.00");
            totalLabel.setText("$0.00");
            chargeButton.setText("Charge $0.00");

            customerNameField.clear();
            chargePopup.setVisible(false);

            System.out.println("Charged order for " + name);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
