package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** 
 * Manages the Z-Report queries and handles the logic for finalizing the day
 * This class is able to populate the data and finalize the report for that day by reseting
 * both Z-report and X-report.
 * 
 * @author Kevin Chen
 * @version 1.0
*/
public class ZReportQueries {

    /** Table of per-employee aggregates for the selected date. */
    @FXML private TableView<EmployeeRow> employeeTable;
    /** Column showing the employee id. */
    @FXML private TableColumn<EmployeeRow, Integer> colEmpId;
    /** Column showing the number of transactions handled by the employee. */
    @FXML private TableColumn<EmployeeRow, Integer> colEmpTxCount;
    /** Column showing the total sales amount handled by the employee. */
    @FXML private TableColumn<EmployeeRow, Number> colEmpSales;

    /** Formatted total sales for the selected date. */
    @FXML private Text totalSalesText;
    /** Status or error text for the view. */
    @FXML private Text statusText;
    /** Optional text area to capture manager/cashier signatures. */
    @FXML private TextArea signaturesArea;

    /** The date currently shown in the Z-Report view. Managed by {@link ReportState}. */
    @FXML public LocalDate currentDate = null;

    /**
     * An initial load of the data in order to populate the table for the current date. 
     * This calls {@code loadReportForDate()} in order to load the transactions for that current date
     * 
     * @author Kevin Chen
     * @version 1.0
     */
    @FXML
    public void initialize() {
        if (colEmpId != null) colEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        if (colEmpTxCount != null) colEmpTxCount.setCellValueFactory(new PropertyValueFactory<>("txCount"));
        if (colEmpSales != null) colEmpSales.setCellValueFactory(new PropertyValueFactory<>("sales"));
    ReportState.initIfNeeded();
    currentDate = ReportState.getCurrentDate();
    loadReportForDate(currentDate);
    }

    /**
     * A function that runs whenever the refresh button is pressed in order to load the data for the current date.
     * This is mainly used in case the table is missing data.
     * 
     * @author Kevin Chen
     */
    @FXML
    private void refresh() {
    currentDate = ReportState.getCurrentDate();
    loadReportForDate(currentDate);
    }

    /**
     * This function is called once the user presses the Finalize button. 
     * Once pressed it will ask for a confirmation.
     * If the user says yes, it will close out the report and reset it for the next day.
     * 
     * @return returns nothing if the user denies confirmation
     * @author Kevin Chen
     * @version 1.0
     */
    @FXML
    private void finalizeDay() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Finalize today's Z-Report? This will reset X-Report totals until new sales occur.",
                ButtonType.CANCEL, ButtonType.OK);
        confirm.setHeaderText("Finalize Z-Report");
        confirm.setTitle("Confirm Finalize");
        ButtonType result = confirm.showAndWait().orElse(ButtonType.CANCEL);
        if (result != ButtonType.OK) return;

    ReportState.incrementDay();
    currentDate = ReportState.getCurrentDate();
    if (statusText != null) statusText.setText("Z-Report finalized. Showing date: " + currentDate);
    loadReportForDate(currentDate);
    }

    /**
     * Returns any non-null node from this controller's view to conveniently
     * access the current {@link Stage} (via node.getScene().getWindow()).
     * Preference order: employeeTable, totalSalesText, statusText.
     *
     * @return a non-null node belonging to this scene
     * @author Kevin Chen
     * @version 1.0
     */
    private javafx.scene.Node anyNode() {
        if (employeeTable != null) return employeeTable;
        if (totalSalesText != null) return totalSalesText;
        return statusText;
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
     * Navigate to the X-Report screen.
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
     * Navigate to the Z-Report screen (current view).
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
     * Loads and displays Z-Report data for the given date.
     * <ul>
     *   <li>Computes total sales for the day.</li>
     *   <li>Aggregates per-employee transaction count and sales.</li>
     *   <li>Populates the table and total sales field on success.</li>
     *   <li>Shows an error message and clears UI elements on failure.</li>
     * </ul>
     * This method performs SQL access on the calling thread; call from the JavaFX Application
     * Thread or offload to a background thread as needed for large datasets.
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

        String totalSql = "SELECT COALESCE(SUM(total_price),0) AS total_sales\n" +
                "FROM transactions\n" +
                "WHERE transaction_time >= ? AND transaction_time < ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(totalSql)) {
            ps.setTimestamp(1, tsStart);
            ps.setTimestamp(2, tsEnd);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalSales = rs.getDouble("total_sales");
                }
            }
        } catch (Exception ex) {
            setError(ex.getMessage());
            return;
        }

        if (totalSalesText != null) totalSalesText.setText(String.format("$%.2f", totalSales));

        String empSql = "SELECT employee_id, COUNT(*) AS tx_count, COALESCE(SUM(total_price),0) AS sales\n" +
                "FROM transactions\n" +
                "WHERE transaction_time >= ? AND transaction_time < ?\n" +
                "GROUP BY employee_id\n" +
                "ORDER BY employee_id";

        List<EmployeeRow> rows = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(empSql)) {
            ps.setTimestamp(1, tsStart);
            ps.setTimestamp(2, tsEnd);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int empId = rs.getInt("employee_id");
                    int txCount = rs.getInt("tx_count");
                    double sales = rs.getDouble("sales");
                    rows.add(new EmployeeRow(empId, txCount, sales));
                }
            }
        } catch (Exception ex) {
            setError(ex.getMessage());
            return;
        }

        if (employeeTable != null) {
            ObservableList<EmployeeRow> data = FXCollections.observableArrayList(rows);
            employeeTable.setItems(data);
        }
        if (statusText != null) statusText.setText("");
    }

    /**
     * Displays an error message in the status text area and resets the UI list fields.
     *
     * @param msg error message; if null, a generic "Error" is shown
     * @author Kevin Chen
     * @version 1.0
     */
    private void setError(String msg) {
        if (statusText != null) statusText.setText(msg != null ? msg : "Error");
        if (employeeTable != null) employeeTable.setItems(FXCollections.observableArrayList());
        if (totalSalesText != null) totalSalesText.setText("Error");
    }

    /**
     * Simple row model for the employee aggregate view. Holds a single employee's
     * transaction count and total sales for the selected date.
     * @author Kevin Chen
     * @version 1.0
     */
    public static class EmployeeRow {
        private final int employeeId;
        private final int txCount;
        private final double sales;

        /**
         * Creates a new EmployeeRow aggregate.
         * @param employeeId employee identifier
         * @param txCount number of transactions handled by this employee
         * @param sales total sales amount accumulated by this employee
         * @author Kevin Chen
         * @version 1.0
         */
        public EmployeeRow(int employeeId, int txCount, double sales) {
            this.employeeId = employeeId;
            this.txCount = txCount;
            this.sales = sales;
        }

        /** @return the employee identifier 
         * @author Kevin Chen
         * @version 1.0
        */
        public int getEmployeeId() { return employeeId; }
        /** @return the transaction count for the employee 
         * @author Kevin Chen
         * @version 1.0
        */
        public int getTxCount() { return txCount; }
        /** @return the total sales amount for the employee 
         * @author Kevin Chen
         * @version 1.0
        */
        public double getSales() { return sales; }
    }
}
