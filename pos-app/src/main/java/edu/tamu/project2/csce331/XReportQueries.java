package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
// 
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
// 
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
// 
import java.util.Objects;

public class XReportQueries {

    @FXML private TableView<TransactionRow> xReportTable;
    @FXML private TableColumn<TransactionRow, String> colHour;
    @FXML private TableColumn<TransactionRow, Number> colSales;      
    @FXML private TableColumn<TransactionRow, Integer> colSales1;    
    @FXML private TableColumn<TransactionRow, String> colSales11;    
    @FXML private TableColumn<TransactionRow, Integer> colSales111;  

    @FXML private Text totalSalesText;          
    @FXML private Text totalTransactionsText;   

    // Admin UI controls
    @FXML private VBox adminBox;
    @FXML private TextField adminCustomerField;
    @FXML private ComboBox<EmployeeOption> adminEmployeeCombo;
    @FXML private ComboBox<ItemOption> adminItemCombo;
    @FXML private Spinner<Integer> adminQtySpinner;
    @FXML private ListView<LineItem> adminLinesList;
    @FXML private Text adminTotalText;
    @FXML private Label adminStatusLabel;

    @FXML
    public void initialize() {
        // table bindings
        if (colHour != null) colHour.setCellValueFactory(new PropertyValueFactory<>("hourLabel"));
        if (colSales != null) colSales.setCellValueFactory(new PropertyValueFactory<>("total"));
        if (colSales1 != null) colSales1.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        if (colSales11 != null) colSales11.setCellValueFactory(new PropertyValueFactory<>("customer"));
        if (colSales111 != null) colSales111.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        // admin init
        initAdminUi();
    ReportState.initIfNeeded();
    loadReportForDate(ReportState.getCurrentDate());
    }

