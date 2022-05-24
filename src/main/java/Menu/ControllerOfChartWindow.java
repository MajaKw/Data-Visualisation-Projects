package Menu;

import DataManagement.Main;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ControllerOfChartWindow {
    @FXML
    BarChart barChart;
    @FXML
    LineChart lineChart;
    @FXML
    TextField addYseriesField;
    @FXML
    Button addYseriesButton;
    @FXML
    RadioButton barChartButton, lineCharButton;
    @FXML
    VBox ySeriesSettings;

    @FXML
    public void initialize(){
        lineChart.setVisible(false);
        barChart.setVisible(true);
        barChartButton.setSelected(true);
    }

    public void bar_LineSwitch(ActionEvent e) {
        if(barChartButton.isSelected()) {
            barChart.setVisible(true);
            lineChart.setVisible(false);
        }
        else {
            barChart.setVisible(false);
            lineChart.setVisible(true);
        }
    }

    public void addYseries(ActionEvent e) {
        String columnName = addYseriesField.getText();
        //check if already contains this series
        for(var tmp : UsefulFunctions.loopOverSceneGraph(ySeriesSettings, Label.class)) {
            if(tmp.getText().equals(columnName)) return;
        }
        String path = "Test1.csv"; // should it be deduced from columnName?
        int columnIndex = UsefulFunctions.getColumnIndex(path, columnName);
        if(columnIndex < 0) return; // check if specified column exists
        barChart.getData().addAll(Main.getSeries(path, columnIndex));
        lineChart.getData().addAll(Main.getSeries(path, columnIndex));

        // adding HBox with this series settings controls
        HBox oneSeriesSettings = null;
        try{
            oneSeriesSettings = FXMLLoader.load(ChartSetUpWindow.class.getResource("oneSeriesSettings.fxml"));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        for(var tmp2 : oneSeriesSettings.getChildren()) {
            if(tmp2 instanceof Label) ((Label)tmp2).setText(columnName);
            if(tmp2 instanceof ComboBox<?>) ((ComboBox)tmp2).getItems().addAll(ChartWindow.seriesColors);
        }
        ySeriesSettings.getChildren().add(oneSeriesSettings);
    }
}
