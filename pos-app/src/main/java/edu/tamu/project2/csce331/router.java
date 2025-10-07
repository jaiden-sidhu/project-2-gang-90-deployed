package edu.tamu.project2.csce331;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class router {
    final String database_name = "gang_90_db";
    final String database_user = "gang_90";
    final String database_password = "gang_90";
    final String database_url = String.format("jdbc:postgresql://csce-315-db.engr.tamu.edu/%s", database_name);
    public router(){

    }


    private ResultSet connect_exicute(String sql_string){
        

        //Building the connection

         Connection conn = null;
         try{

            // open connection

            conn = DriverManager.getConnection(database_url, database_user, database_password);
            
            // 
            


         } catch(Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return null;

            // how will this exit effect the program

         }

         Statement make_statment = null;
         try {

            //prepare sql to take a connection

            make_statment = conn.createStatement();
         } catch (Exception e) {
            
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return null;
         }
         ResultSet result =null;
         try {

            // exicute sql statment

            result = make_statment.executeQuery(sql_string);
         } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return null;
         }

         try {

            // close connection

            conn.close();
            return result;
         } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            return null;
         }

    }


    public ResultSet get_managers(){
        
        
        String sql_string = "SELECT * FROM personel WHERE role = 'manager';";        
        //may return null if error
        return connect_exicute(sql_string);

    }

    public ResultSet get_item_id(){
        
        
        String sql_string = "SELECT item_id, item_name FROM menu;" ;       
        //may return null if error
        return connect_exicute(sql_string);

    }




    // add employee
    public ResultSet add_employee(int employee_id, String name, String role, double pay){
        
        // i am considering doing a rollback but idk if it will be worth it
        // This prepare satment is used to prevent sql injections in postgress
        String sql_string = 
               String.format("""
               PREPARE insert_empoylee (integer, Varchar(255), varchar(255),NUMERIC(10, 2)) AS
               INSERT INTO personnel (employee_id, name, role, pay) VALUES($1,$2, $3, $4);

               EXECUTE insert_empoylee (%d, %s,%s, %f);
 
               """, employee_id, name, role, pay);
        
        
               
        //may return null if error
        return connect_exicute(sql_string);

    }




        // select all transactions grab 50 pass in a offset
    public ResultSet select_transaction(int offset){
        
        // i am considering doing a rollback but idk if it will be worth it
        // This prepare satment is used to prevent sql injections in postgress
        String sql_string = 
               String.format("""
               PREPARE select_transaction (integer) AS
               SELECT * FROM transactions
               LIMIT 50 OFFSET $1;

               EXECUTE select_transaction (%d);
               """, offset);
        
        
               
        //may return null if error
        return connect_exicute(sql_string);

    }


    // select transation details


    //select by time

    //select by order Id



    // transation details click on transaction display the details pass in transaction id

    // get menu items 

    // inset transations

    // insert transation details


    // update inventory quntitys

    // insert inventory

    // 


     

    






}
