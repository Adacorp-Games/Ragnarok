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
        genereBarre();
    }

    public void genereBarre() {
        /*ImageView imageFond = new ImageView(new Image("https://cdn.discordapp.com/attachments/764528562429624391/770430078403870740/barrelong.png"));
        imageFond.setFitWidth(longeurMax-20);
        imageFond.setFitHeight(200);
        imageFond.setX(10);
        imageFond.setY(largeurMax - imageFond.getFitHeight());
        group.getChildren().add(imageFond);*/

        Rectangle barre = new Rectangle();
        barre.setWidth(longeurMax-20);
        barre.setHeight(200);
        barre.setX(10);
        barre.setY(largeurMax - barre.getHeight());
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);
        barre.setFill(Color.LIGHTGRAY);
        //barre.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        group.getChildren().add(barre);

        for (int i = 0; i < listClasse.size(); i++) {
            group.getChildren().add(listClasse.get(i).affichagePersonnageBarre(i));
        }
    }

    public Group returnBarre(){
        return group;
    }

}
