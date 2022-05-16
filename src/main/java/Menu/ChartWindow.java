package Menu;

import app.App;
import app.TwoDimensionalBarChart;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChartWindow {
    public static void showChartWindow(String xAxis, String yAxis, String zAxis){
        try {
            Parent root = FXMLLoader.load(ChartSetUpWindow.class.getResource("ChartWindow.fxml"));
            // searching for a chart in scene graph and filling it with data
            for(var tmp : root.getChildrenUnmodifiable()){
                // creating ResultSet
                String SQLquery = "SELECT year, GDP_per_capita FROM Azerbaijan";
                Connection conn = App.connect();
                ResultSet rs = null;
                try {
                    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = stmt.executeQuery(SQLquery);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                ((BarChart)tmp).getData().addAll(new TwoDimensionalBarChart(rs, 1, 2).createSeries());
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
