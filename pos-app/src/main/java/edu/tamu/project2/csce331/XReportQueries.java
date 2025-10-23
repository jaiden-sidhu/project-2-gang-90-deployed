package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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

/**
 * Controller for the X-Report view.
 * <p>
 * Displays per-transaction rows for the active report date (hour label, total, employee, customer,
 * transaction id) and provides a small admin toolset to simulate transactions for testing/demo.
 * Also honors the Z-Report reset so that X-Report can show data "since last finalize" within the same day.
 * </p>
 *
 * @author Kevin Chen
 * @version 1.0
 */
public class XReportQueries {

    @FXML private TableView<TransactionRow> xReportTable;
    @FXML private TableColumn<TransactionRow, String> colHour;
    @FXML private TableColumn<TransactionRow, Number> colSales;      
    @FXML private TableColumn<TransactionRow, Integer> colSales1;    
    @FXML private TableColumn<TransactionRow, String> colSales11;    
    @FXML private TableColumn<TransactionRow, Integer> colSales111;  

    @FXML private Text totalSalesText;          
    @FXML private Text totalTransactionsText;   

    /**
     * Initializes table bindings and admin UI, then loads the report for the active date from {@link ReportState}.
     *
     * @author Kevin Chen
     * @version 1.0
     */
    @FXML
    public void initialize() {
        // table bindings
        if (colHour != null) colHour.setCellValueFactory(new PropertyValueFactory<>("hourLabel"));
        if (colSales != null) colSales.setCellValueFactory(new PropertyValueFactory<>("total"));
        if (colSales1 != null) colSales1.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        if (colSales11 != null) colSales11.setCellValueFactory(new PropertyValueFactory<>("customer"));
        if (colSales111 != null) colSales111.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        ReportState.initIfNeeded();
        loadReportForDate(ReportState.getCurrentDate());
    }

    /**
     * Reloads the X-Report for the active date from {@link ReportState}.
     * Typically invoked by a Refresh button.
     *
     * @param e JavaFX action event (not used)
     * @author Kevin Chen
     * @version 1.0
     */
    @FXML
    private void refresh(ActionEvent e) {
        loadReportForDate(ReportState.getCurrentDate());
    }

    
    @FXML
    /**
     * Navigate to the Manage Employee screen.
     * Loads {@code /edu/tamu/project2/csce331/employee_list.fxml} into the current stage.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Products management screen.
     * Loads {@code /edu/tamu/project2/csce331/manager_products.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Cashier menu screen.
     * Loads {@code /edu/tamu/project2/csce331/cashier_menu.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Transactions history screen.
     * Loads {@code /edu/tamu/project2/csce331/transactions_history.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Employees list screen.
     * Loads {@code /edu/tamu/project2/csce331/employee_list.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Usage Chart screen.
     * Loads {@code /edu/tamu/project2/csce331/usage_chart.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Sales Report screen.
     * Loads {@code /edu/tamu/project2/csce331/sales_report.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the X-Report screen (current view).
     * Loads {@code /edu/tamu/project2/csce331/x_report.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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
    /**
     * Navigate to the Z-Report screen.
     * Loads {@code /edu/tamu/project2/csce331/z_report.fxml}.
     * @author Kevin Chen
     * @version 1.0
     */
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

    /**
     * Returns any non-null node from this controller's view to conveniently access the current
     * {@link Stage} (via node.getScene().getWindow()). Preference order: xReportTable, totalSalesText,
     * totalTransactionsText.
     *
     * @return a non-null node belonging to this scene
     * @author Kevin Chen
     * @version 1.0
     */
    private javafx.scene.Node anyNode() {
        
        if (xReportTable != null) return xReportTable;
        if (totalSalesText != null) return totalSalesText;
        return totalTransactionsText; 
    }

    
    /**
     * Loads and displays X-Report data for the given date, respecting any Z-Report reset time recorded
     * in the {@code report_state} table. Populates the transaction rows and total counters.
     *
     * @param date the report date whose [start, end) window is queried; must not be null
     * @author Kevin Chen
     * @version 1.0
     */
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

    /**
     * Reads the reset timestamp recorded by Z-Report finalize (if any).
     * Used to start X-Report from that timestamp within the same day.
     *
     * @param conn an open JDBC connection
     * @return the reset timestamp or {@code null} if none is recorded
     * @author Kevin Chen
     * @version 1.0
     */
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

    /**
     * Displays an error in totals fields.
     *
     * @param message error message for the transactions counter; if null, shows "Error".
     * @author Kevin Chen
     * @version 1.0
     */
    private void setTotalsError(String message) {
        if (totalSalesText != null) totalSalesText.setText("Error");
        if (totalTransactionsText != null) totalTransactionsText.setText(message != null ? message : "Error");
    }

    
    /**
     * Row model used by the X-Report table.
     * Holds hour label, total, employee id, customer, and transaction id.
     *
     * @author Kevin Chen
     * @version 1.0
     */
    public static class TransactionRow {
        private final String hourLabel;
        private final double total;
        private final int employeeId;
        private final String customer;
        private final int transactionId;

        /**
         * Creates a new row entry.
         * @param hourLabel hour label (e.g., 13:00)
         * @param total transaction total amount
         * @param employeeId employee identifier
         * @param customer customer name
         * @param transactionId transaction identifier
         */
        public TransactionRow(String hourLabel, double total, int employeeId, String customer, int transactionId) {
            this.hourLabel = hourLabel;
            this.total = total;
            this.employeeId = employeeId;
            this.customer = customer;
            this.transactionId = transactionId;
        }

        /** @return hour-of-day label */
        public String getHourLabel() { return hourLabel; }
        /** @return transaction total amount */
        public double getTotal() { return total; }
        /** @return employee identifier */
        public int getEmployeeId() { return employeeId; }
        /** @return customer name */
        public String getCustomer() { return customer; }
        /** @return transaction identifier */
        public int getTransactionId() { return transactionId; }
    }
}
