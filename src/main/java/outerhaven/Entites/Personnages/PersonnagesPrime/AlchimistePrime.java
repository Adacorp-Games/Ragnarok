package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

public class AlchimistePrime extends Alchimiste {

    public AlchimistePrime() {
        this.augmenterStats(2);
    }

    public AlchimistePrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void pouvoir() {
        if (this.getPathToEnemy().size() - 1 <= this.getRange() && this.getMana() >= 100) {
            Case c = this.getPathToEnemy().get(this.getPathToEnemy().size() - 1);
            this.empoisonnerAOE(c, 60, 15);
            for (Case cv : c.getCaseVoisines()) {
                this.empoisonnerAOE(cv, 60, 15);
            }
            this.setMana(this.getMana() - 100);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new AlchimistePrime(team, position);
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
