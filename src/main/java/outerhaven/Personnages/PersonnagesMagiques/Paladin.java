package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Paladin extends PersonneMagique {

    public Paladin() {
        //   vie    armor cost degat rnge speed  magie)
        super(3000, 250, 300, 300,    1,   1,   100);
        // super(3000, 1000, 300, 300,    1,   1,   100);
    }

    public Paladin(Equipe team, Case position) {
        super(3000, 250, 300, 300, 1, 1, team, position, 100);
    }

    public void action() {
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());

        // Se soigne si il possède de la mana
        if (this.getMana() > 150 && this.getHealth() <= this.getMaxHealth()/4) {
            this.soigner(getMaxHealth()/4);
            this.setMana(this.getMana() - 150);

        } else if (this.getMana() > 150 && this.getHealth() > this.getMaxHealth()/4) {
            this.setHealth(getMaxHealth());
            this.setMana(this.getMana() - 150);

        } else {
            ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
            System.out.println("Taille du chemin vers l'ennemi le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));
            if (pathToEnnemy.size() - 1 <= this.getRange()) {
                System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
                attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
            } else {
                System.out.println(this.getName() + " se déplace");
                deplacer(pathToEnnemy.get(this.getSpeed()));
            }
            // System.out.println("Vie restante de la cible " + getHealth());
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Paladin(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Paladin (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nAttaque augmentées, vie et armure plus élevées mais portée réduite.\nGagne 25 de mana par tour.\nPeut se soigner 1 quart de sa vie pour 50 de mana." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Paladin.class.getResourceAsStream("/Images/Personnes/Paladin.png"));
    }
}
