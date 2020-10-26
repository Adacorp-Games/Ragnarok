package outerhaven;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ragnarok");
        stage.setFullScreen(true);
        Plateau game = new Plateau(100,stage);
        game.lancerScenePlateau();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
    //coucou  alex^^
}

