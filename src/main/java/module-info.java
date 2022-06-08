
module searchEngine {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires json.simple;
    requires java.sql;
    requires java.desktop;
    requires com.google.common;
    requires java.prefs;

    exports ChartManagement;
    exports MainMenu;
    exports DataManagement;

    opens ChartManagement to
            javafx.fxml;
    opens MainMenu to
            javafx.fxml;
    opens DataManagement to
            javafx.fxml;
}
