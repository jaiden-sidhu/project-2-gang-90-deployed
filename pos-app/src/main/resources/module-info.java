module edu.tamu.project2.csce331 {
  // For env
  requires io.github.cdimascio.dotenv.java;

  // JDBC + HikariCP
  requires transitive java.sql;
  requires com.zaxxer.hikari;

  // // PostgreSQL driver:
  // requires org.postgresql.jdbc;

  // JavaFX modules
  requires javafx.controls;
  requires javafx.fxml;


  opens edu.tamu.project2.csce331 to
      javafx.fxml;

  exports edu.tamu.project2.csce331;
}
