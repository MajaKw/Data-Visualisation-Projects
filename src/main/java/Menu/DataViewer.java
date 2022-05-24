package Menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.event.MouseEvent;
import java.io.*;

public class DataViewer {
    Stage window = new Stage();
    VBox layout = new VBox();

    public void display(String title) throws Exception, IOException,ParseException {
        layout.setId("lay");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(600);
        window.setHeight(600);
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        String filePath = "src/main/resources/Uploaded_files.json" ;
        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(filePath));
        list.setOnMouseClicked(event -> {
            if (event.getClickCount()==2){
                String File = list.getSelectionModel().getSelectedItem();
                layout.getChildren().clear();
                VBox header = new VBox();
                Text H1 = new Text(File);
                H1.setId("H1");
                header.setAlignment(Pos.CENTER);
                 header.getChildren().add(H1);
                 layout.getChildren().add(header);
                 JSONArray data = (JSONArray) obj.get(File);
                 ListView<String> File_list = new ListView<String>();
                 ObservableList<String> File_items = FXCollections.observableArrayList();
                 for (int i=0; i<data.size(); i++){
                     File_items.add((String) data.get(i));
                 }
                 File_list.setItems(File_items);
                 layout.getChildren().add(File_list);
                 System.out.println(data.get(0));
            }
        });
        File f = new File("src/main/resources/Uploaded");
        String[] pathnames = f.list();
        for (int i = 0; i < pathnames.length; i++) {
            items.add(pathnames[i].replace(".csv",""));
        }
        list.setId("listView");
        list.setPrefHeight(items.size() * 55);
        list.setItems(items);

        layout.getChildren().add(list);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add((new File("src/main/java/res/style_viewer.css")).toURI().toString());
        window.setScene(scene);
        window.showAndWait();
    }
}
