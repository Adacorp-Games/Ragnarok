package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationHeal;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Pretre;
import outerhaven.Entites.Personnages.Personne;

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
    public void pouvoir() {
        this.soigner(500);

        // Soigne les cases voisines
        ajouterAlter(new AlterationHeal(75, 1, this.getTeam()), this.getPosition());
        for (Case c1 : this.getPosition().getCaseVoisines()) {
            ajouterAlter(new AlterationHeal(75, 1, this.getTeam()), c1);
        }

        // Utilisation du mana
        soignerAOE();
    }

    @Override
    public void soignerAOE() {
        if (this.getMana() >= 200) {
            for (Case c1 : this.getPosition().getCaseVoisines()) {
                ajouterAlter(new AlterationHeal(150, 1, this.getTeam()), c1);
            }
            this.setMana(0);
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