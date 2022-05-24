package Menu;

import MainMenu.MainMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;


public class ControllerOfChartSetUpWindow {
    // TODO: correct window resizing (so that buttons and fields stay centered)
    @FXML
    private ToggleButton button3D;
    @FXML
    private ToggleButton button2D;
    @FXML
    private TextField xAxisInputField;
    @FXML
    private TextField yAxisInputField;
    @FXML
    private TextField zAxisInputField;
    @FXML
    private Label zAxisInputLabel;
    @FXML
    private Button backToMainMenuButton;


    @FXML
    public void initialize() {
        button2D.setSelected(true);
        zAxisInputLabel.setVisible(false);
    }
    public void click2D(ActionEvent e) {
        button2D.setSelected(true);
        button3D.setSelected(false);
        zAxisInputField.setVisible(false);
        zAxisInputLabel.setVisible(false);
    }
    public void click3D(ActionEvent e) {
        button2D.setSelected(false);
        button3D.setSelected(true);
        zAxisInputField.setVisible(true);
        zAxisInputLabel.setVisible(true);
    }

    public void createChart(ActionEvent e) {
        //create new window containing chart with specified data
        if(zAxisInputField.isVisible()) ChartWindow.showChartWindow(xAxisInputField.getText(), yAxisInputField.getText(), zAxisInputField.getText());
        else ChartWindow.showChartWindow(xAxisInputField.getText(), yAxisInputField.getText(), null);
    }

    public void backToMainMenu(ActionEvent e) {
        MainMenu mainMenu = new MainMenu();
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        mainMenu.show(stage);
    }
}
