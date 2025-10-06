package backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class router {
    final String user = "gang-90";
    final String pswd = "gang-90";
    public router(){

    }


    public void connect(){
        
        //Building the connection
         Connection conn = null;
        conn = DriverManager.getConnection(
        "jdbc:postgresql://csce-315-db.engr.tamu.edu/sthomas_demo",
        user, pswd);
    }





}
