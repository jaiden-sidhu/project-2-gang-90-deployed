package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.util.List;

public class EmployeeController {
    @FXML private TextField name_field;
    @FXML private TextField pay_field;
    @FXML private TextField role_field;
    @FXML private TextField id_field;
    @FXML private Text status_label;
    @FXML private TableView<Employee> employee_table;
    @FXML private TableColumn<Employee, String> col_role;
    @FXML private TableColumn<Employee, Integer> col_id;
    @FXML private TableColumn<Employee, String> col_employee;
    @FXML private TableColumn<Employee, Double> col_pay;
    @FXML private AnchorPane addPopup;

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20;
    private int totalCount = 0;

    @FXML
    public void initialize() {
        col_id.setCellValueFactory(cd -> new SimpleIntegerProperty(cd.getValue().get_id()).asObject());
        col_employee.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().get_name()));
        col_role.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().get_role()));
        col_pay.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().get_pay()).asObject());

        TableColumn<Employee, Void> col_delete = new TableColumn<>("Delete");
        col_delete.setCellFactory(param -> new javafx.scene.control.TableCell<>() {
            private final javafx.scene.control.Button btn = new javafx.scene.control.Button("Delete");

            {
                btn.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    try {
                        queries.delete_employee(employee.get_id());
                        totalCount = queries.count_employees();
                        status_label.setText("Employee deleted");
                        load_page();
                    } catch (Exception ex) {
                        status_label.setText("Failed to delete employee: " + ex.getMessage());
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
        employee_table.getColumns().add(col_delete);

        try {
            java.util.List<Employee> list = queries.get_employee();
            ObservableList<Employee> data = FXCollections.observableArrayList(list);
            employee_table.setItems(data);

            try {
                totalCount = queries.count_employees();
            } catch (Exception ignored) {
                totalCount = data.size();
            }

            status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));
        } catch (Exception e) {
            Throwable root = Database.getInitFailure();
            StringBuilder msg = new StringBuilder("DB error: ").append(e.getMessage());
            if (root != null && root.getCause() != null) {
                msg.append(" (cause: ").append(root.getCause().getClass().getSimpleName())
                   .append(" - ").append(root.getCause().getMessage()).append(")");
            }
            status_label.setText(msg.toString());
        }
    }

    private void load_page() {
        try {
            List<Employee> list = queries.get_employee();
            ObservableList<Employee> data = FXCollections.observableArrayList(list);
            employee_table.setItems(data);
            status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));

        } catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    private void add_employee_button(){
        String name =  name_field.getText().trim();
        String role = role_field.getText().trim();
        double pay;
        try {
            pay = Double.parseDouble(pay_field.getText().trim());
        } catch (NumberFormatException nfe) {
            status_label.setText("Invalid pay value");
            return;
        }
        try {
            queries.add_employee(name, role, pay);
            totalCount = queries.count_employees();
            load_page();
            status_label.setText("Employee added");
            closePopup();
        } catch (Exception e) {
            status_label.setText("Failed to add employee: " + e.getMessage());
        }
    }

    @FXML
    private void delete_employee_button(){
        int id;
        try {
            id = Integer.parseInt(id_field.getText().trim());
        } catch (NumberFormatException nfe) {
            status_label.setText("Invalid ID");
            return;
        }
        try {
            queries.delete_employee(id);
            totalCount = queries.count_employees();
            status_label.setText("Employee deleted");
        } catch (Exception e) {
            status_label.setText("Failed to delete employee: " + e.getMessage());
        }
    }

    @FXML
    private void openPopup() {
        addPopup.setVisible(true);
    }

    @FXML
    private void closePopup() {
        addPopup.setVisible(false);
    }
}
