package searchEngine;

import app.App;
import app.TwoDimensionalBarChart;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
        MenuItem countries_menu, categories_menu;
        countries_menu = new MenuItem("countires"); categories_menu = new MenuItem("categories");
        menu_options.getItems().addAll(countries_menu, categories_menu);
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

//        layout.setAlignment(Pos.CENTER);
        // **
        // *** wyszukiwarka do categorii
        HBox categories_Menu = new HBox();

        // adding scene to the window
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}
