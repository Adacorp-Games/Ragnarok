package outerhaven;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Ragnarok");
        stage.setFullScreen(true);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/Images/Personnes/Ragnarok.png")));
        Plateau game = new Plateau(stage);
        game.lancerPartie();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}