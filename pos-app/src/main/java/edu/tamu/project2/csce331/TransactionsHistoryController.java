package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
// Using Text in FXML for status label
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class TransactionsHistoryController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Timestamp> colTime;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, Integer> colEmployee;
    @FXML private TableColumn<Transaction, Double> colCost;
    @FXML private TableColumn<Transaction, String> colCustomer;
    @FXML private Pagination pagination;
    @FXML private Text status_label;

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20; // fits better visually than 50; adjustable
    private int totalCount = 0;

    @FXML
    public void initialize() {
        // Configure columns
        colTime.setCellValueFactory(new PropertyValueFactory<>("transaction_time"));
        colId.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));

        try {
            totalCount = queries.count_transactions();
        } catch (Exception e) {
            Throwable root = Database.getInitFailure();
            StringBuilder msg = new StringBuilder("DB error: ").append(e.getMessage());
            if (root != null && root.getCause() != null) {
                msg.append(" (cause: ").append(root.getCause().getClass().getSimpleName())
                   .append(" - ").append(root.getCause().getMessage()).append(")");
            }
            status_label.setText(msg.toString());
            if (pagination != null) {
                pagination.setDisable(true);
            }
            return;
        }
        int pageCount = (int) Math.ceil(totalCount / (double) PAGE_SIZE);
        if (pageCount == 0) pageCount = 1;
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.currentPageIndexProperty().addListener((obs, oldV, newV) -> loadPage(newV.intValue()));
        loadPage(0);
    }

    private void loadPage(int pageIndex) {
        try {
            List<Transaction> list = queries.get_transactions(pageIndex, PAGE_SIZE);
            ObservableList<Transaction> data = FXCollections.observableArrayList(list);
            transactionsTable.setItems(data);
            status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));
        } catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void go_products() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/manager_products.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_sales() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/transactions_history.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Transactions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_cashier() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/cashier_menu.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_employees() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employees");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     @FXML
    public void go_x_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/x_report.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - X Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_usage_chart() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/usage_chart.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Usage Chart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void go_sales_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/sales_report.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Sale Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void go_z_report() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/z_report.fxml"));
            Stage stage = (Stage) status_label.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Z Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
