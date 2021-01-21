package outerhaven.Interface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import outerhaven.Entites.Personnages.Archer;
import outerhaven.Entites.Personnages.Guerrier;
import outerhaven.Entites.Personnages.Mage;
import outerhaven.Entites.Personnages.PersonnagesEnergetiques.Samourai;
import outerhaven.Entites.Personnages.PersonnagesMagiques.*;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Sauvegarde;
import outerhaven.Plateau;

import java.util.ArrayList;

import static outerhaven.Plateau.*;

/**
 * Classe dans laquelle la majorité de l'interface est crée. La barre personnage en elle même est la partie
 * qui possède les unités achetables pour la batailles ou gagnées lors des enchères.
 */

public class BarrePersonnage {

    private static final Group groupBarre = new Group();
    public static ArrayList<Personne> listeClasse = new ArrayList<>();
    public static ArrayList<Personne> listeEquipe1 = new ArrayList<>();
    public static ArrayList<Personne> listeEquipe2 = new ArrayList<>();
    private static Sauvegarde save;
    private final Group argentGroup = new Group();
    public double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    public double longueurMax = Screen.getPrimary().getVisualBounds().getWidth();
    public Button equipe1 = new Button("Equipe 1");
    public Button equipe2 = new Button("Equipe 2");

    public BarrePersonnage() {
        // Ajoutez les nouvelles Classe personnages ici <-------------------------------------------
        if (listeClasse.isEmpty()) {
            listeClasse.add(new Guerrier());
            listeClasse.add(new Archer());
            listeClasse.add(new Mage());
            listeClasse.add(new Paladin());
            listeClasse.add(new Necromancien());
            listeClasse.add(new Alchimiste());
            listeClasse.add(new Archimage());
            listeClasse.add(new Pretre());
            listeClasse.add(new Samourai());
            listeClasse.add(new Chaman());

            // Configuration des listes de personnages pour chaque équipe
            listeEquipe1.addAll(listeClasse);
            listeEquipe2.addAll(listeClasse);
            Personne.personnages.clear();
        }
        interfaceBarre();
    }

    public static ArrayList<Personne> listeEquipe() {
        if (equipeSelectionne == Equipe.e1) {
            return listeEquipe1;
        } else {
            return listeEquipe2;
        }
    }

    public static Sauvegarde getSave() {
        return save;
    }

    public static void setSave(Sauvegarde save) {
        BarrePersonnage.save = save;
    }

    public void reset() {
        groupBarre.getChildren().clear();
        listeEquipe1.clear();
        listeEquipe1.addAll(listeClasse);
        listeEquipe2.clear();
        listeEquipe2.addAll(listeClasse);
    }

