package outerhaven;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ragnarok");
        stage.setFullScreen(true);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("./Images/Ragnarok.png")));
        Plateau game = new Plateau(stage);
        game.lancerPartie();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}