package edu.tamu.project2.csce331;

import java.sql.Timestamp;

public class TimeItemName {
    Timestamp time;
    String name;
    public TimeItemName(Timestamp time, String name){
        this.time = time;
        this.name = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

