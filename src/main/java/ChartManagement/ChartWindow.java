package ChartManagement;

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
    public VBox ySeriesSettings;
    public String xAxisUnit, yAxisUnit;

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
        toSave.append(xAxis).append(";").append(yAxis).append("\n");

        // populating charts with data and ySeriesSettings

        // assuming that xAxis is in the format <pathToFile>|<columnName>
        // checking if input makes sense
        String xColumnPath = xAxis.split("\\|")[0];
        String xColumnName = xAxis.split("\\|")[0];
        if(!UsefulFunctions.getAllFilePaths().contains(xColumnPath))
            UsefulFunctions.showErrorWindow("Invalid path for X-axis: " + xColumnPath);
        String yColumnName = yAxis.split("\\|")[1];
        String yColumnPath = yAxis.split("\\|")[0];
        if(!xColumnPath.equals(yColumnPath))
            UsefulFunctions.showErrorWindow("Invalid path for Y-axis: " + yColumnName);
        int yAxisIndex = UsefulFunctions.getColumnIndex(yColumnPath, yColumnName);
        if(yAxisIndex < 0) {
            UsefulFunctions.showErrorWindow("Column \"" + yColumnName + "\" not found");
            return;
        }

        xAxisUnit = UsefulFunctions.getColumnUnit(xColumnPath, 0);
        yAxisUnit = UsefulFunctions.getColumnUnit(yColumnPath, UsefulFunctions.getColumnIndex(yColumnPath, yColumnName)+1);
        System.out.println("xAxisUnit: " + xAxisUnit + ", yAxisUnit: " + yAxisUnit);

        for(var tmpVBox : UsefulFunctions.loopOverSceneGraph(root, VBox.class))
            if(tmpVBox.getChildren().size() == 0) ySeriesSettings = tmpVBox;
        ControllerOfChartWindow.addYseriesStaticly(this, xColumnPath, yAxis.split("\\|")[1], toSave);

        Scene scene = new Scene(root);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Chart window");
        stage.show();
    }
}
