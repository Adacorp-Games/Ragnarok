package outerhaven.Interface;

import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 * Classe ayant pour but de raccourcir la gestion des effets (Effect) sur divers objets provenant de la librairie JavaFX.
 */

public class Effets {

    // Méthode permettant de raccourcir l'action de mettre une ombre interne sur quelque chose
    public InnerShadow putInnerShadow(Color couleur) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(couleur);
        shadow.setHeight(30);
        return shadow;
    }
}
