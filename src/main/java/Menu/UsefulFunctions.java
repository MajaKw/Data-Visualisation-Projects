package Menu;

import MainMenu.MainMenu;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public class UsefulFunctions {
    // returning all elements of the specified type contained in the scene graph
    public static <T> List<T> loopOverSceneGraph(Parent parent, Class<T> type) {
        List<T> elements = new LinkedList<>();
        for(Node node : parent.getChildrenUnmodifiable()) {
            if(node instanceof Pane) elements.addAll(loopOverSceneGraph((Pane) node, type));
            if(type.isAssignableFrom(node.getClass())) elements.add((T) node);
        }
        return Collections.unmodifiableList(elements);
    }

    public static int getColumnIndex(String path, String columnName) {
        //using data location
        String file = MainMenu.pathToWorkingDirectory + "/Uploaded/"+path;
        int index = -1;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] names = line.split(";");
            for(int i=0; i<names.length; i++) {
                if(names[i].equals(columnName)) index = i;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return index-1;
    }

    public static String getColumnName(String path, int idx) {
        // using data location
        String file = MainMenu.pathToWorkingDirectory + "/Uploaded/"+path;
        String columnName = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] names = line.split(";");
            columnName = names[idx];
        } catch(Exception e) {
            if(e.getClass() != ArrayIndexOutOfBoundsException.class) e.printStackTrace();
        }
        return columnName;
    }
}
