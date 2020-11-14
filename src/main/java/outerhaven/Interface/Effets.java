package outerhaven.Interface;

import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class Effets {

    // Méthode permettant de raccourcir l'action de mettre une ombre interne sur quelque chose
    public InnerShadow putInnerShadow(Color couleur) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(couleur);
        shadow.setHeight(30);
        return shadow;
    }
}
