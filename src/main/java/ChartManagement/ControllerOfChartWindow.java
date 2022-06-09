package ChartManagement;

import DataManagement.Main;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ControllerOfChartWindow {
    ChartWindow chartWindow;
    @FXML
    public BarChart barChart;
    @FXML
    public LineChart lineChart;
    @FXML
    Button addYseriesButton;
    @FXML
    RadioButton barChartButton, lineCharButton;
    @FXML
    public VBox ySeriesSettings;
    @FXML
    ComboBox<String> addYseriesComboBox;

    private List<String> availableYseries = new ArrayList<>();


    @FXML
    public void initialize(){
        lineChart.setVisible(false);
        barChart.setVisible(true);
        barChartButton.setSelected(true);

        lineChart.setLegendVisible(false);
        barChart.setLegendVisible(false);

        // using data location
        // searching for available xColumns and yColumns
        var filePaths = new ArrayList<String>(UsefulFunctions.getAllFilePaths());
        ControllerOfChartSetUpWindow.fillXYColumns(new ArrayList<>(), availableYseries, filePaths);
        addYseriesComboBox.getItems().addAll(availableYseries);
    }

    public void ComboBoxTypingIn(KeyEvent k) {
        addYseriesComboBox.getItems().clear();
        addYseriesComboBox.getItems().addAll(ControllerOfChartSetUpWindow.getColumnNamesMatching(addYseriesComboBox.getEditor().getText(),availableYseries));
    }

    public void barLineSwitch(ActionEvent e) {
        if(barChartButton.isSelected()) {
            barChart.setVisible(true);
            lineChart.setVisible(false);
        }
        else {
            barChart.setVisible(false);
            lineChart.setVisible(true);
        }
    }

    public void addYseries(ActionEvent e) {
        String inputSeriesName = addYseriesComboBox.getEditor().getText();
        if(!availableYseries.contains(inputSeriesName)) return;
        addYseriesStaticly(chartWindow, inputSeriesName.split("\\|")[0], inputSeriesName.split("\\|")[1], chartWindow.toSave);
    }

    public static void addYseriesStaticly(ChartWindow chartWindow, String path, String columnName, StringBuilder toSave) {
        // check unit compatibility
        String xUnit = UsefulFunctions.getColumnUnit(path, 0);
        String yUnit = UsefulFunctions.getColumnUnit(path, UsefulFunctions.getColumnIndex(path, columnName)+1);
        if(!xUnit.equals(chartWindow.xAxisUnit)) {
            UsefulFunctions.showErrorWindow("Incompatibile X-axis units: required \"" + chartWindow.xAxisUnit + "\" but provided \"" + xUnit + "\"");
            return;
        }
        if(!yUnit.equals(chartWindow.yAxisUnit)) {
            UsefulFunctions.showErrorWindow("Incompatibile Y-axis units: required \"" + chartWindow.yAxisUnit + "\" but provided \"" + yUnit + "\"");
            return;
        }
        //check if already contains this series
        for(var tmp : UsefulFunctions.loopOverSceneGraph(chartWindow.ySeriesSettings, Label.class)) {
            if(tmp.getText().equals(path+"|"+columnName)) {
                UsefulFunctions.showErrorWindow("Chart already contains series \"" + path + "|"+ columnName + "\"");
                return;
            }
        }
        int columnIndex = UsefulFunctions.getColumnIndex(path, columnName);
        // check if specified column exists
        if(columnIndex < 0) {
            UsefulFunctions.showErrorWindow("Column \"" + path + "|" + columnName + "\" does not exist");
            return;
        }
        var seriesForBar = Main.getSeries(path, columnIndex);
        var seriesForLine = Main.getSeries(path, columnIndex);
        seriesForLine[0].setName(columnName); seriesForBar[0].setName(columnName);
        chartWindow.barChart.getData().addAll(seriesForBar);
        chartWindow.lineChart.getData().addAll(seriesForLine);

        // adding HBox with this series settings controls
        HBox oneSeriesSettings = null;
        try{
            oneSeriesSettings = FXMLLoader.load(ChartSetUpWindow.class.getResource("OneSeriesSettings.fxml"));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        Label seriesLabel = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, Label.class).get(0);
        ColorPicker seriesColorPicker = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, ColorPicker.class).get(0);
        ToggleButton showHideButton = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, ToggleButton.class).get(0);
        int seriesNumber = chartWindow.ySeriesSettings.getChildren().size()+1;
        seriesLabel.setText(path+"|"+columnName);

        seriesColorPicker.setValue(Color.RED);
        String style = chartWindow.barChart.getStyle() + "CHART_COLOR_" + seriesNumber + ": " + colorFormat(seriesColorPicker.getValue()) + ";";
        chartWindow.barChart.setStyle(style); chartWindow.lineChart.setStyle(style);

        seriesColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
            String[] style1 = chartWindow.barChart.getStyle().split(";");
            StringBuilder seriesColor = new StringBuilder(style1[seriesNumber-1]);
            seriesColor.replace(seriesColor.indexOf("#"), seriesColor.length(), colorFormat(t1));
            style1[seriesNumber-1] = seriesColor.toString();
            StringBuilder finalStyle = new StringBuilder();
            for (String value : style1) finalStyle.append(value + ";");
            chartWindow.barChart.setStyle(finalStyle.toString());
            chartWindow.lineChart.setStyle(finalStyle.toString());
        });

        showHideButton.setText("O");
        showHideButton.setOnMouseClicked(mouseEvent -> {
            String seriesName = seriesLabel.getText().split("\\|")[1];
            for(var tmp : chartWindow.lineChart.getData()) {
                XYChart.Series series = (XYChart.Series) tmp;
                if(series.getName().equals(seriesName)) {
                    series.getNode().setVisible(!showHideButton.isSelected());
                    for(var x : series.getData())
                        ((XYChart.Data)x).getNode().setVisible(!showHideButton.isSelected());
                }
            }
            for(var tmp : chartWindow.barChart.getData()) {
                XYChart.Series series = (XYChart.Series) tmp;
                if(series.getName().equals(seriesName))
                    for(var x : series.getData())
                        ((XYChart.Data)x).getNode().setVisible(!showHideButton.isSelected());
            }
            if(showHideButton.isSelected()) showHideButton.setText("X");
            else showHideButton.setText("O");
        });


        chartWindow.ySeriesSettings.getChildren().add(oneSeriesSettings);
        if(chartWindow.ySeriesSettings.getChildren().size()>1) {
            toSave.append(path).append(";").append(columnName).append("\n");
        }
    }


    public void handleSaveButtonPressed(ActionEvent event){
        Stage newStage = new Stage();
        newStage.initModality(Modality.APPLICATION_MODAL);
        SaveWindow saveWindow = new SaveWindow(chartWindow);
        saveWindow.display(newStage);
        newStage.showAndWait();
    }

    public static String colorFormat(Color c) {
        int r = (int) (255 * c.getRed()) ;
        int g = (int) (255 * c.getGreen()) ;
        int b = (int) (255 * c.getBlue()) ;

        return String.format("#%02x%02x%02x", r, g, b);
    }
}
