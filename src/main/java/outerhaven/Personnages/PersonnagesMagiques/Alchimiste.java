package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Alteration;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Alchimiste extends PersonneMagique{

    public Alchimiste() {
        super(2000, 50, 400, 150, 4, 1, 100);
    }

    public Alchimiste(Equipe team, Case position) {
        super(2000, 50, 400, 150, 4, 1, team, position, 100);
    }

    public void action() {
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        if (this.getPosition().nbVoisinsLibres() == 0) {
            System.out.println(this.getName() + " patiente");
        } else {
            if (getPosition().pathToPerso(getOtherTeam()).size() == 0) {
                System.out.println(this.getName() + " patiente");
            } else {
                ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
                System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

                // Capacités de l'alchimiste
                if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() >= 100) {
                    pathToEnnemy.get(pathToEnnemy.size() - 1).setAlteration(new Alteration("poison", 50, 5));
                    for (Case c : pathToEnnemy.get(pathToEnnemy.size() - 1).getCaseVoisines()) {
                        c.setAlteration(new Alteration("poison", 50, 5));
                    }
                    pathToEnnemy.get(pathToEnnemy.size() - 1).setAlteration(new Alteration("poison", 50, 5));
                    this.setMana(this.getMana() - 125);

                } else if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() < 100) {
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

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Alchimiste(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Alchimiste (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Alchimiste.class.getResourceAsStream("/Images/Personnes/Alchemist.png"));
    }
}
