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
    XYChart.Series[] createSeries() {
        ResultSet resultSet = getResultSet();
        int indexOfXAxis = getIndexOfXAxis();
        int indexOfYAxis = getIndexOfYAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        try {
            xAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfXAxis));
            yAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfYAxis));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        XYChart.Series[] series = new XYChart.Series[1];
        series[0] = new XYChart.Series();

        try {
            while(resultSet.next()){
                series[0].getData().add(new XYChart.Data(resultSet.getString(indexOfXAxis), resultSet.getDouble(indexOfYAxis)));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return series;
    }

    @Override
    public void graphChart(Stage stage) {
        ResultSet resultSet = getResultSet();
        int indexOfXAxis = getIndexOfXAxis();
        int indexOfYAxis = getIndexOfYAxis();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        try {
            xAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfXAxis));
            yAxis.setLabel(resultSet.getMetaData().getCatalogName(indexOfYAxis));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(createSeries());
        stage.setScene(scene);
        stage.show();
    }

    public void graphChart(Stage stage, XYChart.Series[] extraSeries) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc =
                new BarChart<>(xAxis,yAxis);
        XYChart.Series[] createdSeries = createSeries();

        XYChart.Series[] series = new XYChart.Series[createdSeries.length+extraSeries.length];
        int i=0;
        for(XYChart.Series s:createdSeries){
            series[i]=s;
            i++;
        }
        for(XYChart.Series s:extraSeries){
            series[i]=s;
            i++;
        }
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(createdSeries[0],extraSeries[0]);
        stage.setScene(scene);
        stage.show();
    }
}
