package Menu;

import DataManagement.Main;
import MainMenu.MainMenu;
import MainMenu.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
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
    String location = "";

    public void display(String title) throws Exception, IOException,ParseException {
        layout.setId("lay");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(600);
        window.setHeight(600);
        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        String filePath = MainMenu.pathToWorkingDirectory +"/Uploaded_files.json" ;
        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(filePath));
        list.setOnMouseClicked(event -> {
            ListView<HBox> list_child = new ListView<HBox>();
            if (event.getClickCount()==2){
                String tx = list.getSelectionModel().getSelectedItem();
                layout.getChildren().clear();
                VBox header = new VBox();
                Text H1 = new Text(tx);
                H1.setId("H1");
                header.setAlignment(Pos.CENTER);
                header.getChildren().add(H1);
                layout.getChildren().add(header);
                location=tx;
                File file = new File( MainMenu.pathToWorkingDirectory + "/Uploaded/"+tx);
                String[] directories = file.list();
                ObservableList<HBox> itemsnew = FXCollections.observableArrayList();
                for (int i = 0; i < directories.length; i++) {
                    Button del = new Button("X");
                    del.setStyle("-fx-background-color: transparent; -fx-text-fill: red");
                    Text t = new Text(directories[i].replace(".csv",""));
                    HBox cont = new HBox();
                    itemsnew.add(cont);
                    itemsnew.get(i).getChildren().add(t);
                    itemsnew.get(i).getChildren().add(del);
                    itemsnew.get(i).setMargin(t, new Insets(0, 0, 0, 240));
                    itemsnew.get(i).setMargin(del, new Insets(0, 0, 0, 215));
                    del.setOnAction(e->{
                        itemsnew.remove(cont);
                        File myObj = new File(MainMenu.pathToWorkingDirectory + "/Uploaded/"+location+"/"+t.getText()+".csv");
                        myObj.delete();
                        try {
                            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(MainMenu.pathToWorkingDirectory +  "/categories.json"));
                            for (Object o : json.keySet()) {
                                JSONArray jsonArray = (JSONArray) json.get(o);
                                for (int j = 0; j < jsonArray.size(); j++) {
                                    if (j > jsonArray.size()){
                                        break;
                                    }
                                    if (((String) jsonArray.get(j)).contains("."+t.getText()+".")){
                                        jsonArray.remove(j);
                                    }
                                }
                            }

                            try (PrintWriter out = new PrintWriter(new FileWriter(MainMenu.pathToWorkingDirectory + "/categories.json"))) {
                                out.write(json.toString());
                            } catch (Exception eve) {
                                eve.printStackTrace();
                            }
                        } catch (IOException ev) {
                            throw new RuntimeException(ev);
                        } catch (ParseException ev) {
                            throw new RuntimeException(ev);
                        }

                        try {
                            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(MainMenu.pathToWorkingDirectory + "/Uploaded_files.json"));
                            json.remove(t.getText());

                            try (PrintWriter out = new PrintWriter(new FileWriter(MainMenu.pathToWorkingDirectory+ "/Uploaded_files.json"))) {
                                out.write(json.toString());
                            } catch (Exception eve) {
                                eve.printStackTrace();
                            }

                        } catch (IOException ev) {
                            throw new RuntimeException(ev);
                        } catch (ParseException ev) {
                            throw new RuntimeException(ev);
                        }
                    });
                    itemsnew.get(i).setStyle("-fx-pref-width: 600; -fx-alignment: center");
                }
                list_child.setItems(itemsnew);
                layout.getChildren().add(list_child);
//                 JSONArray data = (JSONArray) obj.get(File);
//                 ListView<String> File_list = new ListView<String>();
//                 ObservableList<String> File_items = FXCollections.observableArrayList();
//                 for (int i=0; i<data.size(); i++){
//                     File_items.add((String) data.get(i));
//                 }
//                 File_list.setItems(File_items);
//                 layout.getChildren().add(File_list);
            }
        });
        File file = new File(MainMenu.pathToWorkingDirectory +  "/Uploaded");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for (int i = 0; i < directories.length; i++) {
            items.add(directories[i]);
        }
        list.setId("listView");
        list.setPrefHeight(items.size() * 55);
        list.setItems(items);

        layout.getChildren().add(list);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add((new File("src/main/java/res/style_viewer.css")).toURI().toString());
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
        window.setScene(scene);
        window.showAndWait();
    }
}
