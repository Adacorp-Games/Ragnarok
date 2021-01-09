package outerhaven;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Le role du Main est de lancer le jeu avec des paramètres de base :
     * - nom de la fenêtre
     * - icône de la fenêtre
     * - taille de la fenêtre (plein écran ou non)
     */

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