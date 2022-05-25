package Menu;

import DataManagement.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import MainMenu.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ChartWindow {
    public StringBuilder toSave;
    public BarChart barChart;
    public LineChart lineChart;
    public Parent root;

    public ControllerOfChartWindow controller;

    public Parent getRoot() { return root; }

    public void showChartWindow(String xAxis, String yAxis, String zAxis){
        Parent root = null;
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
        String path = "Test1.csv"; // path to file will be deduced by xAxis variable?
        int yAxisIndex = UsefulFunctions.getColumnIndex(path, yAxis);
        if(yAxisIndex < 0) return;
        VBox ySeriesSettings = UsefulFunctions.loopOverSceneGraph(root, VBox.class).get(0);
        ControllerOfChartWindow.addYseriesStatic(ySeriesSettings, barChart, lineChart, path, yAxis,toSave);

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
