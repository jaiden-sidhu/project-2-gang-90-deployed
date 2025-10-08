package edu.tamu.project2.csce331;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
  private static final String CONFIG_PATH = "edu/tamu/project2/csce331/application.properties";
  private static volatile HikariDataSource data_source;
  private static volatile RuntimeException initFailure;

  private static synchronized void initIfNeeded() {
    if (data_source != null || initFailure != null) return; 
    try (InputStream input = locateConfigStream()) {
      if (input == null) {
        throw new RuntimeException("Cannot find application.properties in resources (tried: " + CONFIG_PATH + ").");
      }

      Properties props = new Properties();
      props.load(input);

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(props.getProperty("db.url"));
      config.setUsername(props.getProperty("db.username"));
      config.setPassword(props.getProperty("db.password"));

      config.setMaximumPoolSize(
          Integer.parseInt(props.getProperty("db.hikari.maximum-pool-size", "10")));
      config.setMinimumIdle(Integer.parseInt(props.getProperty("db.hikari.minimum-idle", "2")));
      config.setIdleTimeout(Long.parseLong(props.getProperty("db.hikari.idle-timeout", "30000")));
      config.setConnectionTimeout(
          Long.parseLong(props.getProperty("db.hikari.connection-timeout", "10000")));

      data_source = new HikariDataSource(config);
      System.out.println("HikariCP connection pool initialized successfully.");

    } catch (Exception e) {
      System.err.println("[Database] HikariCP initialization failed: " + e.getClass().getName() + ": " + e.getMessage());
      e.printStackTrace();
      initFailure = new RuntimeException(
          "Failed to initialize HikariCP connection pool: " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }

  public static Connection getConnection() throws SQLException {
    if (data_source == null && initFailure == null) {
      initIfNeeded();
    }
    if (initFailure != null) {
      throw new SQLException(initFailure.getMessage(), initFailure);
    }
    return data_source.getConnection();
  }

  public static Throwable getInitFailure() {
    return initFailure;
  }

  public static synchronized void reset() {
    if (data_source != null) {
      try { data_source.close(); } catch (Exception ignored) {}
    }
    data_source = null;
    initFailure = null;
  }

  public static boolean configAvailable() {
    return Database.class.getClassLoader().getResource(CONFIG_PATH) != null
        || Database.class.getResource('/' + CONFIG_PATH) != null;
  }

  private static InputStream locateConfigStream() {
    ClassLoader cl = Database.class.getClassLoader();

    InputStream in = cl.getResourceAsStream(CONFIG_PATH);
    if (in != null) {
      return in;
    }
    in = Database.class.getResourceAsStream('/' + CONFIG_PATH);
    if (in != null) {
      return in;
    }
    System.err.println("[Database] Resource not found via primary or absolute path: " + CONFIG_PATH);
    try {
      System.err.println("[Database] CodeSource=" + Database.class.getProtectionDomain().getCodeSource().getLocation());
    } catch (Exception ignored) {}
    return null;
  }
}

