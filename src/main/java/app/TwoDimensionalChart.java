package app;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.ResultSet;

public abstract class TwoDimensionalChart implements Chart{
    private final ResultSet resultSet;
    private final int indexOfXAxis;
    private final int indexOfYAxis;
    
    public TwoDimensionalChart(ResultSet resultSet, int indexOfXAxis, int indexOfYAxis){
        this.resultSet = resultSet;
        this.indexOfXAxis = indexOfXAxis;
        this.indexOfYAxis = indexOfYAxis;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public int getIndexOfXAxis() {
        return indexOfXAxis;
    }

    public int getIndexOfYAxis() {
        return indexOfYAxis;
    }

    public XYChart.Series[] createSeries() {
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
    public abstract void graphChart(Stage stage, XYChart.Series[] extraSeries);
    //TODO
    TwoDimensionalChart combine(final TwoDimensionalChart secondChart) throws NoCompatibleDataException{
        if(!this.getResultSet().equals(secondChart.getResultSet()) || this.getIndexOfXAxis()!=secondChart.getIndexOfXAxis()){
            throw new NoCompatibleDataException();
        }
        final TwoDimensionalChart firstChart = this;
        return new TwoDimensionalChart(this.getResultSet(),this.indexOfXAxis,-1) {
            final TwoDimensionalChart first = firstChart;
            final TwoDimensionalChart second = secondChart;
            @Override
            public XYChart.Series[] createSeries() {
                XYChart.Series[] firstSeries = first.createSeries();
                XYChart.Series[] secondSeries = second.createSeries();
                XYChart.Series[] series = new XYChart.Series[firstSeries.length+secondSeries.length];
                int i=0;
                for(XYChart.Series s:firstSeries){
                    series[i]=s;
                    i++;
                }
                for(XYChart.Series s:secondSeries){
                    series[i]=s;
                    i++;
                }
                return series;
            }

            @Override
            public void graphChart(Stage stage, XYChart.Series[] extraSeries) {
                XYChart.Series[] secondSeries = second.createSeries();
                XYChart.Series[] series = new XYChart.Series[extraSeries.length+secondSeries.length];
                int i=0;
                for(XYChart.Series s:extraSeries){
                    series[i]=s;
                    i++;
                }
                for(XYChart.Series s:secondSeries){
                    series[i]=s;
                    i++;
                }
                first.graphChart(stage,series);
            }
            @Override
            public void graphChart(Stage stage) {
                graphChart(stage, second.createSeries());
            }
        };
    }
    
}
