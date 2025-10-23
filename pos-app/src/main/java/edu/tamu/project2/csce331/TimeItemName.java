package edu.tamu.project2.csce331;

import java.sql.Timestamp;



/**
 * Repersents TimeItemName as a time and name pair
 * 
 * @author Brendan Larson
 *
 */
public class TimeItemName {
    Timestamp time;
    String name;

    /**
     * constructor to create TimeItemName this is for consitent formating of data
     * 
     * 
     * 
     * @param time time of trasaction from database as timestamp
     * @param name name of item from database as string
     */
    public TimeItemName(Timestamp time, String name){
        this.time = time;
        this.name = name;
    }

    /**
     * gets the time used in facortys
     * 
     * 
     * @return time of transation from database
     */
    public Timestamp getTime() {
        return time;
    }



    /**
     * setter of time
     *  
     * @param time time of trasation from database as timestamp
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }


    /**
     * name getter where name is the name of the item in the database
     * 
     * @return name items name form database
     */
    public String getName() {
        return name;
    }


    /**
     * name setter 
     * 
     * 
     * @param name item name in database
     */

    public void setName(String name) {
        this.name = name;
    }
}

