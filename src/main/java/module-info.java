module searchEngine {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    requires java.desktop;

    opens Menu to javafx.fxml;
    opens searchEngine to javafx.fxml;
    exports Menu;
    exports searchEngine;
}