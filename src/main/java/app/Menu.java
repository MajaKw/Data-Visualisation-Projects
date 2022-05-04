package app;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public class Menu extends Application {
    Stage window;
//	BorderPane layout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("data visualisation");

        BorderPane root = new BorderPane();
        HBox top_menu = new HBox();

        // just items for menu
        MenuItem menuItem_add = new MenuItem("add");
        MenuItem menuItem_delete = new MenuItem("delete");
        MenuItem menuItem_diagram = new MenuItem("diagram");
        MenuItem menuItem_counties = new MenuItem("countires");
        MenuItem menuItem_categories = new MenuItem("categories");

        // menu that is seen
        Button button_clean = new Button("clean");
        MenuButton menu_plus = new MenuButton("+", null, menuItem_diagram, menuItem_counties, menuItem_categories);
        MenuButton menu_data = new MenuButton("data", null,  menuItem_add, menuItem_delete);

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

        // positioning
        HBox box_data = new HBox(menu_data);
        HBox right_corner = new HBox(menu_plus);
        right_corner.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(right_corner, Priority.ALWAYS);
        top_menu.getChildren().addAll(box_data, button_clean, right_corner);
        root.setTop(top_menu);
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        Scene scene = new Scene(root, 400, 300);
        // odniesienie do pliku css
        scene.getStylesheets().add((new File("src/main/java/res/style.css")).toURI().toString());

        window.setScene(scene);
        window.show();

    }

}