    @FXML
    private void refresh(ActionEvent e) {
        loadReportForDate(ReportState.getCurrentDate());
    }

    
    @FXML
    public void go_manage_employee() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employee");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_products() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/manager_products.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_cashier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/cashier_menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_sales() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/transactions_history.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Transactions");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_employees() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employees");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_usage_chart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/usage_chart.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Usage Chart");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_sales_report() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/sales_report.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Sales Report");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_x_report() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/x_report.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void go_z_report() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/z_report.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) anyNode().getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private javafx.scene.Node anyNode() {
        
        if (xReportTable != null) return xReportTable;
        if (totalSalesText != null) return totalSalesText;
        return totalTransactionsText; 
    }

    
    private void loadReportForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        Timestamp tsStart = Timestamp.valueOf(start);
        Timestamp tsEnd = Timestamp.valueOf(end);

        double totalSales = 0.0;
        int totalTx = 0;

        String txSql = "SELECT transaction_time, total_price, employee_id, customer_name, transaction_id\n" +
                "FROM transactions\n" +
                "WHERE transaction_time >= ? AND transaction_time < ?\n" +
                "ORDER BY transaction_time ASC";

        List<TransactionRow> rows = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            // Respect Z-Report finalize: if a reset time is recorded for today, start from there
            Timestamp resetAt = getResetAt(conn);
            Timestamp effectiveStart = tsStart;
            if (resetAt != null && resetAt.after(tsStart) && resetAt.before(tsEnd)) {
                effectiveStart = resetAt;
            }

            try (PreparedStatement ps = conn.prepareStatement(txSql)) {
                ps.setTimestamp(1, effectiveStart);
            ps.setTimestamp(2, tsEnd);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("transaction_time");
                        LocalDateTime ldt = ts.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        String hourLabel = String.format("%02d:00", ldt.getHour());
                        double total = rs.getDouble("total_price");
                        int employeeId = rs.getInt("employee_id");
                        String customer = rs.getString("customer_name");
                        int transactionId = rs.getInt("transaction_id");

                        rows.add(new TransactionRow(hourLabel, total, employeeId, customer, transactionId));
                        totalSales += total;
                        totalTx++;
                    }
                }
            }
        } catch (Exception ex) {
            setTotalsError(ex.getMessage());
            if (xReportTable != null) xReportTable.setItems(FXCollections.observableArrayList());
            return;
        }

        
        if (totalSalesText != null) totalSalesText.setText(String.format("$%.2f", totalSales));
        if (totalTransactionsText != null) totalTransactionsText.setText(Integer.toString(totalTx));
        if (xReportTable != null) {
            ObservableList<TransactionRow> data = FXCollections.observableArrayList(rows);
            xReportTable.setItems(data);
        }
    }

    // Read reset timestamp recorded by Z-Report finalize
    private Timestamp getResetAt(Connection conn) {
        String sql = "SELECT value FROM report_state WHERE key = 'x_reset_at'";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getTimestamp(1);
            }
        } catch (SQLException ignore) {
            // table may not exist yet; treat as no reset
        }
        return null;
    }

    private void setTotalsError(String message) {
        if (totalSalesText != null) totalSalesText.setText("Error");
        if (totalTransactionsText != null) totalTransactionsText.setText(message != null ? message : "Error");
    }

    
    public static class TransactionRow {
        private final String hourLabel;
        private final double total;
        private final int employeeId;
        private final String customer;
        private final int transactionId;

        public TransactionRow(String hourLabel, double total, int employeeId, String customer, int transactionId) {
            this.hourLabel = hourLabel;
            this.total = total;
            this.employeeId = employeeId;
            this.customer = customer;
            this.transactionId = transactionId;
        }

        public String getHourLabel() { return hourLabel; }
        public double getTotal() { return total; }
        public int getEmployeeId() { return employeeId; }
        public String getCustomer() { return customer; }
        public int getTransactionId() { return transactionId; }
    }

    // ===== Admin Tools =====
    private void initAdminUi() {
        if (adminBox == null) return; // FXML block not present
        // keep hidden by default
        adminBox.setVisible(false);
        adminBox.setManaged(false);

        // Toggle with Cmd+Shift+A
        adminBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, evt -> {
                    if (evt.isMetaDown() && evt.isShiftDown() && evt.getCode() == javafx.scene.input.KeyCode.A) {
                        boolean show = !adminBox.isVisible();
                        adminBox.setVisible(show);
                        adminBox.setManaged(show);
                        evt.consume();
                    }
                });
            }
        });

        // quantity spinner
        if (adminQtySpinner != null) {
            adminQtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }

        // load combos
        loadEmployeesIntoCombo();
        loadMenuIntoCombo();

        // list placeholder
        if (adminLinesList != null) {
            adminLinesList.setPlaceholder(new Label("No items yet"));
        }
        updateAdminTotal();
    }

    private void loadEmployeesIntoCombo() {
        if (adminEmployeeCombo == null) return;
        try {
            Queries q = new Queries();
            List<Employee> emps = q.get_employee();
            List<EmployeeOption> options = new ArrayList<>();
            for (Employee e : emps) {
                options.add(new EmployeeOption(e.get_id(), e.get_name()));
            }
            adminEmployeeCombo.setItems(FXCollections.observableArrayList(options));
            if (!options.isEmpty()) adminEmployeeCombo.getSelectionModel().select(0);
        } catch (Exception ex) {
            setAdminError("Load employees failed: " + ex.getMessage());
        }
    }

    private void loadMenuIntoCombo() {
        if (adminItemCombo == null) return;
        try {
            Queries q = new Queries();
            List<Item> menu = q.get_menu();
            List<ItemOption> options = new ArrayList<>();
            for (Item it : menu) {
                options.add(new ItemOption(it.get_id(), it.get_name(), it.get_price()));
            }
            adminItemCombo.setItems(FXCollections.observableArrayList(options));
            if (!options.isEmpty()) adminItemCombo.getSelectionModel().select(0);
        } catch (Exception ex) {
            setAdminError("Load menu failed: " + ex.getMessage());
        }
    }

    @FXML
    private void adminAddLine(ActionEvent e) {
        if (adminItemCombo == null || adminQtySpinner == null || adminLinesList == null) return;
        ItemOption opt = adminItemCombo.getSelectionModel().getSelectedItem();
        if (opt == null) return;
        int qty = adminQtySpinner.getValue() != null ? adminQtySpinner.getValue() : 1;
        adminLinesList.getItems().add(new LineItem(opt, qty));
        updateAdminTotal();
    }

    @FXML
    private void adminRemoveSelected(ActionEvent e) {
        if (adminLinesList == null) return;
        int idx = adminLinesList.getSelectionModel().getSelectedIndex();
        if (idx >= 0) {
            adminLinesList.getItems().remove(idx);
            updateAdminTotal();
        }
    }

    @FXML
    private void adminClear(ActionEvent e) {
        if (adminCustomerField != null) adminCustomerField.clear();
        if (adminLinesList != null) adminLinesList.getItems().clear();
        updateAdminTotal();
        setAdminStatus("");
    }

    @FXML
    private void adminSave(ActionEvent e) {
        if (adminEmployeeCombo == null || adminLinesList == null) return;
        EmployeeOption emp = adminEmployeeCombo.getSelectionModel().getSelectedItem();
        if (emp == null) { setAdminError("Select employee"); return; }
        if (adminLinesList.getItems().isEmpty()) { setAdminError("Add at least one item"); return; }

        String customer = adminCustomerField != null ? adminCustomerField.getText() : null;
        if (customer == null || customer.isBlank()) customer = "Walk-in";

        double total = adminLinesList.getItems().stream()
                .mapToDouble(li -> li.qty * li.item.price)
                .sum();

    // Build Transaction and item list with the active report date
    LocalDate reportDate = ReportState.getCurrentDate();
    LocalDateTime reportDateTime = reportDate.atTime(LocalTime.now());
    Timestamp txTimestamp = Timestamp.valueOf(reportDateTime);
    Transaction tx = new Transaction(0, customer, txTimestamp, emp.id, total);

        ArrayList<Item> items = new ArrayList<>();
        for (LineItem li : adminLinesList.getItems()) {
            for (int i = 0; i < li.qty; i++) {
                items.add(new Item(li.item.id, li.item.name, li.item.price));
            }
        }

        try {
            Queries q = new Queries();
            q.add_transaction_and_details(tx, items);
            setAdminStatus("Saved ✔");
            adminClear(null);
            // refresh report for the active report date
            loadReportForDate(ReportState.getCurrentDate());
        } catch (Exception ex) {
            setAdminError("Save failed: " + ex.getMessage());
        }
    }

    private void updateAdminTotal() {
        if (adminTotalText == null || adminLinesList == null) return;
        double total = adminLinesList.getItems().stream()
                .mapToDouble(li -> li.qty * li.item.price)
                .sum();
        adminTotalText.setText(String.format("$%.2f", total));
    }

    private void setAdminError(String msg) {
        if (adminStatusLabel != null) {
            adminStatusLabel.setText(msg);
            adminStatusLabel.setStyle("-fx-text-fill: #cc0000;");
        }
    }
    private void setAdminStatus(String msg) {
        if (adminStatusLabel != null) {
            adminStatusLabel.setText(msg);
            adminStatusLabel.setStyle("-fx-text-fill: #007700;");
        }
    }

    public static class EmployeeOption {
        final int id; final String name;
        public EmployeeOption(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name + " (" + id + ")"; }
    }
    public static class ItemOption {
        final int id; final String name; final double price;
        public ItemOption(int id, String name, double price) { this.id = id; this.name = name; this.price = price; }
        @Override public String toString() { return name + String.format(" - $%.2f", price); }
    }
    public static class LineItem {
        final ItemOption item; final int qty;
        public LineItem(ItemOption item, int qty) { this.item = item; this.qty = qty; }
        @Override public String toString() { return item.name + " x" + qty + String.format(" = $%.2f", qty * item.price); }
    }
}
