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
    public static ObservableList<String> seriesColors = FXCollections.observableArrayList("Red", "Green", "Blue");

    public static void showChartWindow(String xAxis, String yAxis, String zAxis){
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
            var s = Main.getSeries(path, yAxisIndex);
            s[0].setName(yAxis);
            tmp.getData().addAll(s);
        }
        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, BarChart.class)){
            var s = Main.getSeries(path, yAxisIndex);
            s[0].setName(yAxis);
            tmp.getData().addAll(s);
        }

        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, VBox.class)) {
            HBox oneSeriesSettings = null;
            try {
                oneSeriesSettings = FXMLLoader.load(ChartSetUpWindow.class.getResource("oneSeriesSettings.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (var tmp2 : oneSeriesSettings.getChildren()) {
                if (tmp2 instanceof Label) ((Label) tmp2).setText(yAxis);
                if(tmp2 instanceof ColorPicker) {
                    ((ColorPicker)tmp2).setValue(Color.RED);
                    ((ColorPicker)tmp2).valueProperty().addListener(new ChangeListener<Color>() {
                        @Override
                        public void changed(ObservableValue<? extends Color> observableValue, Color color, Color t1) {
                            for(var tmp3 : UsefulFunctions.loopOverSceneGraph(root2, Chart.class))
                                tmp3.setStyle("CHART_COLOR_1: "+ControllerOfChartWindow.colorFormat(((ColorPicker)tmp2).getValue())+";");
                        }
                    });
                }
            }
            tmp.getChildren().add(oneSeriesSettings);
        }
        Scene scene = new Scene(root);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
//        this .css file is not ready yet
//        scene.getStylesheets().add("ChartStyling.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
