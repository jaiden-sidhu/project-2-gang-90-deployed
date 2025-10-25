package edu.tamu.project2.csce331;



/**
 * Repersents details of the transaction for POS sytem
 * The trasactions details are repersented as:
 * <ul>
 * <li> transaction_id is unique indintification for each transaction<li>
 * <li> item_id is unique indintification for item on the menu<li>
 * <ul>
 * @author Brendan Larson
 *
 */
public class TransactionDetails {
    
    public int transaction_id;
    public int item_id;


    /**
     * constuctor of transaction Detials used for formating data
     * 
     * 
     * @param transaction_id unique indintification for each transaction
     * @param item_id unique indintification for item on the menu
     */
    public TransactionDetails(int transaction_id, int item_id) {
        this.transaction_id = transaction_id;
        this.item_id = item_id;
    }
    



}
