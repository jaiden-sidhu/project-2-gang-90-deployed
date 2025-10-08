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

    private final String DB_URL = "jdbc:postgresql://csce-315-db.engr.tamu.edu:5432/gang_90_db";
    private final String DB_USER = "gang_90";
    private final String DB_PASS = "gang_90";

    @FXML
    public void initialize() {
        loadMenuFromDB();
        populateDrinkGrid();
    }

    private void loadMenuFromDB() {
        List<String> namesList = new ArrayList<>();
        List<Double> pricesList = new ArrayList<>();

        String query = "SELECT item_name, price FROM menu ORDER BY id";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String name = rs.getString("item_name");
                double price = rs.getDouble("price");

                namesList.add(toTitleCase(name));
                pricesList.add(price);
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

        Connection conn = null;
        PreparedStatement insertTransactionStmt = null;
        PreparedStatement insertDetailStmt = null;
        PreparedStatement updatePopularityStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false);

            // 1) Insert into transactions
            String insertTransactionSQL = "INSERT INTO transactions (name, timestamp, employee_id, total_price) VALUES (?, ?, ?, ?) RETURNING id";
            insertTransactionStmt = conn.prepareStatement(insertTransactionSQL);
            insertTransactionStmt.setString(1, name);
            insertTransactionStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            insertTransactionStmt.setInt(3, 1);
            insertTransactionStmt.setDouble(4, subtotal);

            generatedKeys = insertTransactionStmt.executeQuery();

            int transactionId = -1;
            if (generatedKeys.next()) {
                transactionId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve transaction ID.");
            }

            // 2) Insert into transaction_details and update menu popularity
            String insertDetailSQL = "INSERT INTO transaction_details (transaction_id, item_id) VALUES (?, ?)";
            String updatePopularitySQL = "UPDATE menu SET item_popularity = item_popularity + 1 WHERE id = ?";

            insertDetailStmt = conn.prepareStatement(insertDetailSQL);
            updatePopularityStmt = conn.prepareStatement(updatePopularitySQL);

            for (int i = 0; i < orderItems.getChildren().size(); i++) {
                VBox itemBox = (VBox) orderItems.getChildren().get(i);
                Label nameLabel = (Label) itemBox.getChildren().get(0);
                String itemText = nameLabel.getText();
                String drinkName = itemText.split(" - ")[0];

                int itemId = -1;
                for (int j = 0; j < drinkNames.length; j++) {
                    if (drinkNames[j].equals(drinkName)) {
                        itemId = j + 1;
                        break;
                    }
                }
                if (itemId == -1) continue;

                insertDetailStmt.setInt(1, transactionId);
                insertDetailStmt.setInt(2, itemId);
                insertDetailStmt.executeUpdate();

                updatePopularityStmt.setInt(1, itemId);
                updatePopularityStmt.executeUpdate();
            }

            conn.commit();

            // 3) Reset order
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
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (insertTransactionStmt != null) insertTransactionStmt.close();
                if (insertDetailStmt != null) insertDetailStmt.close();
                if (updatePopularityStmt != null) updatePopularityStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
