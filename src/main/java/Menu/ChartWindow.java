package Menu;

import MainMenu.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChartWindow {
    public StringBuilder toSave;
    public BarChart barChart;
    public LineChart lineChart;
    public Parent root;

    public ControllerOfChartWindow controller;

    public void showChartWindow(String xAxis, String yAxis){
        root = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChartSetUpWindow.class.getResource("ChartWindow.fxml"));
            root = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.chartWindow=this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        lineChart = UsefulFunctions.loopOverSceneGraph(root, LineChart.class).get(0);
        barChart = UsefulFunctions.loopOverSceneGraph(root, BarChart.class).get(0);

        toSave = new StringBuilder();

        // populating charts with data and ySeriesSettings
        // using data location
        // assuming that xAxis is in the format <pathToFile>|<columnName>
        String path = xAxis.split("\\|")[0];
        int yAxisIndex = UsefulFunctions.getColumnIndex(path, yAxis.split("\\|")[1]);
        if(yAxisIndex < 0) return;
        VBox ySeriesSettings = UsefulFunctions.loopOverSceneGraph(root, VBox.class).get(1);
        ControllerOfChartWindow.addYseriesStaticly(ySeriesSettings, barChart, lineChart, path, yAxis.split("\\|")[1], toSave);

        Scene scene = new Scene(root);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
