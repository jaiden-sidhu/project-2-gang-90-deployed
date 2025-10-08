package edu.tamu.project2.csce331;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
// Using Text in FXML for status label
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Timestamp;
import java.util.List;

public class EmployeeController {
    @FXML private TextField name_field;
    @FXML private TextField pay_field;
    @FXML private TextField role_feild;
    @FXML private TextField id_feild;
    @FXML private TableView<Employee> employee_table;
    @FXML private TableColumn<Employee, String> col_role;
    @FXML private TableColumn<Employee, Integer> col_id;
    @FXML private TableColumn<Employee, String> col_employee;
    @FXML private TableColumn<Employee, Double> col_pay;
    @FXML private Pagination pagination;
    @FXML private Text status_label;

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20; // fits better visually than 50; adjustable
    private int totalCount = 0;

    @FXML
    public void initialize() {
        // Configure columns
        col_employee.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_id.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        col_role.setCellValueFactory(new PropertyValueFactory<>("role"));
        col_pay.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        

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
        
        load_page();
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
        String role = role_feild.getText().trim();
        double pay = Double.parseDouble(pay_field.getText().trim());
        try {
            queries.add_employee(name, role, pay);
            load_page();
        } catch (Exception e) {
        }
        



    }

    @FXML
    private void delete_employee_button(){
        int id = Integer.parseInt(id_feild.getText().trim());
        try {
            queries.delete_employee(id);
        } catch (Exception e) {
        }
    }
}
