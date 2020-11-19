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
        return new Text("\nManieur de sabres avec de très bons dégâts et une bonne resistance" + "\n" +
                "Cumule de l'énergie à chaque déplacement (10) ou attaque (25)." + "\n" +
                "Peut dépenser 50 points d'énergie pour tripler la prochaine attaque." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Samourai.class.getResourceAsStream("/Images/Personnes/Samourai.png"));
    }
}
