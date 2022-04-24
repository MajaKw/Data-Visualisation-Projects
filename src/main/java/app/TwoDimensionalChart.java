package app;

import javafx.stage.Stage;

import javax.swing.*;
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
    //TODO
    TwoDimensionalChart combine(final TwoDimensionalChart secondChart, Box box) throws NoCompatibleDataException{
        if(!this.getResultSet().equals(secondChart.getResultSet()) || this.getIndexOfXAxis()!=secondChart.getIndexOfXAxis()){
            throw new NoCompatibleDataException();
        }
        return new TwoDimensionalChart(this.getResultSet(),this.indexOfXAxis,-1) {
            final TwoDimensionalChart fC = this;
            final TwoDimensionalChart sC = secondChart;
            @Override
            public void graphChart(Stage stage) {
                //TODO
            }
        };
    }
    
}
