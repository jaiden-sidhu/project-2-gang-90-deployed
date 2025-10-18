package edu.tamu.project2.csce331;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class XReportQueries {
    



    public Item get_curr_special_item(Timestamp time) throws SQLException {
        String sql = "SELECT * FROM seasonal_item WHERE ? < end_time AND ? >= start_time LIMIT 1;";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setTimestamp(1, time);
        stmt.setTimestamp(2, time);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
            int id = rs.getInt("item_id");
            String name = rs.getString("item_name");
            double price = rs.getDouble("item_price");
            return (new Item(id, name, price));
            } else {
            return null;
            }
        }

        }
    }


    public Item get_sales(Timestamp time) throws SQLException {
        String sql = "SELECT * FROM seasonal_item WHERE ? < end_time AND ? >= start_time LIMIT 1;";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setTimestamp(1, time);
        stmt.setTimestamp(2, time);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
            int id = rs.getInt("item_id");
            String name = rs.getString("item_name");
            double price = rs.getDouble("item_price");
            return (new Item(id, name, price));
            } else {
            return null;
            }
        }

        }
    }



    public Item get_sales_by_day(Timestamp time) throws SQLException {
        String sql = "SELECT * FROM seasonal_item WHERE ? < end_time AND ? >= start_time LIMIT 1;";

        try (Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setTimestamp(1, time);
        stmt.setTimestamp(2, time);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
            int id = rs.getInt("item_id");
            String name = rs.getString("item_name");
            double price = rs.getDouble("item_price");
            return (new Item(id, name, price));
            } else {
            return null;
            }
        }

        }
    }


}
