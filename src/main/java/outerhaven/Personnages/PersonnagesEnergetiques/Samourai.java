package outerhaven.Personnages.PersonnagesEnergetiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Necromancien;
import outerhaven.Personnages.PersonnagesMagiques.Pretre;
import outerhaven.Personnages.Personne;

public class Samourai extends PersonneEnergetique {

    public Samourai() {
        super(2000, 50, 500, 100, 2, 1);
    }

    public Samourai(Equipe team, Case position) {
        super(2000, 50, 500, 100, 2, 1, team, position);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Samourai(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Samourai (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Samourai.class.getResourceAsStream("/Images/Personnes/Samourai.png"));
    }
}
