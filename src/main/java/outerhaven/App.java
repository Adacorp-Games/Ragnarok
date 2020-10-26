package outerhaven;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ragnarok");
        stage.setHeight(600);
        stage.setWidth(800);
        //stage.setFullScreen(true);
        Plateau game = new Plateau(100,stage);
        game.lancerParti();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}

