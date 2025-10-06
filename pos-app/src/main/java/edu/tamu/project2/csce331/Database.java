package edu.tamu.project2.csce331;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class Database {
    private static HikariDataSource dataSource;

    // Initialize the connection pool once
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/posdb");
        config.setUsername("postgres");
        config.setPassword("mypassword");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000); // 30s
        config.setConnectionTimeout(10000); // 10s

        dataSource = new HikariDataSource(config);
    }

    // Get a connection from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
