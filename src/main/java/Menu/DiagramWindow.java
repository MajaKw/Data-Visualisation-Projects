package Menu;

import app.App;
import app.TwoDimensionalBarChart;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.*;
//import java.awt.Component;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import com.google.common.io.Files;
import org.controlsfx.control.PropertySheet;

public class DiagramWindow {
    String xColumn = null; // name of the column for X-axis
    String yColumn = null; // name of the column for Y-axis

    public void display(String title) throws Exception {
        Stage window = new Stage();
        // zabezpieczenie ze wymusza kliknac w to okno i sie nim zajac (a nie tym "od spodem")
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(600);
        window.setHeight(600);
        // ** wyszukiwarka krajow
        BorderPane root = new BorderPane();
        HBox search_menu = new HBox();


//        JMenuBar menu = new JMenuBar();
        MenuBar menuBar = new MenuBar();

        // diagram menu
        Menu menu_diagram, diagram2D, diagram3D;
        MenuItem barGraph, lineGraph, pieChart, histogram, dotGraph, bubbleChart;
        MenuItem example3D;
        menu_diagram = new Menu("diagram");
        diagram2D = new Menu("2D"); diagram3D = new Menu("3D");
        barGraph = new MenuItem("barGraph"); lineGraph = new MenuItem("lineGraph"); pieChart = new MenuItem("pieChart");
        histogram = new MenuItem("histogram"); dotGraph = new MenuItem("dotGraph"); bubbleChart = new MenuItem("bubbleChart");
        example3D = new MenuItem("example");
        diagram2D.getItems().addAll(barGraph, lineGraph, pieChart, histogram, dotGraph, bubbleChart);
        diagram3D.getItems().addAll(example3D);
        menu_diagram.getItems().addAll(diagram2D, diagram3D);

        // other simpler menus
        Menu menu_options = new Menu("data");
        MenuItem countries_menu, categories_menu, upload_menu;
        countries_menu = new MenuItem("countires"); categories_menu = new MenuItem("categories"); upload_menu = new MenuItem("upload");
        menu_options.getItems().addAll(countries_menu, categories_menu, upload_menu);
        Menu menu_X = new Menu("X-axis"); Menu menu_Y = new Menu("Y-axis");
        MenuItem yearX, countriesX, categoryX; MenuItem yearY, countriesY, categoryY;
        // musialam zrobic 2, bo nie dalo sie tych samych bo beda robic co innego w teorii
        yearX = new MenuItem("year"); countriesX = new MenuItem("countries"); categoryX = new MenuItem("category");
        yearY = new MenuItem("year"); countriesY = new MenuItem("countries"); categoryY = new MenuItem("category");
        menu_X.getItems().addAll(yearX, countriesX, categoryX); menu_Y.getItems().addAll(yearY, countriesY, categoryY);

        menuBar.getMenus().addAll(menu_diagram, menu_options, menu_X, menu_Y);
        VBox VBoxdiagram = new VBox(menuBar);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search.fxml"));
//        System.out.println(fxmlLoader.getController().toString());
//        MainController controller = fxmlLoader.<MainController>getController();
//        controller.setPath("/home/maisho/demo1/src/main/resources/searchEngine/countries.json");
        AnchorPane newLoadedPane = fxmlLoader.load();
        // tu problem jest taki ze chcialabym sie dostac do obeiktu Controller i tam ustawic za pomoca funckji odpowiednia sciezke
        // zeby miec jedna wyszukiwarke dla krajow i kategorii, ale jest z tym jakis problem do zmiany pozniej


        VBox layout = new VBox();
        layout.setSpacing(10);
        VBox layout_content = new VBox();
        layout_content.setSpacing(10);
        HBox error_layout = new HBox();
        Text error = new Text(10,50,"");
        error.setFill(Color.RED);
        error_layout.setAlignment(Pos.CENTER);
        error_layout.getChildren().add(error);

        search_menu.getChildren().add(newLoadedPane);
        ListView list = (ListView) search_menu.lookup("#listView");

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            // funckja ktora sie wywoluje po kliknieciu myszy (czyli np mozna nia zrobic chyba zeby sie dobry wykres towrzyl)
            @Override
            public void handle(MouseEvent event) {
                if(xColumn == null) {
                    xColumn = (String) list.getSelectionModel().getSelectedItem();
                    System.out.println("clicked on " + xColumn);
                }
                // if it the user has chosen second column for creating the chart
                else {
                    yColumn = (String) list.getSelectionModel().getSelectedItem();
                    System.out.println("Creating a chart for " + xColumn + " and " + yColumn);
                    String xTableName = xColumn.split("\\.")[0];
                    String xColumnName = xColumn.split("\\.")[1];
                    String yTableName = yColumn.split("\\.")[0];
                    String yColumnName = yColumn.split("\\.")[1];
                    String SQLquery = "SELECT " + xColumnName + ", " + yColumnName + " " +
                            "FROM " + xTableName; // TODO: how to tell which values of xColumn correspond to which values of yColumn?
                    System.out.println(SQLquery);
                    Connection conn = App.connect();
                    try {
                        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(SQLquery);
                        Stage st = new Stage();
                        TwoDimensionalBarChart barChar = new TwoDimensionalBarChart(rs, 1, 2);
                        barChar.graphChart(st);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    xColumn = null; yColumn = null;
                }
//                System.out.println("clicked on " + xColumn);
                layout.getChildren().remove(search_menu);
            }
        });

//        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            // funckja ktora sie wywoluje po kliknieciu myszy (czyli np mozna nia zrobic chyba zeby sie dobry wykres towrzyl)
//            @Override
//            public void handle(MouseEvent event) {
//                String yColumn = (String) list.getSelectionModel().getSelectedItem();
//                System.out.println("secondly clicked on " + yColumn);
//                layout.getChildren().remove(search_menu);
//            }
//        });
        layout.getChildren().addAll(VBoxdiagram);
        countries_menu.setOnAction(new EventHandler<ActionEvent>() {
                                       @Override
                                       public void handle(ActionEvent actionEvent) {
                                           System.out.println("WRR");
                                           layout.getChildren().add(search_menu);
                                       }
                                   }
        );
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        List<String> extensions = Arrays.asList("csv", "txt");

