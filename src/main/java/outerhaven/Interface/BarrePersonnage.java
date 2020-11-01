package outerhaven.Interface;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import static outerhaven.Plateau.*;

import java.util.ArrayList;

public class BarrePersonnage {

    private Group group = new Group();
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    ArrayList<Personne> listClasse = new ArrayList<>();

    public BarrePersonnage() {
        // Ajoutez les nouvelles Classe personnages ici                           <-------------------------------------------

        listClasse.add(new Guerrier());
        listClasse.add(new Archer());
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
        group.getChildren().add(boutonEquipe());
        for (int i = 0; i < listClasse.size(); i++) {
            group.getChildren().add(listClasse.get(i).affichagePersonnageBarre(i));
        }
    }

    /* Cette fonction est présente dans cette classe car cela nous permet de faire disparaitre en meme temps que la
    barre de personnage les boutons d'équipes lorsque l'on lance la partie */
    private Group boutonEquipe() {
        Button equipe1 = new Button("Equipe 1");
        equipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        equipe1.setLayoutX(10);
        equipe1.setLayoutY(800);
        equipe1.setMinSize(60, 20);

        Button equipe2 = new Button("Equipe 2");
        equipe2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        equipe2.setLayoutX(80);
        equipe2.setLayoutY(800);
        equipe2.setMinSize(60, 20);

        // Actions sur les boutons d'équipes
        equipe1.setOnMouseClicked(mouseEvent -> {
            Plateau.incorporeEquipe(Plateau.getE1());
            equipe1.setEffect(new Effets().putInnerShadow(Plateau.getE1().getCouleur()));
            equipe2.setEffect(null);
        });
        equipe2.setOnMouseClicked(mouseEvent -> {
            Plateau.incorporeEquipe(Plateau.getE2());
            equipe2.setEffect(new Effets().putInnerShadow(Plateau.getE2().getCouleur()));
            equipe1.setEffect(null);
        });

        Group groupEquipeButton = new Group();
        if (!statusPartie){
            groupEquipeButton.getChildren().add(equipe1);
            groupEquipeButton.getChildren().add(equipe2);
        }
        else{
            equipe1.setVisible(true);
            equipe2.setVisible(true);
        }

        return groupEquipeButton;
    }

    public Group returnBarre(){
        return group;
    }
}
