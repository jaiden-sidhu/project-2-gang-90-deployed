module edu.tamu.project2.csce331 {
  // JDBC + HikariCP
  requires java.sql;
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
