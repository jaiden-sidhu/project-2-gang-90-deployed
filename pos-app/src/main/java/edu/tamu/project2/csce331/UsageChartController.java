package edu.tamu.project2.csce331;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class UsageChartController {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Timestamp> colTime;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, Integer> colEmployee;
    @FXML private TableColumn<Transaction, Double> colCost;
    @FXML private TableColumn<Transaction, String> colCustomer;
    @FXML private Pagination pagination;
    @FXML private Text statusLabel;


    //added
    @FXML private TableView<Map<String, Integer>> usageTable; //TODO: add IngredientUsage object
    @FXML private TableColumn<Map<String, Integer>, String> colIngredientUsage;
    @FXML private TableColumn<Map<String, Integer>, Integer> colUsed;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    @FXML private BarChart<String, Integer> usageBarChart;
    

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20; // fits better visually than 50; adjustable
    private int totalCount = 0;

    @FXML
    public void initialize() {
        // Configure columns
        colIngredientUsage.setCellValueFactory( new MapValueFactory<String>("name"));
        colUsed.setCellValueFactory(new MapValueFactory("amount"));
    

        try {
            totalCount = queries.count_transactions();//TODO: change query when avalibale
        } catch (Exception e) {
            Throwable root = Database.getInitFailure();
            StringBuilder msg = new StringBuilder("DB error: ").append(e.getMessage());
            if (root != null && root.getCause() != null) {
                msg.append(" (cause: ").append(root.getCause().getClass().getSimpleName())
                   .append(" - ").append(root.getCause().getMessage()).append(")");
            }
            statusLabel.setText(msg.toString());
            if (pagination != null) {
                pagination.setDisable(true);
            }
            return;
        }
        
        //loadPage(0);
    }

    @FXML
    



    private void loadPage(Timestamp begin_timestamp, Timestamp end_timeStamp) {
        try {
            Map<String, Integer> list = queries.get_ingredient_usage(begin_timestamp, end_timeStamp);//insert quiery
            
            ObservableList<Map<String, Integer>> data = FXCollections.observableArrayList(list);
            usageTable.setItems(data);
            statusLabel.setText(String.format("Showing %d of %d total", data.size(), totalCount));
            usageBarChart.getData().clear();

            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Total Usage");
            
            for (String entry : list.keySet()) {
                series.getData().add(new XYChart.Data<>(entry, list.get(entry)));
            }

            usageBarChart.getData().add(series);

        } catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML 
    public void apply_dates(){
        try{
            LocalDate end_date = endDate.getValue();
            LocalDate start_date = startDate.getValue();
            Timestamp start_time = new Timestamp(start_date.toEpochDay());
            Timestamp end_time = new Timestamp(end_date.toEpochDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_today(){
        try{
            LocalDate start_date = LocalDate.now();
            LocalDate end_date = start_date.minusDays(1);
            Timestamp start_time = new Timestamp(start_date.toEpochDay());
            Timestamp end_time = new Timestamp(end_date.toEpochDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }


    }

    @FXML
    public void apply_week(){
        try{
            LocalDate start_date = LocalDate.now();
            LocalDate end_date = start_date.minusDays(7);
            Timestamp start_time = new Timestamp(start_date.toEpochDay());
            Timestamp end_time = new Timestamp(end_date.toEpochDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_30_days(){
        try{
            LocalDate start_date = LocalDate.now();
            LocalDate end_date = start_date.minusWeeks(30);
            Timestamp start_time = new Timestamp(start_date.toEpochDay());
            Timestamp end_time = new Timestamp(end_date.toEpochDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_month(){
        try{
            LocalDate start_date = LocalDate.now();
            LocalDate end_date = start_date.minusMonths(1);
            Timestamp start_time = new Timestamp(start_date.toEpochDay());
            Timestamp end_time = new Timestamp(end_date.toEpochDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            statusLabel.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void go_manage_employee() 
    { 
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) statusLabel.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Employee");
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_products() 
    { 
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/manager_products.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) statusLabel.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Manager - Products");
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void go_cashier() 
    { 
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/cashier_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) statusLabel.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
