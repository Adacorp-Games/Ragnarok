package outerhaven.Interface;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Personne;
import static outerhaven.Plateau.*;

import java.util.ArrayList;

public class BarrePersonnage{

    private Group group = new Group();
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    ArrayList<Personne> listClasse = new ArrayList<Personne>();

    public BarrePersonnage() {
        // Ajoutez les nouvelles Classe personnages ici                           <-------------------------------------------

        listClasse.add(new Archer());
        listClasse.add(new Guerrier());
        for (int i = 0; i < listClasse.size(); i++) {
            personnages.remove(0);
        }
        genereBarre();
    }

    public void genereBarre() {
        Rectangle barre = new Rectangle();
        barre.setWidth(longeurMax-20);
        barre.setHeight(200);
        barre.setX(10);
        barre.setY(largeurMax - barre.getHeight());
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);
        barre.setFill(Color.LIGHTGRAY);
        group.getChildren().add(barre);
        for (int i = 0; i < listClasse.size(); i++) {
            group.getChildren().add(listClasse.get(i).affichagePersonnageBarre(i));
        }
    }

    public Group returnBarre(){
        return group;
    }

}
