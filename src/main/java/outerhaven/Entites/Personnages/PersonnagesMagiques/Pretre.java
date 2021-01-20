package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationHeal;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public class Pretre extends PersonneMagique {

    public Pretre() {
        super(700, 50, 350, 110, 2, 1, 100);
    }

    public Pretre(Equipe team, Case position) {
        super(700, 50, 350, 100, 2, 1, team, position, 100);
    }

    @Override
    public void action() {
        this.gainMana(); // this gagne du mana si this peut effectuer une action.
        this.genererChemin(); // Créer le chemin vers l'objectif de this (attaquer, protéger un bâtiment ...).
        this.comportementsBasiques(); // Se déplace ou attaque.
        this.pouvoir(); // Utilise le pouvoir de this.
    }

    @Override
    public void pouvoir() {
        this.soigner(300);

        // Soigne les cases voisines
        ajouterAlter(new AlterationHeal(60, 1, this.getTeam()), this.getPosition());
        for (Case c1 : this.getPosition().getCaseVoisines()) {
            ajouterAlter(new AlterationHeal(60, 1, this.getTeam()), c1);
        }

        // Utilisation du mana
        soignerAOE();
    }

    public void soignerAOE() {
        if (this.getMana() >= 200) {
            for (Case c1 : this.getPosition().getCaseVoisines()) {
                ajouterAlter(new AlterationHeal(120, 1, this.getTeam()), c1);
            }
            this.setMana(0);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Pretre(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Prêtre (" + this.getCost() + " €) :\n");
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
        return new Image(Pretre.class.getResourceAsStream("/Images/Personnes/Priest.png"));
    }
}
