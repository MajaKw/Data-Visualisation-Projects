package Menu;

import DataManagement.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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

//        addYseriesComboBox.focusedProperty().addListener((obs, oldVal, newVal) ->
//        {
//            addYseriesComboBox.setVisible(newVal);
//            addYseriesComboBox.setManaged(newVal);
//        });
//        addYseriesComboBox.setOnMouseClicked(event -> {
//            String File = addYseriesComboBox.getSelectionModel().getSelectedItem();
//            addYseriesField.setText(File);
//            addYseriesComboBox.getSelectionModel().clearSelection();
//        });

        // using data location
        // searching for available xColumns and yColumns
        var filePaths = new ArrayList<String>();
        filePaths.add("Test1.csv");
        ControllerOfChartSetUpWindow.fillXYColumns(new ArrayList<>(), availableYseries, filePaths);
        addYseriesComboBox.getItems().addAll(availableYseries);
    }

    public void ComboBoxTypingIn(KeyEvent k) {
        addYseriesComboBox.getItems().clear();
        addYseriesComboBox.getItems().addAll(ControllerOfChartSetUpWindow.searchList(addYseriesComboBox.getEditor().getText(),availableYseries));
    }

    public void bar_LineSwitch(ActionEvent e) {
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
        // using data location
        ArrayList<String> filePaths = new ArrayList<>();
        filePaths.add("Test1.csv");
        for(var path : filePaths) {
            addYseriesStaticly(ySeriesSettings, barChart, lineChart, path, addYseriesComboBox.getEditor().getText().split("\\|")[1], chartWindow.toSave);
        }
    }

    public static void addYseriesStaticly(VBox ySeriesSettings, BarChart barChart, LineChart lineChart, String path, String columnName, StringBuilder toSave) {
        //check if already contains this series
        for(var tmp : UsefulFunctions.loopOverSceneGraph(ySeriesSettings, Label.class)) {
            if(tmp.getText().equals(columnName)) return;
        }
        int columnIndex = UsefulFunctions.getColumnIndex(path, columnName);
        if(columnIndex < 0) return; // check if specified column exists

        var seriesForBar = Main.getSeries(path, columnIndex);
        var seriesForLine = Main.getSeries(path, columnIndex);
        seriesForLine[0].setName(columnName); seriesForBar[0].setName(columnName);
        barChart.getData().addAll(seriesForBar);
        lineChart.getData().addAll(seriesForLine);

        // adding HBox with this series settings controls
        HBox oneSeriesSettings = null;
        try{
            oneSeriesSettings = FXMLLoader.load(ChartSetUpWindow.class.getResource("oneSeriesSettings.fxml"));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        Label seriesLabel = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, Label.class).get(0);
        ColorPicker seriesColorPicker = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, ColorPicker.class).get(0);
        ToggleButton showHideButton = UsefulFunctions.loopOverSceneGraph(oneSeriesSettings, ToggleButton.class).get(0);
        int seriesNumber = ySeriesSettings.getChildren().size()+1;
        seriesLabel.setText(columnName);

        seriesColorPicker.setValue(Color.RED);
        String style = barChart.getStyle() + "CHART_COLOR_" + seriesNumber + ": " + colorFormat(seriesColorPicker.getValue()) + ";";
        barChart.setStyle(style); lineChart.setStyle(style);
        seriesColorPicker.valueProperty().addListener((observableValue, color, t1) -> {
            String[] style1 = barChart.getStyle().split(";");
            StringBuilder seriesColor = new StringBuilder(style1[seriesNumber-1]);
            seriesColor.replace(seriesColor.indexOf("#"), seriesColor.length(), colorFormat(t1));
            style1[seriesNumber-1] = seriesColor.toString();
            StringBuilder finalStyle = new StringBuilder();
            for (String value : style1) finalStyle.append(value + ";");
            barChart.setStyle(finalStyle.toString());
            lineChart.setStyle(finalStyle.toString());
        });

        showHideButton.setOnMouseClicked(mouseEvent -> {
            for(var tmp : lineChart.getData()) {
                XYChart.Series series = (XYChart.Series) tmp;
                if(series.getName().equals(seriesLabel.getText())) {
                    series.getNode().setVisible(!showHideButton.isSelected());
                    for(var x : series.getData())
                        ((XYChart.Data)x).getNode().setVisible(!showHideButton.isSelected());
                }
            }
            for(var tmp : barChart.getData()) {
                XYChart.Series series = (XYChart.Series) tmp;
                if(series.getName().equals(seriesLabel.getText()))
                    for(var x : series.getData())
                        ((XYChart.Data)x).getNode().setVisible(!showHideButton.isSelected());
            }
        });


        ySeriesSettings.getChildren().add(oneSeriesSettings);

        toSave.append(path).append(";").append(columnName).append("\n");
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
