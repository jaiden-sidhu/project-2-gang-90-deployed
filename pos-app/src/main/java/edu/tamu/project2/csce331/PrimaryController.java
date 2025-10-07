package edu.tamu.project2.csce331;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

  @FXML
  private void switchToSecondary() throws IOException {
    App.setRoot("secondary");
  }
}
