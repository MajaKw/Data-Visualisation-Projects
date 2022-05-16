package Menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class ChartSetUpWindow {

    public static void display(Stage stage){
        // load the .fxml file created in Scene Builder
        try {
            Parent root = FXMLLoader.load(ChartSetUpWindow.class.getResource("ChartSetUpWindow.fxml"));
            // searching for button2D and setting it to be selected
            for(var tmp : LoopingOverSceneGraph.loop(root, Node.class)) {
                if(tmp.getId() == null) continue;
                if(tmp.getId().equals("button2D")) ((ToggleButton) tmp).setSelected(true);
                else if(tmp.getId().equals("zAxisInputLabel")) tmp.setVisible(false);
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}