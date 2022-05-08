package app;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.ResultSet;

public abstract class TwoDimensionalChart implements Chart {
    private final ResultSet resultSet;
    private final int indexOfXAxis;
    private final int indexOfYAxis;
    TwoDimensionalChart first;
    TwoDimensionalChart second;

    public TwoDimensionalChart(ResultSet resultSet, int indexOfXAxis, int indexOfYAxis) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        XYChart.Series[] series = new XYChart.Series[1];
        series[0] = new XYChart.Series();
        try {
            resultSet.beforeFirst();
            resultSet.next();
            Double max = resultSet.getDouble(indexOfYAxis);
            Double min = resultSet.getDouble(indexOfYAxis);
            while(resultSet.next()){
                Double current= resultSet.getDouble(indexOfYAxis);
                if(current<min){
                    min=current;
                }
                if(current>max){
                    max=current;
                }
            }

            resultSet.beforeFirst();
            while (resultSet.next()) {
                series[0].getData().add(new XYChart.Data(resultSet.getString(indexOfXAxis), (resultSet.getDouble(indexOfYAxis))/(max-min)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return series;
    }

    public abstract void graphChart(Stage stage, XYChart.Series[] extraSeries);

    //TODO
    TwoDimensionalChart combine(final TwoDimensionalChart secondChart) throws NoCompatibleDataException {
        if (!this.getResultSet().equals(secondChart.getResultSet()) || this.getIndexOfXAxis() != secondChart.getIndexOfXAxis()) {
            throw new NoCompatibleDataException();
        }
        //secondChart.graphChart(new Stage());
        TwoDimensionalChart twoDimensionalChart = new TwoDimensionalChart(this.getResultSet(), this.indexOfXAxis, -1) {

            @Override
            public XYChart.Series[] createSeries() {
                XYChart.Series[] firstSeries = first.createSeries();
                XYChart.Series[] secondSeries = second.createSeries();
                XYChart.Series[] series = new XYChart.Series[firstSeries.length + secondSeries.length];
                int i = 0;
                for (XYChart.Series s : firstSeries) {
                    series[i] = s;
                    i++;
                }
                for (XYChart.Series s : secondSeries) {
                    series[i] = s;
                    i++;
                }
                return series;
            }

            @Override
            public void graphChart(Stage stage, XYChart.Series[] extraSeries) {
                XYChart.Series[] secondSeries = second.createSeries();
                XYChart.Series[] series = new XYChart.Series[extraSeries.length + secondSeries.length];
                int i = 0;
                for (XYChart.Series s : extraSeries) {
                    series[i] = s;
                    i++;
                }
                for (XYChart.Series s : secondSeries) {
                    series[i] = s;
                    i++;
                }
                first.graphChart(stage, series);
            }

            @Override
            public void graphChart(Stage stage) {
                System.out.println(secondChart.createSeries().length);
                if (second == null) {
                    System.out.println("Bad");
                }
                //second.graphChart(new Stage() );
                first.graphChart(stage, second.createSeries());
            }
        };
        twoDimensionalChart.first = this;
        twoDimensionalChart.second = secondChart;
        return twoDimensionalChart;
    }

}
