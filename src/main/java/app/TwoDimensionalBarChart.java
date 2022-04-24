package app;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TwoDimensionalBarChart extends TwoDimensionalChart{
    public TwoDimensionalBarChart(ResultSet resultSet, int indexOfXAxis, int indexOfYAxis) {
        super(resultSet, indexOfXAxis, indexOfYAxis);
    }

    @Override
    public void graphChart(Stage stage) {
        ResultSet resultSet = getResultSet();
        int indexOfXAxis = getIndexOfXAxis();
        int indexOfYAxis = getIndexOfYAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        XYChart.Series series = new XYChart.Series();

        try {
            xAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfXAxis));
            yAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfYAxis));
            while(resultSet.next()){
                series.getData().add(new XYChart.Data(resultSet.getString(indexOfXAxis), resultSet.getDouble(indexOfYAxis)));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series);
        stage.setScene(scene);
        stage.show();
    }
}
