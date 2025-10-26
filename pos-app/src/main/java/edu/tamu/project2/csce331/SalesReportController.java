package edu.tamu.project2.csce331;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * SalesReportController grabs data for the database and formates it for the sales_report.fxml
 * This class injects FXML with listeners to recive data form the user
 * The class will take this information and send it to the database to display the desire items.
 * The items are formated in this class.
 * 
 * @author Brendan Larson
 *
 */
public class SalesReportController {

    /**
     * deafualt constructor for sales report not used
     */
    public  SalesReportController (){

    }


    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Timestamp> colTime;
    @FXML private TableColumn<Transaction, Integer> colId;
    @FXML private TableColumn<Transaction, Integer> colEmployee;
    @FXML private TableColumn<Transaction, Double> colCost;
    @FXML private TableColumn<Transaction, String> colCustomer;
    @FXML private Pagination pagination;
    @FXML private Text status_label;


    //added
    @FXML private TableView<TimeItemName> itemTable; //TODO: add IngredientUsage object
    @FXML private TableColumn<TimeItemName, Timestamp> colTimeItem;
    @FXML private TableColumn<TimeItemName, String> colName;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    

    private final Queries queries = new Queries();
    private static final int PAGE_SIZE = 20; // fits better visually than 50; adjustable
    private int totalCount = 0;

    /**
     * initialize sets up the orignal value facotrys
     * 
     * 
     * 
     */

    @FXML
    public void initialize() {
        // Configure columns
        colTimeItem.setCellValueFactory( new PropertyValueFactory<>("name"));
        colName.setCellValueFactory(new PropertyValueFactory<>("time"));
    

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

    

    /**
     * loadPage is a private helper function which will populate the page with the recived data
     * 
     * @param begin_timestamp the start time for the query as a timestamp using standared time 
     * @param end_timestamp the end time for the query as a timestamp using standared time
     */

    private void loadPage(Timestamp begin_timestamp, Timestamp end_timeStamp) {
        try {
            ArrayList<TimeItemName> list = queries.get_sales_report(begin_timestamp, end_timeStamp);//insert quiery
            

            ObservableList<TimeItemName> data = FXCollections.observableArrayList(list);
            itemTable.setItems(data);
            status_label.setText(String.format("Showing %d of %d total", data.size(), totalCount));

        } catch (Exception e) {
            status_label.setText("Failed to load page: " + e.getMessage());
        }
    }

    /** 
     * 
     * apply_dates is a listner for the apply button in the javafxml file
     * this function will call loadPage which will take values form the user given from
     * endDate and startDate datePicker from the user
     * 
     */
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

    /** 
     * 
     * apply_today is a listner for the apply button in the javafxml file
     * this funciton will triger load page which will grab and display all sales for the today
     * 
     * 
     */

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


    /** 
     * 
     * apply_week is a listner for the week short cut button in the javafxml file
     * apply_week will triger load page which will grab all sales data for a week
     * 
     * 
     */

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

    /**
     * 
     * apply_30_days allows for the last 30 days to be displayed by the sales_report.fxml
     * 
     * 
     */
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

    /**
     * 
     * apply_month allows for the last month to be displayed by the sales_report.fxml
     * 
     * 
     */
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


    /**
     * 
     * go_employees transfers to the employee_list.fxml
     * 
     * 
     */
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

     /**
     * 
     * go_cashier transfers to the manager_products.fxml
     * 
     * 
     */
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


    /**
     * 
     * go_cashier transfers to the cshier_menu.fxml
     * 
     * 
     */
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



    /**
     * 
     * go_x_report transfers to the x_report.fxml
     * 
     * 
     */
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


    /**
     * 
     * go_usage_chart() transfers to the usage_chart.fxml
     * 
     * 
     */
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



    /**
     * 
     * go_sales_report transfers to the sales_report.fxml
     * 
     * 
     */
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

    /**
     * 
     * go_sales transfers to the transactions_history.fxml
     * 
     * 
     */
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


    /**
     * 
     * go_z_report transfers to the z_report.fxml
     * 
     * 
     */
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
