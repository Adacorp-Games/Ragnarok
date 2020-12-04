package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class AlchimistePrime extends PersonnagePrime {

    public AlchimistePrime() {
        super(1000 * getPrimeMultiplier(), 80 * getPrimeMultiplier(), 650 * getPrimeMultiplier(), 150 * getPrimeMultiplier(), 4 * getPrimeMultiplier(), 1, 100 * getPrimeMultiplier());
    }

    public AlchimistePrime(Equipe team, Case position) {
        super(1000 * getPrimeMultiplier(), 80 * getPrimeMultiplier(), 650 * getPrimeMultiplier(), 150 * getPrimeMultiplier(), 4 * getPrimeMultiplier(), 1, team, position, 150 * getPrimeMultiplier());
    }

    public void action() {
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

        // Capacités de l'alchimiste
        if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() >= 100) {
            ajouterAlter("poison", 60, 15, pathToEnnemy.get(pathToEnnemy.size() - 1), this.getTeam());
            ajouterAlterVoisine("poison", 60, 15, pathToEnnemy.get(pathToEnnemy.size() - 1), this.getTeam());
            for (Case c : pathToEnnemy.get(pathToEnnemy.size() - 1).getCaseVoisines()) {
                ajouterAlterVoisine("poison", 60, 15, c, this.getTeam());
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
    public Personne personneNouvelle(Equipe team,Case position) {
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
