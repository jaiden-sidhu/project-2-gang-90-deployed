package edu.tamu.project2.csce331;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.text.DecimalFormat;
import java.io.IOException;
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
    private Label employeeName;
    @FXML
    private Button chargeButton;
    @FXML
    private VBox order_Items;
    @FXML
    private AnchorPane modifications_popup;
    @FXML
    private AnchorPane loginPopup;
    @FXML
    private AnchorPane charge_popup;
    @FXML
    private TextField customer_name_field;
    @FXML
    private TextField loginNameField;
    @FXML
    private TextField loginIDField;
    @FXML
    private GridPane drink_grid;
    @FXML
    private Label errorLogin;
    @FXML
    private Button managerViewButton;

    private DecimalFormat df = new DecimalFormat("#0.00");

    private String current_drink_name;
    private double current_drink_price;
    private List<String> currentModifications = new ArrayList<>();

    private double subtotal = 0;

    private String[] drinkNames;
    private double[] drinkPrices;
    private int cashierID = 0;

    @FXML
    public void initialize() {
        loadMenuFromDB();
        populatedrink_grid();
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

    private void populatedrink_grid() {
        if (drinkNames == null || drinkNames.length == 0) return;

        int col = 0, row = 0;
        for (int i = 0; i < drinkNames.length; i++) {
            String name = drinkNames[i];
            double price = drinkPrices[i];

            Button btn = new Button(name);
            btn.setPrefSize(140, 140);
            btn.setWrapText(true);
            btn.setStyle("-fx-font-size: 18px; -fx-text-alignment: center; -fx-alignment: center;");
            btn.setTextAlignment(TextAlignment.CENTER);
            btn.setAlignment(Pos.CENTER);
            btn.setOnAction(e -> handleDrinkSelection(name, price));

            drink_grid.add(btn, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }

    private void handleDrinkSelection(String name, double price) {
        current_drink_name = name;
        current_drink_price = price;
        currentModifications.clear();
        resetModificationButtons();
        modifications_popup.setVisible(true);
    }

    @FXML
    private void selectModification(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String mod = btn.getText();

        javafx.scene.Node parent = btn.getParent();
        String category = "";
        if (parent instanceof HBox) {
            HBox h = (HBox) parent;
            if (!h.getChildren().isEmpty() && h.getChildren().get(0) instanceof Label) {
                category = ((Label) h.getChildren().get(0)).getText().trim();
            }
        }

        String key = category + ":" + mod;

        String existing = null;
        for (String s : currentModifications) {
            if (s.startsWith(category + ":")) {
                existing = s;
                break;
            }
        }

        if (existing != null && existing.equals(key)) {
            currentModifications.remove(existing);
            if (parent instanceof HBox) {
                for (javafx.scene.Node c : ((HBox) parent).getChildren()) {
                    if (c instanceof Button) ((Button) c).setStyle("");
                }
            }
        }
        else {
            if (existing != null) currentModifications.remove(existing);
            currentModifications.add(key);

            if (parent instanceof HBox) {
                for (javafx.scene.Node c : ((HBox) parent).getChildren()) {
                    if (c instanceof Button) {
                        Button b = (Button) c;
                        if (b == btn) {
                            b.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
                        } else {
                            b.setStyle("");
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void confirmModifications() {
        addDrinkToOrder(current_drink_name, current_drink_price, new ArrayList<>(currentModifications));
        modifications_popup.setVisible(false);
        resetModificationButtons();
    }

    @FXML
    private void confirmLogin() {
        String enteredName = loginNameField.getText().trim();
        String enteredID = loginIDField.getText().trim();

        errorLogin.setText("");

        if (enteredName.isEmpty() || enteredID.isEmpty()) {
            errorLogin.setText("Please enter both Name and Employee ID.");
            return;
        }

        try {
            edu.tamu.project2.csce331.Queries queries = new edu.tamu.project2.csce331.Queries();
            ArrayList<Employee> employees = queries.get_employee();
            boolean matchFound = false;

            for (Employee emp : employees) {
                if (emp.get_name().equalsIgnoreCase(enteredName) &&
                    String.valueOf(emp.get_id()).equals(enteredID)) {

                    cashierID = emp.get_id();
                    loginPopup.setVisible(false);
                    matchFound = true;
                    employeeName.setText("Hello, " + emp.get_name());
                    if (emp.get_role().equals("manager")) {
                        managerViewButton.setVisible(true);
                    }
                    else {
                        managerViewButton.setVisible(false);
                    }
                    break;
                }
            }

            if (!matchFound) {
                errorLogin.setText("Login failed. Try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            errorLogin.setText("Database error occurred.");
        }
    }

    private void addDrinkToOrder(String name, double price, List<String> mods) {
        if (order_Items.getChildren().size() == 1 && order_Items.getChildren().get(0) instanceof Label) {
            order_Items.getChildren().clear();
        }

        VBox itemBox = new VBox(5);
        Label nameLabel = new Label(name + " - $" + df.format(price));
        Label modsLabel = new Label(String.join(", ", mods));
        modsLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #555;");
        itemBox.getChildren().addAll(nameLabel, modsLabel);

        order_Items.getChildren().add(itemBox);

        recalcTotal();
    }

    private void recalcTotal() {
        double sum = 0.0;
        for (javafx.scene.Node n : order_Items.getChildren()) {
            if (!(n instanceof VBox)) continue;
            VBox itemBox = (VBox) n;
            if (itemBox.getChildren().isEmpty()) continue;
            javafx.scene.Node first = itemBox.getChildren().get(0);
            if (!(first instanceof Label)) continue;
            String text = ((Label) first).getText();
            int dollar = text.lastIndexOf('$');
            if (dollar >= 0 && dollar + 1 < text.length()) {
                String num = text.substring(dollar + 1).replaceAll("[^0-9.\\-]", "");
                try {
                    sum += Double.parseDouble(num);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        this.subtotal = sum;
        subtotalLabel.setText(df.format(subtotal));
        totalLabel.setText(df.format(subtotal));
        chargeButton.setText("Charge " + df.format(subtotal));
    }

    private void resetModificationButtons() {
        if (modifications_popup == null) return;
        for (javafx.scene.Node child : modifications_popup.getChildren()) {
            if (child instanceof VBox) {
                VBox v = (VBox) child;
                for (javafx.scene.Node row : v.getChildren()) {
                    if (row instanceof HBox) {
                        HBox h = (HBox) row;
                        for (javafx.scene.Node c : h.getChildren()) {
                            if (c instanceof Button) ((Button) c).setStyle("");
                        }
                    } else if (row instanceof Button) {
                        ((Button) row).setStyle("");
                    }
                }
            }
        }
    }

    @FXML
    private void openPopup() {
        modifications_popup.setVisible(true);
    }

    @FXML
    private void closePopup() {
        modifications_popup.setVisible(false);
    }

    @FXML
    private void openChargePopup() {
        charge_popup.setVisible(true);
    }

    @FXML
    private void closeChargePopup() {
        charge_popup.setVisible(false);
    }

    @FXML
    private void openLoginPopup() {
        loginPopup.setVisible(true);
    }

    @FXML
    private void closeLoginPopup() {
        loginPopup.setVisible(false);
    }

    @FXML
    private void confirmCharge() {
        String name = customer_name_field.getText().trim();
        if (name.isEmpty()) return;
        try {
            edu.tamu.project2.csce331.Queries queries = new edu.tamu.project2.csce331.Queries();

            java.util.ArrayList<edu.tamu.project2.csce331.Item> menu = queries.get_menu();
            java.util.Map<String, edu.tamu.project2.csce331.Item> menuByName = new java.util.HashMap<>();
            for (edu.tamu.project2.csce331.Item it : menu) {
                menuByName.put(toTitleCase(it.get_name()), it);
            }

            java.util.ArrayList<edu.tamu.project2.csce331.Item> itemsForTransaction = new java.util.ArrayList<>();
            for (int i = 0; i < order_Items.getChildren().size(); i++) {
                if (!(order_Items.getChildren().get(i) instanceof VBox)) continue;
                VBox itemBox = (VBox) order_Items.getChildren().get(i);
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
                    cashierID,
                    subtotal
            );

            queries.add_transaction_and_details(tx, itemsForTransaction);

            order_Items.getChildren().clear();
            order_Items.getChildren().add(new Label("No items yet."));
            subtotal = 0;
            subtotalLabel.setText("0.00");
            totalLabel.setText("0.00");
            chargeButton.setText("Charge 0.00");

            customer_name_field.clear();
            charge_popup.setVisible(false);

            System.out.println("Charged order for " + name);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_products() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/manager_products.fxml"));
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_x_report() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/x_report.fxml"));
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - X Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_z_report() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/z_report.fxml"));
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Z Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
