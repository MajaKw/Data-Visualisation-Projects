
module searchEngine {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    requires java.desktop;
    requires com.google.common;
    requires java.prefs;

    exports Menu;
    exports MainMenu;
    exports DataManagement;

    opens Menu to
            javafx.fxml;
    opens MainMenu to
            javafx.fxml;
    opens DataManagement to
            javafx.fxml;
}
