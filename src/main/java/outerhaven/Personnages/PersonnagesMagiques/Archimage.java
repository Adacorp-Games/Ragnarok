package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Alteration;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import java.util.ArrayList;

public class Archimage extends PersonneMagique {
    public Archimage() {
        super(2000, 0, 500, 300, 8, 1, 100);
    }

    public Archimage(Equipe team, Case position) {
        super(2000, 0, 500, 300, 8, 1, team, position, 100);
    }

    public void action() {
        this.gainMana();
        boolean danger = false;

        // Vérification si les cases voisines contiennent au moins un ennemi
        for (Case c : this.getPosition().voisinsLibres(false)) {
            // Si la case voisine n'est pas vide
            if (c.getContenu().size() != 0) {
                // Si le contenu de la case n'est pas un allié
                if (c.getContenu().get(0).getTeam() != this.getTeam()) {
                    danger = true;
                }
            }
        }

        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        if (this.getPosition().nbVoisinsLibres() == 0) {
            System.out.println(this.getName() + " patiente");
        } else {
            if (getPosition().pathToPerso(getOtherTeam()).size() == 0) {
                System.out.println(this.getName() + " patiente");

            } else {
                ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
                System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

                // Téléportation si cible trop proche (danger détecté)
                if (danger && this.getPosition().nbVoisinsLibres() > 0 && this.getMana() >= 50) {

                    // Freeze les cases autour de lui et les personnes qui y sont ou y rentrent
                    ajouterAlter(this.getPosition());
                    for (Case c : this.getPosition().getCaseVoisines()) {
                        ajouterAlter(c);
                    }

                    // Cherche un voisin libre et un voisin libre du voisin libre si possible
                    Case voisinLibre = this.getPosition().getRandomVoisinLibre().get(0);
                    if (voisinLibre.nbVoisinsLibres() > 0) {
                    /*if (voisinLibre.getRandomVoisinLibre().get(0).nbVoisinsLibres() > 0) {
                        déplacer(this.getPosition().getRandomVoisinLibre().get(0).getRandomVoisinLibre().get(0));
                    } else {*/
                        déplacer(voisinLibre);
                        //}
                    }
                    this.setMana(this.getMana() - 50);

                } else if (pathToEnnemy.size() - 1 <= this.getRange()) {
                    System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
                    attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
                } else {
                    System.out.println(this.getName() + " se déplace");
                    déplacer(pathToEnnemy.get(this.getSpeed()));
                }
                // System.out.println("Vie restante de la cible " + getHealth());
            }
        }
    }

    public void ajouterAlter(Case c) {
        if (c.getAlteration() == null) {
            c.setAlteration(new Alteration("freeze", 50, 10));
            Plateau.listeCaseAlterees.add(c);
        } else {
            c.getAlteration().setDuree(10);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Archimage(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archimage (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nAttaque augmentées, vie et armure plus élevées mais portée réduite. Peut se soigner." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Archimage.class.getResourceAsStream("/Images/Personnes/Archimage.png"));
    }
}
