
module searchEngine {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    requires java.desktop;
    requires com.google.common;

    exports Menu;
    exports MainMenu;
    exports searchEngine;

    opens Menu to
            javafx.fxml;
    opens searchEngine to
            javafx.fxml;
}
