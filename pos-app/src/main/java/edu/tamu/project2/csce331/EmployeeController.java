package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


/**
 * @author Jaiden Sidhu
 * Controller class for the Employee List View, controls the interactions and logic for the employee list interface.
 **/
public class EmployeeController {
    @FXML private TextField nameField;
    @FXML private TextField payField;
    @FXML private TextField roleField;
    @FXML private TextField idField;
    @FXML private Text statusLabel;
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> colRole;
    @FXML private TableColumn<Employee, Integer> colId;
    @FXML private TableColumn<Employee, String> colEmployee;
    @FXML private TableColumn<Employee, Double> colPay;
    @FXML private AnchorPane addPopup;

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20;
    private int totalCount = 0;

    /**
     * Initializes the JavaFX controller after FXML injection. Loads menu items from the database and populates the drink grid.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().getId()).asObject());
        colEmployee.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getName()));
        colRole.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getRole()));
        colPay.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().getPay()).asObject());

        TableColumn<Employee, Void> colDelete = new TableColumn<>("Delete");
        colDelete.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Delete");

            {
                btn.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    try {
                        queries.deleteEmployee(employee.getId());
                        totalCount = queries.countEmployees();
                        // status_label.setText("Employee deleted");
                        loadPage();
                    } catch (Exception ex) {
                        // status_label.setText("Failed to delete employee: " + ex.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
        employeeTable.getColumns().add(colDelete);

        try {
            java.util.List<Employee> list = queries.getEmployee();
            ObservableList<Employee> data = FXCollections.observableArrayList(list);
            employeeTable.setItems(data);

            try {
                totalCount = queries.countEmployees();
            } catch (Exception ignored) {
                totalCount = data.size();
            }

            // status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));
        } catch (Exception e) {
            Throwable root = Database.getInitFailure();
            StringBuilder msg = new StringBuilder("DB error: ").append(e.getMessage());
            if (root != null && root.getCause() != null) {
                msg.append(" (cause: ").append(root.getCause().getClass().getSimpleName())
                   .append(" - ").append(root.getCause().getMessage()).append(")");
            }
            // status_label.setText(msg.toString());
        }
    }

    /* 
     * Loads a page of employees from the database and updates the table view.
     */
    private void loadPage() {
        try {
            List<Employee> list = queries.getEmployee();
            ObservableList<Employee> data = FXCollections.observableArrayList(list);
            employeeTable.setItems(data);
            // status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));

        } catch (Exception e) {
            // status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    /*
     * Handles the action of adding a new employee when the add button is clicked.
     */
    @FXML
    private void addEmployeeButton(){
        String name =  nameField.getText().trim();
        String role = roleField.getText().trim();
        double pay;
        try {
            pay = Double.parseDouble(payField.getText().trim());
        } catch (NumberFormatException nfe) {
            // status_label.setText("Invalid pay value");
            return;
        }
        try {
            queries.addEmployee(name, role, pay, true);
            totalCount = queries.countEmployees();
            loadPage();
            // status_label.setText("Employee added");
            closePopup();
        } catch (Exception e) {
            // status_label.setText("Failed to add employee: " + e.getMessage());
        }
    }

    /* 
     * Handles the action of deleting an employee when the delete button is clicked.
     */
    @FXML
    private void deleteEmployeeButton(){
        int id;
        try {
            id = Integer.parseInt(idField.getText().trim());
        } catch (NumberFormatException nfe) {
            // status_label.setText("Invalid ID");
            return;
        }
        try {
            queries.fireEmployee(id);
            totalCount = queries.countEmployees();
            // status_label.setText("Employee deleted");
        } catch (Exception e) {
            // status_label.setText("Failed to delete employee: " + e.getMessage());
        }
    }

    /**
     * Opens the popup for adding a new employee.
     */
    @FXML
    private void openPopup() {
        addPopup.setVisible(true);
    }

    /**
     * Closes the popup for adding a new employee.
     */
    @FXML
    private void closePopup() {
        addPopup.setVisible(false);
    }

    /**
     * This loads the products view when the products button is clicked.
     */
    @FXML
    public void goProducts() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/ManagerProducts.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the sales view when the sales button is clicked.
     */
    @FXML
    public void goSales() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/TransactionsHistory.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Transactions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the cashier view when the cashier button is clicked.
     */
    @FXML
    public void goCashier() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/CashierMenu.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the employees view when the employees button is clicked.
     */
    @FXML
    public void goEmployees() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/EmployeeList.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employees");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the x report view when the x report button is clicked.
     */
    @FXML
    public void goXReport() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/XReport.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - X Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the usage chart view when the usage chart button is clicked.
     */
    @FXML
    public void goUsageChart() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/UsageChart.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Usage Chart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This loads the sales report view when the sales report button is clicked.
     */
    @FXML
    public void goSalesReport() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/SalesReport.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Sale Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This loads the z report view when the z report button is clicked.
     */
    @FXML
    public void goZReport() 
    { 
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/tamu/project2/csce331/ZReport.fxml"));
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Z Report");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
