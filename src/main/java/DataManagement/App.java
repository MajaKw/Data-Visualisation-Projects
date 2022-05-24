package DataManagement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        Scene scene  = new Scene(bc,800,600);
        String file = "Test1.csv";

        bc.getData().addAll(Main.getAllSeries(file));
        stage.setScene(scene);
        stage.show();
    }
}
