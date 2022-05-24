package Menu;

import MainMenu.Settings;
import app.App;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import Menu.DiagramWindow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Menu extends Application {
    Stage window;

    public static void main(String[] args) {
        // filling countries.json with entries of the format table_name.column_name
//        Connection conn = App.connect();
//        try{
//            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Owner\\Documents\\Projekty\\oopProjekt\\src\\main\\resources\\searchEngine\\countries.json"));
//            String SQLquery = "SELECT table_name, column_name " +
//                    "FROM information_schema.columns " +
//                    "WHERE table_schema NOT IN ('pg_catalog', 'information_schema')";
//            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmt.executeQuery(SQLquery);
//            writer.write("[\n");
//            while(rs.next()) {
//                writer.append('{' + "\"name\": \"" + rs.getString("table_name") + '.' + rs.getString("column_name") + "\"}");
//                if(!rs.isLast()) writer.append(",\n");
//            }
//            writer.append("\n]");
//            writer.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("data visualisation");

        // loading ChartSetUpWindow






        BorderPane root = new BorderPane();
        HBox top_menu = new HBox();
//        HBox countries_menu = new HBox();
//        Pane newLoadedPane =  FXMLLoader.load(getClass().getResource("search.fxml"));
//        countries_menu.getChildren().add(newLoadedPane);

        // just items for menu
        MenuItem menuItem_add = new MenuItem("add");
        MenuItem menuItem_delete = new MenuItem("delete");
//        MenuItem menuItem_diagram = new MenuItem("diagram");

//        MenuItem menuItem_counties = new MenuItem("countires");
//        menuItem_counties.setOnAction((event) -> root.getChildren().add(countries_menu));
//        ListView list = (ListView) countries_menu.lookup("#listView");
//        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            // funckja ktora sie wywoluje po kliknieciu myszy
//            @Override
//            public void handle(MouseEvent event) {
//                String country = (String) list.getSelectionModel().getSelectedItem();
//                System.out.println("clicked on " + country);
//                root.getChildren().remove(countries_menu);
//            }
//        });
//        MenuItem menuItem_categories = new MenuItem("categories");

        // menu that is seen
        Button button_clean = new Button("clean");

        Button button_plus = new Button("+");
        Button button_upload = new Button("Upload");

//        MenuButton menu_plus = new MenuButton("+", null, menuItem_counties);
        MenuButton menu_data = new MenuButton("data", null,  menuItem_add, menuItem_delete);

        // positioning
        HBox box_data = new HBox(menu_data);
        HBox right_corner = new HBox(button_upload,button_plus);
//        HBox right_corner = new HBox(menu_plus);
        right_corner.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(right_corner, Priority.ALWAYS);
        top_menu.getChildren().addAll(box_data, button_clean, right_corner);
        root.setTop(top_menu);
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        DiagramWindow diagramWindow = new DiagramWindow();

        button_plus.setOnAction(event -> {
            try {
                new ChartSetUpWindow().display(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        button_upload.setOnAction(event -> {
            try {
                diagramWindow.display("diagram settings");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        menu_plus.setOnAction(event -> DiagramWindow.display("diagram", "choose your settings"));

        Scene scene = new Scene(root, 400, 300);
        // odniesienie do pliku css
        scene.getStylesheets().add((new File("src/main/java/res/style.css")).toURI().toString());
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        window.setScene(scene);
        window.show();

    }

}
// dodawanie ikony, ta jest bardzo brzydka ale tu jest jak to robic zeby w miare dzialalo wiec gdzies sie moze przydac, nie usuwac tego
//        FileInputStream input = new FileInputStream("plus.png");
//        Image image = new Image(input);
//        ImageView imageView = new ImageView(image);
//        imageView.setFitHeight(30);
//        imageView.setFitWidth(30);
//        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, false, false, false);
//        BackgroundImage scaledImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//        menu_plus.setBackground(new Background(scaledImage));
//        menu_plus.setGraphic(imageView);
