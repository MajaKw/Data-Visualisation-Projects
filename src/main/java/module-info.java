module searchEngine {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    requires java.desktop;

    opens Menu to javafx.fxml;
    exports Menu;
    opens searchEngine to javafx.fxml;
    exports searchEngine;
    opens MainMenu to javafx.fxml;
    exports MainMenu;
}