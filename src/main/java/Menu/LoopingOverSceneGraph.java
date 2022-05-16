package Menu;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LoopingOverSceneGraph {
    public static <T> List<T> loop(Parent parent, Class<T> type) {
        List<T> elements = new LinkedList<>();
        for(Node node : parent.getChildrenUnmodifiable()) {
            if(node instanceof Pane) elements.addAll(loop((Pane) node, type));
            else if(type.isAssignableFrom(node.getClass())) elements.add((T) node);
        }
        return Collections.unmodifiableList(elements);
    }
}
