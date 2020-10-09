package outerhaven;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // Create MenuBar
        MenuBar menuBar = new MenuBar();

        // Create menus
        Menu fileMenu = new Menu("Fichier");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Aide");

        // Create MenuItems
        MenuItem newItem = new MenuItem("Nouveau");
        MenuItem openFileItem = new MenuItem("Ouvrir Fichier");
        MenuItem exitItem = new MenuItem("Quitter");

        MenuItem copyItem = new MenuItem("Copier");
        MenuItem pasteItem = new MenuItem("Coller");

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, openFileItem, exitItem);
        editMenu.getItems().addAll(copyItem, pasteItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        Scene scene = new Scene(root, 350, 200);

        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
/*public class App {
    public static void main(String[] args) {
        System.out.println("test");
    }
}*/

