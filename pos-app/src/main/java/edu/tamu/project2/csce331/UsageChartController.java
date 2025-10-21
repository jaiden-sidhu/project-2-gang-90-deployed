package edu.tamu.project2.csce331;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
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



public class UsageChartController{

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Timestamp> colTime;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, Integer> colEmployee;
    @FXML private TableColumn<Transaction, Double> colCost;
    @FXML private TableColumn<Transaction, String> colCustomer;
    @FXML private Pagination pagination;
    @FXML private Text status_label;


    //added
    @FXML private TableView<IngredientUsage> usageTable; //TODO: add IngredientUsage object
    @FXML private TableColumn<IngredientUsage, String> colIngredientUsage;
    @FXML private TableColumn<IngredientUsage, Integer> colUsed;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    @FXML private BarChart<String, Number> usageBarChart;
    

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20; // fits better visually than 50; adjustable
    private int totalCount = 0;

    @FXML
    public void initialize() {
        // Configure columns
        colIngredientUsage.setCellValueFactory( new PropertyValueFactory<>("name"));
        colUsed.setCellValueFactory(new PropertyValueFactory<>("amount"));
    

        try {
            totalCount = queries.count_transactions();//TODO: change query when avalibale
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
        
        //loadPage(0);
    }

    



    private void loadPage(Timestamp begin_timestamp, Timestamp end_timeStamp) {
        try {
            ArrayList<IngredientUsage> list = queries.get_ingredient_usage(begin_timestamp, end_timeStamp);//insert quiery
            
            
            System.out.println(begin_timestamp);
            System.out.println(end_timeStamp);

            System.out.println("hello does this work");

            ObservableList<IngredientUsage> data = FXCollections.observableArrayList(list);
            usageTable.setItems(data);
            status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));
            usageBarChart.getData().clear();
            usageBarChart.setAnimated(false);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Total Usage");
            
            for (IngredientUsage entry : list) {
                String x = entry.getName();
                Number y = entry.getAmount();
                if(x != null && y != null){
                    series.getData().add(new XYChart.Data<>(x, y));
                }
            }

            usageBarChart.getData().add(series);

        } catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML 
    public void apply_dates(){
        try{
            LocalDate end_date = endDate.getValue();
            LocalDate start_date = startDate.getValue();
            Timestamp start_time =  Timestamp.valueOf(start_date.atStartOfDay());
            Timestamp end_time =  Timestamp.valueOf(end_date.atStartOfDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_today(){
        try{
            LocalDate start_date = LocalDate.now();
            LocalDate end_date = start_date.plusDays(1);
            Timestamp start_time =  Timestamp.valueOf(start_date.atStartOfDay());
            Timestamp end_time =  Timestamp.valueOf(end_date.plusDays(1).atStartOfDay());

            loadPage(start_time,end_time);
        }catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_week(){
        try{
            LocalDate end_date = LocalDate.now();
            LocalDate start_date = end_date.minusDays(7);
            Timestamp start_time =  Timestamp.valueOf(start_date.atStartOfDay());
            Timestamp end_time =  Timestamp.valueOf(end_date.plusDays(1).atStartOfDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_30_days(){
        try{
            LocalDate end_date = LocalDate.now();
            LocalDate start_date = end_date.minusDays(30);
            Timestamp start_time =  Timestamp.valueOf(start_date.atStartOfDay());
            Timestamp end_time =  Timestamp.valueOf(end_date.plusDays(1).atStartOfDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void apply_month(){
        try{
            LocalDate end_date = LocalDate.now();
            LocalDate start_date = end_date.minusMonths(1);
            Timestamp start_time =  Timestamp.valueOf(start_date.atStartOfDay());
            Timestamp end_time =  Timestamp.valueOf(end_date.plusDays(1).atStartOfDay());
            loadPage(start_time,end_time);
        }catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    @FXML
    public void go_employees() 
    { 
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/employee_list.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) status_label.getScene().getWindow();

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

            Stage stage = (Stage) status_label.getScene().getWindow();

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
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/tamu/project2/csce331/cashier_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) status_label.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Cashier - Menu");
            stage.show();
        } 
        catch (IOException e) 
        {
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
