package app;

import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.sql.ResultSet;

public class TwoDimensionalLineChart extends TwoDimensionalChart{
    public TwoDimensionalLineChart(ResultSet resultSet, int indexOfXAxis, int indexOfYAxis) {
        super(resultSet, indexOfXAxis, indexOfYAxis);
    }


    @Override
    public void graphChart(Stage stage) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String,Number> bc =
                new LineChart<>(xAxis,yAxis);
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(createSeries());
        stage.setScene(scene);
        stage.show();
    }

    public void graphChart(Stage stage, XYChart.Series[] extraSeries) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String,Number> bc =
                new LineChart<>(xAxis,yAxis);
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
        bc.getData().addAll(series);
        stage.setScene(scene);
        stage.show();
    }
}
