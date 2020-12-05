package outerhaven.Interface;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import outerhaven.Mecaniques.Enchere;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Mage;
import outerhaven.Personnages.PersonnagesEnergetiques.*;
import outerhaven.Personnages.PersonnagesMagiques.*;
import outerhaven.Personnages.PersonnagesPrime.AlchimistePrime;
import outerhaven.Personnages.PersonnagesPrime.NecromancienPrime;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import static outerhaven.Plateau.*;

import java.util.ArrayList;

public class BarrePersonnage {

    private static final Group groupBarre = new Group();
    public double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    public double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    public static ArrayList<Personne> listeClasse = new ArrayList<>();
    public static final ArrayList<Personne> listeEquipe1 = new ArrayList<>();
    public static final ArrayList<Personne> listeEquipe2 = new ArrayList<>();
    private final Group argentGroup = new Group();

    public BarrePersonnage() {
        // Ajoutez les nouvelles Classe personnages ici                           <-------------------------------------------
        listeClasse.add(new Guerrier());
        listeClasse.add(new Archer());
        listeClasse.add(new Mage());
        listeClasse.add(new Paladin());
        listeClasse.add(new Necromancien());
        listeClasse.add(new Alchimiste());
        listeClasse.add(new Archimage());
        listeClasse.add(new Pretre());
        listeClasse.add(new Samourai());
        listeEquipe1.addAll(listeClasse);
        listeEquipe2.addAll(listeClasse);
        //listeClasse.add(new NecromancienPrime());
        /*for (int i = 0; i < listeClasse.size(); i++) {
            personnages.remove(0);
        }*/
        personnages.clear();
        interfaceBarre();
    }

    private void genererBarre(ArrayList<Personne> list) {
        Rectangle barre = new Rectangle();
        barre.setWidth(longeurMax - 20);
        barre.setHeight(200);
        barre.setX(10);
        barre.setY(largeurMax - barre.getHeight());
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);
        barre.setFill(Color.LIGHTGRAY);
        groupBarre.getChildren().add(barre);
        groupBarre.getChildren().add(boutonEquipe());
        for (int i = 0; i < list.size(); i++) {
            groupBarre.getChildren().add(list.get(i).affichagePersonnageBarre(i));
        }
    }

    private void interfaceBarre() {
        if (!activerEnchere) {
            genererBarre(listeClasse);
        } else {
            majBarreEnchere();
        }
    }

    private void majBarreEnchere() {
        groupBarre.getChildren().clear();
        if (equipeSelectionne == Plateau.getE1()) {
            genererBarre(listeEquipe1);
        } else {
            genererBarre(listeEquipe2);
        }
    }

    public void ajouterClass(Personne personne) {
        if (personne.getTeam() == Plateau.getE1()){
            listeEquipe1.add(personne);
        } else {
            listeEquipe2.add(personne);
        }
        majBarreEnchere();
    }

    /* Cette fonction est présente dans cette classe car cela nous permet de faire disparaitre en meme temps que la
    barre de personnage les boutons d'équipes lorsque l'on lance la partie */
    public Group boutonEquipe() {
        Group groupEquipeButton = new Group();

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

        if (!enchereTerminee) {
            equipeSelectionne = Plateau.getE1();
            equipe1.setEffect(new Effets().putInnerShadow(Plateau.getE1().getCouleur()));
        }

        /*if (isActiverEnchere() && personnages.size() != 0) {
            TextField encherirFieldE1 = new TextField();
            encherirFieldE1.setLayoutX(equipe1.getLayoutX());
            encherirFieldE1.setLayoutY(equipe1.getLayoutY() - 130);
            encherirFieldE1.setMinSize(100,50);
            encherirFieldE1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
            encherirFieldE1.setOnKeyReleased(key -> {
                if (key.getCode() == KeyCode.ENTER) {
                    Plateau.getE1().augmenterEnchere(getIntFromTextField(encherirFieldE1), Enchere.getListeEnchere().get(idEnchere.get()));
                }
            });

            TextField encherirFieldE2 = new TextField();
            encherirFieldE2.setLayoutX(equipe2.getLayoutX());
            encherirFieldE2.setLayoutY(equipe2.getLayoutY() - 130);
            encherirFieldE2.setMinSize(100,50);
            encherirFieldE2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
            encherirFieldE2.setOnKeyReleased(key -> {
                if (key.getCode() == KeyCode.ENTER) {
                    Plateau.getE2().augmenterEnchere(getIntFromTextField(encherirFieldE2), Enchere.getListeEnchere().get(idEnchere.get()));
                }
            });

            groupEquipeButton.getChildren().add(encherirFieldE1);
            groupEquipeButton.getChildren().add(encherirFieldE2);
        }*/

        // Actions sur les boutons d'équipes
        equipe1.setOnMouseClicked(mouseEvent -> {
            Plateau.incorporeEquipe(Plateau.getE1());
            equipe1.setEffect(Bouton.effectE1);
            equipe2.setEffect(null);
            for (Personne p : listeClasse) {
                if (personneSelectionne == p) {
                    p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                } else {
                    p.getImageperson().setEffect(null);
                }
            }
            if (activerEnchere) {
                if (enchereTerminee) {
                    majBarreEnchere();
                }
                Plateau.brouillard();
                personneSelectionne = null;
                for (Personne p : BarrePersonnage.listeEquipe1) {
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
            for (Personne p : listeClasse) {
                if (personneSelectionne == p) {
                    p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                } else {
                    p.getImageperson().setEffect(null);
                }
            }
            if (activerEnchere) {
                if (enchereTerminee) {
                    majBarreEnchere();
                }
                Plateau.brouillard();
                personneSelectionne = null;
                for (Personne p : BarrePersonnage.listeEquipe2) {
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
        return groupBarre;
    }

    public Group getArgentGroup() {
        return argentGroup;
    }

    public ArrayList<Personne> getListeClasse() {
        return listeClasse;
    }
}
