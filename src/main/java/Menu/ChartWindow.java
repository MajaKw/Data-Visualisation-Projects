package Menu;

import DataMengment.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;

public class ChartWindow {

    public static void showChartWindow(String xAxis, String yAxis, String zAxis){
        Parent root = null;
        try {
            root = FXMLLoader.load(ChartSetUpWindow.class.getResource("ChartWindow.fxml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // getting numbers of column yAxis
        String path = "Test1.csv"; // path to file will be deduced by xAxis variable?
        int yAxisIndex = UsefulFunctions.getColumnIndex(path, yAxis);
        if(yAxisIndex == -1) System.out.println("Exception should be thrown here!");

        // searching for a chart in scene graph and filling it with data
        System.out.println(yAxisIndex-1);
        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, BarChart.class)){
            tmp.getData().addAll(Main.getSeries(path, yAxisIndex-1));
            System.out.println(tmp);
        }
        for(var tmp : UsefulFunctions.loopOverSceneGraph(root, LineChart.class)){
            tmp.getData().addAll(Main.getSeries(path, yAxisIndex-1));
            System.out.println(tmp);
        }

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
