package edu.tamu.project2.csce331;

import java.sql.Timestamp;

public class Transaction {
    public int transaction_id;
    public String customer_name;
    public Timestamp transaction_time;
    public int employee_id;
    public double total_price;
    Transaction(int transaction_id, String customer_name, Timestamp transaction_time, int employee_id, double total_price ){
        this.transaction_id = transaction_id;
        this.customer_name = customer_name;
        this.transaction_id = transaction_id;
        this.employee_id = employee_id;
        this.total_price = total_price;
    }
    
}
