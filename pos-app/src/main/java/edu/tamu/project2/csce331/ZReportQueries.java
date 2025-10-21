package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class ZReportQueries {

    @FXML private TableView<EmployeeRow> employeeTable;
    @FXML private TableColumn<EmployeeRow, Integer> colEmpId;
    @FXML private TableColumn<EmployeeRow, Integer> colEmpTxCount;
    @FXML private TableColumn<EmployeeRow, Number> colEmpSales;

    @FXML private Text totalSalesText;
    @FXML private Text statusText;
    @FXML private TextArea signaturesArea;

    @FXML public LocalDate currentDate = null;

    @FXML
    public void initialize() {
        if (colEmpId != null) colEmpId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        if (colEmpTxCount != null) colEmpTxCount.setCellValueFactory(new PropertyValueFactory<>("txCount"));
        if (colEmpSales != null) colEmpSales.setCellValueFactory(new PropertyValueFactory<>("sales"));
    ReportState.initIfNeeded();
    currentDate = ReportState.getCurrentDate();
    loadReportForDate(currentDate);
    }

    @FXML
    private void refresh(ActionEvent e) {
    currentDate = ReportState.getCurrentDate();
    loadReportForDate(currentDate);
    }

    @FXML
    private void finalizeDay(ActionEvent e) {
        // Confirm with the user before finalizing
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


    private javafx.scene.Node anyNode() {
        if (employeeTable != null) return employeeTable;
        if (totalSalesText != null) return totalSalesText;
        return statusText;
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

    private void setError(String msg) {
        if (statusText != null) statusText.setText(msg != null ? msg : "Error");
        if (employeeTable != null) employeeTable.setItems(FXCollections.observableArrayList());
        if (totalSalesText != null) totalSalesText.setText("Error");
    }

    public static class EmployeeRow {
        private final int employeeId;
        private final int txCount;
        private final double sales;

        public EmployeeRow(int employeeId, int txCount, double sales) {
            this.employeeId = employeeId;
            this.txCount = txCount;
            this.sales = sales;
        }

        public int getEmployeeId() { return employeeId; }
        public int getTxCount() { return txCount; }
        public double getSales() { return sales; }
    }
}
