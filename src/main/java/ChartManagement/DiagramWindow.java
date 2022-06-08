package ChartManagement;

import MainMenu.MainMenu;
import MainMenu.Settings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;
import java.util.List;

import com.google.common.io.Files;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DiagramWindow {
    Stage window = new Stage();
    VBox layout_content = new VBox();
    TextField textField2 = new TextField();
    ComboBox<String> comboBox;
    ComboBox<String> comboBox2;
    ComboBox<String> comboBox3;
    TextField textField3_new = new TextField();


    String path_format = "%s.%s.%s";

    Text error = new Text(10,50,"");
    HBox error_layout = new HBox();
    Map<String, String> hm = new HashMap<>();


    public void display(String title) {
        // zabezpieczenie ze wymusza kliknac w to okno i sie nim zajac (a nie tym "od spodem")
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(900);
        window.setHeight(620);
        // ** wyszukiwarka krajow




        VBox layout = new VBox();
        layout.setSpacing(10);
//        layout_content.getChildren().add(root);
        layout_content.setSpacing(10);


        error.setFill(Color.RED);
        error_layout.setAlignment(Pos.CENTER);
        error_layout.getChildren().add(error);


//        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            // funckja ktora sie wywoluje po kliknieciu myszy (czyli np mozna nia zrobic chyba zeby sie dobry wykres towrzyl)
//            @Override
//            public void handle(MouseEvent event) {
//                String yColumn = (String) list.getSelectionModel().getSelectedItem();
//                System.out.println("secondly clicked on " + yColumn);
//                layout.getChildren().remove(search_menu);
//            }
//        });
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        List<String> extensions = Arrays.asList("csv", "txt");

        File uploaded_file = fileChooser.showOpenDialog(window);
        //ERROR CHECKS
        if(uploaded_file == null){
            error.setText("No file chosen");
            return;
        }
        System.out.println(uploaded_file);
        String extension = Files.getFileExtension(uploaded_file.getAbsolutePath());
        long file_size = uploaded_file.length();
        if (!extensions.contains(extension)) {
            error.setText("File extension not in allowed: " + extensions.toString());
            return;
        }
        if(file_size>500000){
            error.setText("File is too big");
            return;
        }
        //END
        try {
            String separator = table_creation("",uploaded_file);

            Label label1 = new Label("Separator:");
            TextField textField = new TextField();
            textField.setText(separator);

            Button button = new Button("Change");
            HBox hb = new HBox();
            hb.getChildren().addAll(label1, textField,button);
            hb.setSpacing(10);
            layout_content.getChildren().add(hb);

            button.setOnAction(actionEvents ->  {
                try {
                    hm.clear();
                    layout_content.getChildren().clear();
                    table_creation(textField.getText(),uploaded_file);
                    layout_content.getChildren().add(hb);
                }catch (FileNotFoundException e) {
                    error.setText("File was not found");
                    return;
                }
            });



        } catch (FileNotFoundException e) {
            error.setText("File was not found");
            return;
        }

//        layout.setAlignment(Pos.CENTER);
        // **
        // *** wyszukiwarka do categorii
        HBox categories_Menu = new HBox();
        layout_content.getChildren().add(error_layout);
        layout.getChildren().add(layout_content);


        // adding scene to the window
        Scene scene = new Scene(layout);
        if(Settings.isDarkMode){
            scene.getStylesheets().add("DarkMode.css");
        }
        else {
            scene.getStylesheets().add("LightMode.css");
        }
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
            File directory = new File(MainMenu.pathToWorkingDirectory + "/Uploaded/"+comboBox.getValue());
            if (! directory.exists()){
                directory.mkdir();
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(MainMenu.pathToWorkingDirectory + "/Uploaded/"+comboBox.getValue()+"/"+file_name.replace("/","").replaceAll("[-+.^:/\\,]","")+".csv", true));
            if (Integer.parseInt(hm.get("X"))<Integer.parseInt(hm.get("Y"))){
                for (int j=0; j<row_data.size(); j++){
                    if (!hm.get("X").equals(Integer.toString(j)) && !hm.get("Y").equals(Integer.toString(j))&&!row_data.get(0).equals("---EOD---")){
                        continue;
                    }
                    if (j==Integer.parseInt(hm.get("Y"))){
                        writer.append((String) row_data.get(j));
                    }else{
                        writer.append((String) row_data.get(j)+";");
                    }
                }
            } else{
                for (int j=row_data.size()-1; j>-1; j--){
                    System.out.println(row_data.get(j));
                    if (!hm.get("X").equals(Integer.toString(j)) && !hm.get("Y").equals(Integer.toString(j))&&!row_data.get(0).equals("---EOD---")){
                        continue;
                    }
                    if (j==Integer.parseInt(hm.get("Y"))){
                        writer.append((String) row_data.get(j));
                    }else{
                        writer.append((String) row_data.get(j)+";");
                    }
                }
            }
            writer.append("\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void save_new_category(String category){
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(MainMenu.pathToWorkingDirectory + "/categories.json"));
            if(!obj.containsKey(category)){
                obj.put(category, new ArrayList<String>());
            }
            try (PrintWriter out = new PrintWriter(new FileWriter(MainMenu.pathToWorkingDirectory + "/categories.json"))) {
                out.write(obj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    ObservableList<String> get_categories(){
        ObservableList<String> result = FXCollections.observableArrayList();
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(MainMenu.pathToWorkingDirectory + "/categories.json"));
            obj.keySet().stream().forEach((key) -> result.add((String) key));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    void save_to_json(String filePath, String data, Boolean Cat){
        String category = comboBox.getValue();
        if (category == null){

        }
        if (Cat){
            String temp = category;
            category = data;
            data = temp;
        }
        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(filePath));
            if(obj.get(data) == null){
                obj.put(data, new JSONArray());
            };
            JSONArray jsonArray = (JSONArray) obj.get(data);
            if (Cat){
                jsonArray.add(String.format(path_format,data,category,textField2.getText()) );
            }else{
                jsonArray.add(String.format(path_format,category,data,textField2.getText()) );
            }

            obj.put(data, jsonArray);
            try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
                out.write(obj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    String table_creation(String separator, File uploaded_file) throws FileNotFoundException{
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
        MenuItem mi2 = new MenuItem("Set as default X");
        MenuItem mi3 = new MenuItem("Set as default Y");

        hm.put("X", null);
        hm.put("Y", null);
        mi2.setOnAction((ActionEvent event) -> {
            TablePosition item = (TablePosition) data_table.getSelectionModel().getSelectedCells().get(0);
            TableColumn col = item.getTableColumn();
            System.out.println(col.getId());
            hm.put("X",col.getId());
            col.setStyle("-fx-border-color: red");
        });
        mi3.setOnAction((ActionEvent event) -> {
            TablePosition item = (TablePosition) data_table.getSelectionModel().getSelectedCells().get(0);
            TableColumn col = item.getTableColumn();
            hm.put("Y",col.getId());
            col.setStyle("-fx-border-color: green");
        });

        mi1.setOnAction((ActionEvent event) -> {
            System.out.println("W");
            if(textField3_new.getText()!=""){
                comboBox.setValue(textField3_new.getText());
                save_new_category(textField3_new.getText());
            };
            if (comboBox.getValue() == null || textField2.getText().equals("") || comboBox2.getValue() == null || comboBox3.getValue()==null){
                error.setText("Please fill all of the data");
                return;
            }


            TablePosition item = (TablePosition) data_table.getSelectionModel().getSelectedCells().get(0);
            int row_count = data_table.getItems().size();
            TableColumn col = item.getTableColumn();
            ObservableList table_data = data_table.getItems();
            List<String> done = new ArrayList<>(Arrays.asList());
            ObservableList row1 = (ObservableList)table_data.get(0);
            for (int i = 0; i < row_count; i++){
                int row = i;
                Object it = data_table.getItems().get(row);
                String data = (String) col.getCellObservableValue(it).getValue();
                try {
                    JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(MainMenu.pathToWorkingDirectory + "/categories.json"));
                    JSONArray catar = (JSONArray) obj.get(comboBox.getValue());
                    if (catar.contains(String.format(path_format,comboBox.getValue(),data,textField2.getText()))){
                        error.setText("That metric already exists: "+String.format(path_format,comboBox.getValue(),data,textField2.getText()));
                        return;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                if (!done.contains(data)){
                    ObservableList<String> labels = FXCollections.observableArrayList();
                    for (int f = 0; f < row1.size(); f++){
                        if(Integer.toString(f).equals(hm.get("X"))){
                            labels.add("X|"+comboBox2.getValue());
                        }else if(Integer.toString(f).equals(hm.get("Y"))){
                            labels.add("Y|"+comboBox3.getValue());
                        }else{
                            labels.add("-");
                        }
                    }
                    table_row_save(labels, data);
                }
                done.add(data);
            }
            for (int i = 0; i < row_count; i++){
                ObservableList row_data = (ObservableList)table_data.get(i);
                int row = i;
                Object it = data_table.getItems().get(row);
                String data = (String) col.getCellObservableValue(it).getValue();
                table_row_save(row_data, data);
            }
            List<String> done2 = new ArrayList<>(Arrays.asList());
            for (int i = 0; i < row_count; i++){
                int row = i;
                Object it = data_table.getItems().get(row);
                String data = (String) col.getCellObservableValue(it).getValue();
                ObservableList<String> labels = FXCollections.observableArrayList();
                labels.add("---EOD---");
                if (!done2.contains(data)) {
                    table_row_save(labels, data);
                    String filePath = MainMenu.pathToWorkingDirectory +"/Uploaded_files.json" ;
                    save_to_json(filePath,data,false);
                    String filePath2 =  MainMenu.pathToWorkingDirectory + "/categories.json" ;
                    save_to_json(filePath2,data,true);
                }
                done2.add(data);
            }
            window.close();
        });


        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(mi2,mi3,mi1);
        data_table.setContextMenu(menu);
        data_table.getSelectionModel().setCellSelectionEnabled(true);
        data_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn<ObservableList<String>, String>[] columns = new TableColumn[data_split.length];
        for (int i = 0; i<data_split.length;i++){
            final int finalIdx = i;
            columns[i] = new TableColumn<>(
                    data_split[i]
            );
            columns[i].setId(Integer.toString(i));
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
        HBox settings = new HBox();
        VBox vblabel = new VBox();
        vblabel.setId("label");
        Label label1 = new Label("Set the file name manualy");
        TextField textField = new TextField();
        vblabel.getChildren().addAll(label1, textField);
        Label label2 = new Label("Add metric name");
        ObservableList<String> options = get_categories();
        ObservableList<String> options2 = FXCollections.observableArrayList();
        options2.add("YEAR");
        options2.add("NUMBER");
        options2.add("PERCENT");
        options2.add("LABEL");
        VBox vblabel2 = new VBox();
        VBox vblabel3 = new VBox();
        VBox vblabel3_new = new VBox();
        VBox vblabel4 = new VBox();
        VBox vblabel5 = new VBox();
        Label label3 = new Label("Categories name");
        vblabel2.setAlignment(Pos.CENTER);
        vblabel3.setAlignment(Pos.CENTER);
        vblabel3_new.setAlignment(Pos.CENTER);
        vblabel5.setAlignment(Pos.CENTER);
        vblabel4.setAlignment(Pos.CENTER);
        Label label3_new = new Label("New category name");

        Label label4 = new Label("X unit: ");
        label4.setAlignment(Pos.CENTER);
        Label label5 = new Label("Y unit: ");
        vblabel2.getChildren().addAll(label2, textField2);
        comboBox = new ComboBox(options);
        comboBox2 = new ComboBox(options2);
        comboBox3 = new ComboBox(options2);
        vblabel3.getChildren().addAll(label3, comboBox);
        vblabel3_new.getChildren().addAll(label3_new, textField3_new);
        vblabel4.getChildren().addAll(label4, comboBox2);
        vblabel5.getChildren().addAll(label5, comboBox3);
        textField.setText("File");
        Button button = new Button("Save the file");
        button.setOnAction(actionEvents ->  {
            if (comboBox.getValue() == null || textField2.getText().equals("") || comboBox2.getValue() == null || comboBox3.getValue()==null){
                error.setText("Please fill all of the data");
                return;
            }
            int row_count = data_table.getItems().size();
            ObservableList table_data = data_table.getItems();
            ObservableList<String> labels = FXCollections.observableArrayList();
            ObservableList row1 = (ObservableList)table_data.get(0);
            for (int i = 0; i < row1.size(); i++){
                if(Integer.toString(i).equals(hm.get("X"))){
                    labels.add("X|"+comboBox2.getValue());
                }else if(Integer.toString(i).equals(hm.get("Y"))){
                    labels.add("Y|"+comboBox3.getValue());
                }else{
                    labels.add("-");
                }

            }
            table_row_save(labels, textField.getText());
            for (int i = 0; i < row_count; i++){
                ObservableList row_data = (ObservableList)table_data.get(i);
                int row = i;
                Object it = data_table.getItems().get(row);
                table_row_save(row_data, textField.getText());
            }
            window.close();
        });

        settings.getChildren().addAll(vblabel2,vblabel3, vblabel3_new,vblabel4,vblabel5);
        layout_content.getChildren().add(settings);
        myReader.close();
        return separator;
    }
}