package edu.tamu.project2.csce331;

import java.sql.Timestamp;



/**
 * Repersents Transaction in the POS system 
 * The trasactions are repersented as:
 * <ul>
 * <li> transaction_id is uniqe indintification for each transaction<li>
 * <li> customer_name is the name of the customer as string <li>
 * <li> trasaction_time is the time of each trasaction as time stamp<li>
 * <li> employee_id the id of the employee who completed the transaction<li>
 * <li> total_price the entire price of the tracastion <li>
 * <ul>
 * @author Brendan Larson
 *
 */
public class Transaction {
    public final int transaction_id;
    public final String customer_name;
    public final Timestamp transaction_time;
    public final int employee_id;
    public final double total_price;


    
    /**
     * constructor of transactions with transaction_id, customer_name, transaction_time, employee_id, total_price
     * 
     * 
     * @param transaction_id uniqe indintification of transaction
     * @param customer_name name of costumer who made the transaction
     * @param transaction_time the time of the transaction was made as timestamp
     * @param employee_id uniqe id of the employee who made the transaction
     * @param total_price the total price of the transaction
     */
    public Transaction(int transaction_id, String customer_name, Timestamp transaction_time, int employee_id, double total_price) {
        this.transaction_id = transaction_id;
        this.customer_name = customer_name;
        this.transaction_time = transaction_time;
        this.employee_id = employee_id;
        this.total_price = total_price;
    }


    /**
     * gets unique id for trnasaction
     * 
     * @return transaction_id
     */
    public int getTransaction_id() { 
        return transaction_id;
    }


    /**
     * gets the name of the customer of the transaction
     * 
     * @return customer_name
     */
    public String getCustomer_name() {
        return customer_name;
    }


    /**
     * 
     * gets the time which the transaction happened
     * 
     * @return transaction_time
     */
    public Timestamp getTransaction_time() {
        return transaction_time;
    }


    /**
     * 
     * gets the unique id of employees
     * 
     * @return employee_id
     */
    public int getEmployee_id() {
        return employee_id;
    }

    /**
     * 
     * gets the total price of the transaction
     * 
     * @return total_price
     */
    public double getTotal_price() {
        return total_price;
    }
}
