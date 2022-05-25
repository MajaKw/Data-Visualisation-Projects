package DataManagement;

import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;

public class SeriesCollector {
    XYChart.Series<String,Number>[] Series;

    public SeriesCollector(String path){
        try {
            String file ="src/main/resources/Uploaded/"+path;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] names = line.split(";");
            Series = new XYChart.Series[names.length-1];
            for (int i = 0; i < names.length-1; i++) {
                Series[i]=new XYChart.Series<>();
                Series[i].setName(names[i+1]);
            }
            while((line = reader.readLine()) != null && !line.equals("---EOD---")){
                String[] values = line.split(";");
                for (int i = 0; i < names.length-1 ; i++) {
                    Series[i].getData().add(new XYChart.Data<String,Number>(values[0],Double.valueOf(values[i+1])));
                }
            }
            reader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public XYChart.Series<String,Number>[] getSeries(int...columns){
        XYChart.Series<String,Number>[] toReturn = new XYChart.Series[columns.length];
        for (int i = 0; i < columns.length; i++) {
            toReturn[i]=copySeries(Series[columns[i]]);

        }
        return toReturn;
    }

    public XYChart.Series<String,Number>[] getAllSeries(){
        XYChart.Series<String,Number>[] toReturn = new XYChart.Series[Series.length];
        for (int i = 0; i < Series.length; i++) {
            toReturn[i]=copySeries(Series[i]);

        }
        return toReturn;
    }

    public static <S, T> XYChart.Series<S, T> copySeries(XYChart.Series<S, T> series) {
        XYChart.Series<S, T> copy = new XYChart.Series<>(series.getName(),
                series.getData().stream()
                        .map(data -> new XYChart.Data<S, T>(data.getXValue(), data.getYValue()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        return copy;
    }
}
