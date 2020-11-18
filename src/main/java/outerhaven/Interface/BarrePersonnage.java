package outerhaven.Interface;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Mage;
import outerhaven.Personnages.PersonnagesEnergetiques.Samourai;
import outerhaven.Personnages.PersonnagesMagiques.*;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import static outerhaven.Plateau.*;

import java.util.ArrayList;

public class BarrePersonnage {

    private Group group = new Group();
    public double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    public double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    public ArrayList<Personne> listClasse = new ArrayList<>();
    private Group argentGroup = new Group();

    public BarrePersonnage() {
        // Ajoutez les nouvelles Classe personnages ici                           <-------------------------------------------
        listClasse.add(new Guerrier());
        listClasse.add(new Archer());
        listClasse.add(new Mage());
        listClasse.add(new Paladin());
        listClasse.add(new Necromancien());
        listClasse.add(new Alchimiste());
        listClasse.add(new Archimage());
        listClasse.add(new Pretre());
        listClasse.add(new Samourai());
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
        equipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        equipe1.setLayoutX(10);
        equipe1.setLayoutY(780);
        equipe1.setMinSize(100, 50);

        Button equipe2 = new Button("Equipe 2");
        equipe2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        equipe2.setLayoutX(120);
        equipe2.setLayoutY(780);
        equipe2.setMinSize(100, 50);

        // Actions sur les boutons d'équipes
        equipe1.setOnMouseClicked(mouseEvent -> {
            Plateau.incorporeEquipe(Plateau.getE1());
            equipe1.setEffect(Bouton.effectE1);
            equipe2.setEffect(null);
            for (Personne p : listClasse) {
                if (personneSelectionne == p) {
                    p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                } else {
                    p.getImageperson().setEffect(null);
                }
            }
        });

        equipe1.setOnMouseEntered(mouseEvent -> {
            equipe1.setEffect(Bouton.effectE1);
        });

        equipe1.setOnMouseExited(mouseEvent -> {
            if (equipe1.getEffect() == Bouton.effectE1 && equipeSelectionne != Plateau.getE1()) {
                equipe1.setEffect(null);
            }
        });

        equipe2.setOnMouseClicked(mouseEvent -> {
            Plateau.incorporeEquipe(Plateau.getE2());
            equipe2.setEffect(new Effets().putInnerShadow(Plateau.getE2().getCouleur()));
            equipe1.setEffect(null);
            for (Personne p : listClasse) {
                if (personneSelectionne == p) {
                    p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                } else {
                    p.getImageperson().setEffect(null);
                }
            }
        });

        equipe2.setOnMouseEntered(mouseEvent -> {
            equipe2.setEffect(Bouton.effectE2);
        });

        equipe2.setOnMouseExited(mouseEvent -> {
            if (equipe2.getEffect() == Bouton.effectE2 && equipeSelectionne != Plateau.getE2()) {
                equipe2.setEffect(null);
            }
        });

        Group groupEquipeButton = new Group();

        if (!statusPartie) {
            groupEquipeButton.getChildren().add(equipe1);
            groupEquipeButton.getChildren().add(equipe2);
        } else {
            equipe1.setVisible(true);
            equipe2.setVisible(true);
        }

        return groupEquipeButton;
    }

    public void afficherArgentEquipes() {
        Text argentEquipe1 = new Text(getE1().getArgent() + " €");
        argentEquipe1.setX(10);
        argentEquipe1.setY(760);
        argentEquipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        Text argentEquipe2 = new Text(getE2().getArgent() + " €");
        argentEquipe2.setX(120);
        argentEquipe2.setY(argentEquipe1.getY());
        argentEquipe2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        argentGroup.getChildren().add(argentEquipe1);
        argentGroup.getChildren().add(argentEquipe2);
    }

    public void updateArgentEquipes() {
        if (argentPartie > 0) {
            argentGroup.getChildren().clear();
            afficherArgentEquipes();
        }
    }

    public Group returnBarre() {
        return group;
    }

    public Group getArgentGroup() {
        return argentGroup;
    }

    public ArrayList<Personne> getListClasse() {
        return listClasse;
    }
}
