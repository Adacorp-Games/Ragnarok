package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Paladin;
import outerhaven.Personnages.PersonnagesMagiques.PersonneMagique;
import outerhaven.Personnages.Personne;

public class PaladinPrime extends Paladin {
    private static int multiplier = 2;

    public PaladinPrime() {
        //   vie    armor cost degat rnge speed  magie)
        super(3000*multiplier, 250*multiplier, 300*multiplier, 300*multiplier, 1, 1, 100*multiplier);
    }

    public PaladinPrime(Equipe team, Case position) {
        super(3000*multiplier, 250*multiplier, 300*multiplier, 300*multiplier, 1, 1, 100*multiplier);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new PaladinPrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("PaladinPrime (" + this.getCost() + " €) :\n");
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
        return new Image(Paladin.class.getResourceAsStream("/Images/Personnes/PaladinPrime.png"));
    }
}
