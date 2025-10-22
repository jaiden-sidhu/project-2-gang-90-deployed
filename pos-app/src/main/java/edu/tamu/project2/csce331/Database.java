package edu.tamu.project2.csce331;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Manages database connections using HikariCP connection pooling.
 * This class initializes the connection pool based on properties loaded from a configuration file
 * and environment variables loaded via Dotenv. It provides methods to obtain connections,
 * check initialization status, and reset the pool.
 * 
 * @author Daniel Zhang
 * @author Kevin Chen
 */
public class Database {
  private static final String CONFIG_PATH = "edu/tamu/project2/csce331/application.properties";
  private static volatile HikariDataSource data_source;
  private static volatile RuntimeException initFailure;

  /**
   * Initializes the HikariCP connection pool if it has not been initialized yet.
   *
   * @throws RuntimeException if the configuration file cannot be found or initialization fails
   */
  private static synchronized void initIfNeeded() {
    if (data_source != null || initFailure != null) return; 
    try (InputStream input = locateConfigStream()) {
      if (input == null) {
        throw new RuntimeException("Cannot find application.properties in resources (tried: " + CONFIG_PATH + ").");
      }
      Dotenv env = Dotenv.load();

      Properties props = new Properties();
      props.load(input);

      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(env.get("DATABASE_URL"));
      config.setUsername(env.get("DATABASE_USER"));
      config.setPassword(env.get("DATABASE_PASSWORD"));

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

  /**
   * Obtains a new database connection from the HikariCP connection pool,
   * initializing the pool if necessary.
   *
   * @return a {@link Connection} to the database
   * @throws SQLException if the connection pool failed to initialize or a database access error occurs
   */
  public static Connection getConnection() throws SQLException {
    if (data_source == null && initFailure == null) {
      initIfNeeded();
    }
    if (initFailure != null) {
      throw new SQLException(initFailure.getMessage(), initFailure);
    }
    return data_source.getConnection();
  }

  /**
   * Returns the exception thrown during the initialization of the connection pool, if any.
   *
   * @return the initialization failure {@link Throwable}, or null if initialization succeeded
   */
  public static Throwable getInitFailure() {
    return initFailure;
  }

  /**
   * Resets the connection pool by closing the existing data source if present,
   * and clearing any recorded initialization failure.
   * This allows for reinitialization of the pool on subsequent connection requests.
   */
  public static synchronized void reset() {
    if (data_source != null) {
      try { data_source.close(); } catch (Exception ignored) {}
    }
    data_source = null;
    initFailure = null;
  }

  /**
   * Checks whether the application properties configuration file is available in the classpath.
   *
   * @return true if the configuration file is found, false otherwise
   */
  public static boolean configAvailable() {
    return Database.class.getClassLoader().getResource(CONFIG_PATH) != null
        || Database.class.getResource('/' + CONFIG_PATH) != null;
  }

  /**
   * Attempts to locate and open an input stream to the application properties configuration file.
   *
   * @return an {@link InputStream} to the configuration file if found, or null if not found
   */
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
