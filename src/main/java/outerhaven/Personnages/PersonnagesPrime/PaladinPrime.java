package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Paladin;
import outerhaven.Personnages.Personne;

public class PaladinPrime extends Paladin {
    private static int primeMultiplier = 2;

    public PaladinPrime() {
        //   vie    armor cost degat rnge speed  magie)
        super(3000 * primeMultiplier, 250 * primeMultiplier, 300 * primeMultiplier, 300 * primeMultiplier, 1, 1, 100 * primeMultiplier);
    }

    public PaladinPrime(Equipe team, Case position) {
        super(3000 * primeMultiplier, 250 * primeMultiplier, 300 * primeMultiplier, 300 * primeMultiplier, 1, 1, team, position, 100 * primeMultiplier);
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

    /**
     * Se soigne et devient plus résistant plus il reste sur le plateau
     */
    @Override
    public void soigner(double vie) {
        if (this.getHealth() <= this.getMaxHealth() - vie - 500) {
            this.setHealth(this.getHealth() + vie + 500);
        } else if (this.getHealth() > this.getMaxHealth() - vie - 500) {
            this.setHealth(this.getMaxHealth());
        }
        seRenforce(25);
    }
}
