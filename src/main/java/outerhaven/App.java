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
    stage.setTitle("test");
    Plateau test = new Plateau(5,stage);
    test.lancerScenePlateu();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}