    private void genererBarre(ArrayList<Personne> list) {
        Rectangle barre = new Rectangle();
        barre.setWidth(longueurMax - 20);
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

    public void interfaceBarre() {
        if (!activerEnchere) {
            genererBarre(listeClasse);
        } else {
            majBarreEnchere();
        }
    }

    public void majBarreEnchere() {
        groupBarre.getChildren().clear();
        if (equipeSelectionne == Plateau.getE1()) {
            genererBarre(listeEquipe1);
        } else {
            genererBarre(listeEquipe2);
        }
    }

    public void ajouterClass(Personne personne) {
        if (personne.getTeam() == Plateau.getE1()) {
            listeEquipe1.add(personne);
        } else {
            listeEquipe2.add(personne);
        }
        majBarreEnchere();
    }

    /**
     * Cette fonction est présente dans cette classe car cela nous permet de faire disparaitre en meme temps que
     * la barre de personnage les boutons d'équipes lorsque l'on lance la partie
     */
    public Group boutonEquipe() {
        Group groupEquipeButton = new Group();

        equipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        equipe1.setLayoutX(10);
        equipe1.setLayoutY(780);
        equipe1.setMinSize(120, 50);

        equipe2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        equipe2.setLayoutX(140);
        equipe2.setLayoutY(780);
        equipe2.setMinSize(120, 50);

        if (equipeSelectionne == Equipe.e1) {
            equipe1.setEffect(Bouton.effectE1);
        }

        // Actions sur les boutons d'équipes

        equipe1.setOnMouseClicked(mouseEvent -> {
            if ((activerEnchere && !enchereTerminee && equipeSelectionne == null)
                    || (!activerEnchere)
                    || (enchereTerminee)) {
                Plateau.incorporeEquipe(Plateau.getE1());
                equipe1.setEffect(Bouton.effectE1);
                equipe2.setEffect(null);
                lancementAnimation();
            }
        });

        equipe1.setOnMouseEntered(mouseEvent -> {
            if ((activerEnchere && !enchereTerminee && equipeSelectionne == null)
                    || (!activerEnchere)
                    || (enchereTerminee)) {
                equipe1.setEffect(Bouton.effectE1);
            }
        });

        equipe1.setOnMouseExited(mouseEvent -> {
            if (equipe1.getEffect() == Bouton.effectE1 && equipeSelectionne != Plateau.getE1()) {
                equipe1.setEffect(null);
            }
        });

        equipe2.setOnMouseClicked(mouseEvent -> {
            if ((activerEnchere && !enchereTerminee && equipeSelectionne == null)
                    || (!activerEnchere)
                    || (enchereTerminee)) {
                Plateau.incorporeEquipe(Plateau.getE2());
                equipe2.setEffect(new Effets().putInnerShadow(Plateau.getE2().getCouleur()));
                equipe1.setEffect(null);
                lancementAnimation();
            }
        });

        equipe2.setOnMouseEntered(mouseEvent -> {
            if ((activerEnchere && !enchereTerminee && equipeSelectionne == null)
                    || (!activerEnchere)
                    || (enchereTerminee)) {
                equipe2.setEffect(Bouton.effectE2);
            }
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

    private void lancementAnimation() {
        if (enchereTerminee) {
            final double[] incr = {0.99};
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), ev -> {
                group.setOpacity(incr[0]);
                incr[0] = incr[0] - 0.01;
            }));
            timeline.setCycleCount(100);
            timeline.setOnFinished(actionEvent -> mecaniqueBouton());
            timeline.play();
        } else {
            mecaniqueBouton();
        }
    }

    private void mecaniqueBouton() {
        for (Personne p : listeClasse) {
            if (personneSelectionne == p) {
                p.getImagePerson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
            } else {
                p.getImagePerson().setEffect(null);
            }
        }
        if (activerEnchere) {
            if (enchereTerminee) {
                majBarreEnchere();
            }
            Plateau.brouillard();
            personneSelectionne = null;
            for (Personne p : BarrePersonnage.listeEquipe()) {
                p.getImagePerson().setEffect(null);
            }
        }
        if (enchereTerminee) {
            final double[] incr = {0.01};
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), ev -> {
                group.setOpacity(incr[0]);
                incr[0] = incr[0] + 0.01;
            }));
            timeline.setCycleCount(100);
            Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(2000), ev -> {
                timeline.play();
            }));
            timeline2.play();
        }
    }

    public void afficherArgentEquipes() {
        Text argentEquipe1 = new Text(getE1().getArgent() + " €");
        argentEquipe1.setX(10);
        argentEquipe1.setY(760);
        argentEquipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold");
        Text argentEquipe2 = new Text(getE2().getArgent() + " €");
        argentEquipe2.setX(140);
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

    public Button getButtonTeamSelect() {
        if (equipeSelectionne == Equipe.e1) {
            return equipe1;
        } else {
            return equipe2;
        }
    }

    public void cleanEffects() {
        if (!activerEnchere) {
            for (Personne p : this.getListeClasse()) {
                p.getImagePerson().setEffect(null);
            }
        } else {
            for (Personne p : listeEquipe1) {
                p.getImagePerson().setEffect(null);
            }
            for (Personne p : listeEquipe2) {
                p.getImagePerson().setEffect(null);
            }
        }
    }
}
