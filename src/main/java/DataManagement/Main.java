package DataManagement;

import javafx.scene.chart.XYChart;

import java.util.HashMap;

public class Main {
    //Map of all already created Series
    private static HashMap<String,SeriesCollector> hashMap= new HashMap<>();

    public static XYChart.Series[] getSeries(String path, int...columns){
        if(!hashMap.containsKey(path)){
            hashMap.put(path,new SeriesCollector(path));
        }
        else{
        }
        return hashMap.get(path).getSeries(columns);
    }

    public static XYChart.Series[] getAllSeries(String path){
        if(!hashMap.containsKey(path)){
            hashMap.put(path,new SeriesCollector(path));
        }
        return hashMap.get(path).getAllSeries();
    }
}
