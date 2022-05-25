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
    public static StringBuilder toSave;
    public BarChart barChart;
    public LineChart lineChart;
    public Parent root;

    public Parent getRoot() { return root; }

    public void showChartWindow(String xAxis, String yAxis, String zAxis){
        Parent root = null;
        try {
            root = FXMLLoader.load(ChartSetUpWindow.class.getResource("ChartWindow.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Parent root2 = root;
        // getting numbers of column yAxis
        String path = "Test1.csv"; // path to file will be deduced by xAxis variable?
        int yAxisIndex = UsefulFunctions.getColumnIndex(path, yAxis);
        if(yAxisIndex < 0) return;

        // searching for charts in scene graph and filling it with data

        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, LineChart.class)){
            lineChart = tmp;
            tmp.getData().addAll(Main.getSeries(path, yAxisIndex));
        }
        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, BarChart.class)){
            barChart = tmp;
            tmp.getData().addAll(Main.getSeries(path, yAxisIndex));
        }
        toSave = new StringBuilder();
        toSave.append(xAxis).append(";").append(yAxis).append("\n");
        System.out.println(toSave);
        VBox ySeriesSettings = UsefulFunctions.loopOverSceneGraph(root, VBox.class).get(0);
        HBox oneSeriesSettings = null;
        try {
            oneSeriesSettings = FXMLLoader.load(ChartSetUpWindow.class.getResource("oneSeriesSettings.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (var tmp2 : oneSeriesSettings.getChildren()) {
            if (tmp2 instanceof Label) ((Label) tmp2).setText(yAxis);
            if(tmp2 instanceof ColorPicker) {
                ((ColorPicker)tmp2).valueProperty().addListener(new ChangeListener<Color>() {
                    @Override
                    public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                        barChart.setStyle("CHART_COLOR_1: "+ControllerOfChartWindow.colorFormat(((ColorPicker)tmp2).getValue())+";");
                        lineChart.setStyle("CHART_COLOR_1: "+ControllerOfChartWindow.colorFormat(((ColorPicker)tmp2).getValue())+";");
                    }
                });
            }
        }
        ySeriesSettings.getChildren().add(oneSeriesSettings);

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
