package outerhaven.Interface;

import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class Effets {

    public InnerShadow putInnerShadow(Color couleur) {
        InnerShadow shadow = new InnerShadow();
        shadow.setColor(couleur);
        return shadow;
    }
}
