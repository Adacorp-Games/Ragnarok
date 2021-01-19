package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationPoison;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public class AlchimistePrime extends Alchimiste {

    public AlchimistePrime() {
        this.augmenterStats(2);
    }

    public AlchimistePrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void action() {
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

        // Capacités de l'alchimiste
        if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() >= 100) {
            ajouterAlter(new AlterationPoison(60, 15, this.getTeam()), pathToEnnemy.get(pathToEnnemy.size() - 1));
            ajouterAlterVoisine(new AlterationPoison(60, 15, this.getTeam()), pathToEnnemy.get(pathToEnnemy.size() - 1));
            for (Case c : pathToEnnemy.get(pathToEnnemy.size() - 1).getCaseVoisines()) {
                ajouterAlterVoisine(new AlterationPoison(60, 15, this.getTeam()), pathToEnnemy.get(pathToEnnemy.size() - 1));
            }
            this.setMana(this.getMana() - 100);

        } else if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() < 100) {
            System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
            attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
        } else {
            System.out.println(this.getName() + " se déplace");
            deplacer(pathToEnnemy.get(this.getSpeed()));
        }
        // System.out.println("Vie restante de la cible " + getHealth());
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new AlchimistePrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("AlchimistePrime (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nUnité pouvant altérer des cases." + "\n" +
                "Gagne 25 de mana par tour." + "\n" +
                "Peut dépenser 100 de mana pour mettre du poison dans la case sa\ncible et celles voisines.\nLes personnages (hors Alchimistes) marchant dessus subiront des dégâts." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(AlchimistePrime.class.getResourceAsStream("/Images/Personnes/AlchemistPrime.png"));
    }
}
