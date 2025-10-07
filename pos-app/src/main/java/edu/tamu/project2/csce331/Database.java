package edu.tamu.project2.csce331;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
  private static HikariDataSource dataSource;

  static {
    try (InputStream input =
        Database.class
            .getClassLoader()
            .getResourceAsStream("edu/tamu/project2/csce331/application.properties")) {

      if (input == null) {
        throw new RuntimeException("Cannot find application.properties in resources.");
      }

      Properties props = new Properties();
      props.load(input);

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(props.getProperty("db.url"));
      config.setUsername(props.getProperty("db.username"));
      config.setPassword(props.getProperty("db.password"));

      // Optional: fallback defaults
      config.setMaximumPoolSize(
          Integer.parseInt(props.getProperty("db.hikari.maximum-pool-size", "10")));
      config.setMinimumIdle(Integer.parseInt(props.getProperty("db.hikari.minimum-idle", "2")));
      config.setIdleTimeout(Long.parseLong(props.getProperty("db.hikari.idle-timeout", "30000")));
      config.setConnectionTimeout(
          Long.parseLong(props.getProperty("db.hikari.connection-timeout", "10000")));

      dataSource = new HikariDataSource(config);
      System.out.println("HikariCP connection pool initialized successfully.");

    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize HikariCP connection pool", e);
    }
  }

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }
}
