package searchEngine;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainController {
    private String filePath;

    public void setPath(String filePath){
        this.filePath = filePath;
    }
    @FXML
    private void initialize() {

        Platform.runLater(() -> {

            //do stuff

        });

    }
}