        upload_menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                layout_content.getChildren().clear();
                File uploaded_file = fileChooser.showOpenDialog(window);
                //ERROR CHECKS
                if(uploaded_file == null){
                    layout_content.getChildren().add(error_layout);
                    error.setText("No file chosen");
                    return;
                }
                System.out.println(uploaded_file);
                String extension = Files.getFileExtension(uploaded_file.getAbsolutePath());
                long file_size = uploaded_file.length();
                if (!extensions.contains(extension)) {
                    layout_content.getChildren().add(error_layout);
                    error.setText("File extension not in allowed: " + extensions.toString());
                    return;
                }
                if(file_size>5000000){
                    layout_content.getChildren().add(error_layout);
                    error.setText("File is too big");
                    return;
                }
                //END
                try {
                    String separator = table_creation("",layout_content,uploaded_file);

                    Label label1 = new Label("Separator:");
                    TextField textField = new TextField();
                    textField.setText(separator);
                    Button button = new Button("Change");
                    HBox hb = new HBox();
                    button.setOnAction(actionEvents ->  {
                        try {
                            table_creation(textField.getText(),layout_content,uploaded_file);
                            layout_content.getChildren().add(hb);
                        }catch (FileNotFoundException e) {
                            layout_content.getChildren().add(error_layout);
                            error.setText("File was not found");
                            return;
                        }
                    });

                    hb.getChildren().addAll(label1, textField,button);
                    hb.setSpacing(10);
                    layout_content.getChildren().add(hb);

                } catch (FileNotFoundException e) {
                    layout_content.getChildren().add(error_layout);
                    error.setText("File was not found");
                    return;
                }
            }
        });

//        layout.setAlignment(Pos.CENTER);
        // **
        // *** wyszukiwarka do categorii
        HBox categories_Menu = new HBox();
        layout.getChildren().add(layout_content);


        // adding scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
    private static class RowDataGenerator {
        List<String> getNext(String data,String separator, int nWords) {
            List<String> words = new ArrayList<>();

            for (int i = 0; i < nWords; i++) {
                words.add(data.split(separator)[i]);
            }

            return words;
        }
    }

    void table_row_save(ObservableList row_data, String file_name){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/Uploaded/"+file_name.replace("/","").replaceAll("[-+.^:/\\,]","")+".csv", true));
            for (int j=0; j<row_data.size(); j++){
                if (j==row_data.size()-1){
                    writer.append((String) row_data.get(j));
                }else{
                    writer.append((String) row_data.get(j)+";");
                }
            }
            writer.append("\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    String table_creation(String separator, VBox layout_content, File uploaded_file) throws FileNotFoundException{
        layout_content.getChildren().clear();
        Scanner myReader = new Scanner(uploaded_file);
        String first_line = myReader.nextLine();
        String[] separators = {";", ",", "  ", " "};
        if(separator==""){
            for (int i = 0; i<separators.length;i++){
                if (first_line.contains(separators[i])){
                    separator = separators[i];
                    break;
                };
            }
        }

        String[] data_split = first_line.split(separator);
        TableView data_table = new TableView();
        MenuItem mi1 = new MenuItem("Set column as label and save");
        mi1.setOnAction((ActionEvent event) -> {
            TablePosition item = (TablePosition) data_table.getSelectionModel().getSelectedCells().get(0);
            int row_count = data_table.getItems().size();
            TableColumn col = item.getTableColumn();
            ObservableList table_data = data_table.getItems();
            for (int i = 0; i < row_count; i++){
                ObservableList row_data = (ObservableList)table_data.get(i);
                int row = i;
                Object it = data_table.getItems().get(row);
                String data = (String) col.getCellObservableValue(it).getValue();
                table_row_save(row_data, data);
            }
            layout_content.getChildren().clear();
        });


        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        data_table.setContextMenu(menu);
        data_table.getSelectionModel().setCellSelectionEnabled(true);
        data_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn<ObservableList<String>, String>[] columns = new TableColumn[data_split.length];
        for (int i = 0; i<data_split.length;i++){
            final int finalIdx = i;
            columns[i] = new TableColumn<>(
                    data_split[i]
            );
            columns[i].setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            data_table.getColumns().add(columns[i]);
        }
        RowDataGenerator dataGenerator = new RowDataGenerator();

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            data_table.getItems().add(
                    FXCollections.observableArrayList(
                            dataGenerator.getNext(data,separator,data_split.length)
                    )
            );
//                        System.out.println(data);
        }
        layout_content.getChildren().add(data_table);
        HBox hb = new HBox();
        Label label1 = new Label("Set the file name manualy: ");
        TextField textField = new TextField();
        textField.setText("File");
        Button button = new Button("Save the file");
        button.setOnAction(actionEvents ->  {
            int row_count = data_table.getItems().size();
            ObservableList table_data = data_table.getItems();
            for (int i = 0; i < row_count; i++){
                ObservableList row_data = (ObservableList)table_data.get(i);
                int row = i;
                Object it = data_table.getItems().get(row);
                table_row_save(row_data, textField.getText());
            }
            layout_content.getChildren().clear();
        });

        hb.getChildren().addAll(label1, textField,button);
        layout_content.getChildren().add(hb);
        myReader.close();
        return separator;
    }
}
