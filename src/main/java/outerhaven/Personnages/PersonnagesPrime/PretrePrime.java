package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Necromancien;
import outerhaven.Personnages.PersonnagesMagiques.Pretre;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class PretrePrime extends Pretre {

    public PretrePrime() {
        this.augmenterStats(2);
    }

    public PretrePrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void action() {
        // Gagne de la mana chaque tour et se soigne
        this.gainMana();
        this.soigner(500);

        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());

        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));
        if (pathToEnnemy.size() - 1 <= this.getRange()) {
            System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
            attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
        } else {
            System.out.println(this.getName() + " se déplace");
            deplacer(pathToEnnemy.get(this.getSpeed()));
        }
        // System.out.println("Vie restante de la cible " + getHealth());

        // Soigne les cases voisines
        ajouterAlter("heal", 70, 1, this.getPosition(), this.getTeam());
        for (Case c1 : this.getPosition().getCaseVoisines()) {
            ajouterAlter("heal", 70, 1, c1, this.getTeam());
        }

        // Utilisation du mana
        soignerAOE();
    }

    @Override
    public void soignerAOE() {
        if (this.getMana() >= 200) {
            for (Case c1 : this.getPosition().getCaseVoisines()) {
                ajouterAlter("heal", 150, 1, c1, this.getTeam());
            }
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new PretrePrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("PrêtrePrime (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nUnité pouvant altérer des cases." + "\n" +
                "Gagne 25 de mana par tour." + "\n" +
                "Soigne les unités alliées dans les cases voisines à la sienne." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(PretrePrime.class.getResourceAsStream("/Images/Personnes/PriestPrime.png"));
    }
}