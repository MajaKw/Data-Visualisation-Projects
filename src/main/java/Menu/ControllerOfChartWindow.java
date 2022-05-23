package Menu;

import DataMengment.Main;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

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
    RadioButton barChartButton;
    @FXML
    RadioButton lineCharButton;

    @FXML
    public void initialize(){
        lineChart.setVisible(false);
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

    public void addYseries(javafx.event.ActionEvent e) {
        String columnName = addYseriesField.getText();
        String path = "Test1.csv"; // should it be deduced from columnName?
        int columnIndex = UsefulFunctions.getColumnIndex(path, columnName);
        barChart.getData().addAll(Main.getSeries(path, columnIndex));
        lineChart.getData().addAll(Main.getSeries(path, columnIndex));
    }
}
