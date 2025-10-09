package edu.tamu.project2.csce331;

import java.sql.Timestamp;

public class Transaction {
    public final int transaction_id;
    public final String customer_name;
    public final Timestamp transaction_time;
    public final int employee_id;
    public final double total_price;

    public Transaction(int transaction_id, String customer_name, Timestamp transaction_time, int employee_id, double total_price) {
        this.transaction_id = transaction_id;
        this.customer_name = customer_name;
        this.transaction_time = transaction_time;
        this.employee_id = employee_id;
        this.total_price = total_price;
    }

    public int getTransaction_id() { 
        return transaction_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public Timestamp getTransaction_time() {
        return transaction_time;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public double getTotal_price() {
        return total_price;
    }
}
