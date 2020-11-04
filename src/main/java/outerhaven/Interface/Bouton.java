package outerhaven.Interface;

import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import outerhaven.Plateau;

public class Bouton {
    public static Effect innerShadowOrange = new Effets().putInnerShadow(Color.ORANGE);
    public static Effect innerShadowBlack = new Effets().putInnerShadow(Color.BLACK);
    public static Effect effectE1 = new Effets().putInnerShadow(Plateau.getE1().getCouleur());
    public static Effect effectE2 = new Effets().putInnerShadow(Plateau.getE2().getCouleur());

    // Pour faire des boutons spécialements conçus pour le projet et éviter la redondance
    public Button creerBouton(String texte) {
        Button button = new Button(texte);
        button.setMinSize(100,50);
        button.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        button.setOnMouseEntered(mouseEvent -> {
            if (button.getEffect() == null) {
                button.setEffect(innerShadowBlack);
            }
        });
        button.setOnMouseExited(mouseEvent -> {
            if (button.getEffect().equals(innerShadowBlack)) {
                button.setEffect(null);
            }
        });
        return button;
    }
}