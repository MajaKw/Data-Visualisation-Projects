package searchEngine;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    public String filePath;
    ArrayList<String> words;
    Object obj;
    JSONArray jo;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<String> listView;

    public Controller() throws IOException, ParseException {
        System.out.println("---------------creation----------------");
        filePath = "src/main/resources/searchEngine/countries.json" ;
        words = new ArrayList<>();
        this.obj = new JSONParser().parse(new FileReader(filePath));
        this.jo = (JSONArray) obj;
    }
    public void setPath(String filePath) throws IOException, ParseException {
        this.filePath = filePath;

    }

    @FXML
    void search(KeyEvent evt) {
        listView.getItems().clear();
        listView.getItems().addAll(searchList(searchBar.getText(),words));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        words.clear();
        for (int i = 0; i<jo.size(); i++){
            JSONObject obj = (JSONObject) jo.get(i);
            String name = (String) obj.get("name");
            words.add(name);
        }
        listView.getItems().addAll(words);
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings) {

        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));

        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }
}
/*
<!--<?xml version="1.0" encoding="UTF-8"?>-->

<!--<?import java.lang.*?>-->
<!--<?import java.util.*?>-->
<!--<?import javafx.scene.*?>-->
<!--<?import javafx.scene.control.*?>-->
<!--<?import javafx.scene.layout.*?>-->

<!--<AnchorPane xmlns="http://javafx.com/javafx"-->
<!--            xmlns:fx="http://javafx.com/fxml"-->
<!--            fx:controller="searchEngine.Search"-->
<!--            prefHeight="400.0" prefWidth="600.0">-->

<!--</AnchorPane>-->
*/