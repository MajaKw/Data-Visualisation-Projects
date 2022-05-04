module changeThisModuleName {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.sql;

    opens app to javafx.fxml;
    exports app;
}