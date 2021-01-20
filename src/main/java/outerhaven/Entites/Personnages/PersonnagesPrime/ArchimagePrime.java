package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationFreeze;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public class ArchimagePrime extends Archimage {

    public ArchimagePrime() {
        this.augmenterStats(2);
    }

    public ArchimagePrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void gelAOE(Case c) {
        ajouterAlter(new AlterationFreeze(75, 15, this.getTeam()), c);
        for (Case cv : c.getCaseVoisines()) {
            ajouterAlter(new AlterationFreeze(75, 15, this.getTeam()), cv);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new ArchimagePrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("ArchimagePrime (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nAttaque puissante et grande portée mais vie et armure plus basses.\nPeut altérer les cases.\nGagne 25 de mana par tour.\nPeut se téléporter dans une case voisine en cas d'ennemi dans une case\nvoisine. La case sur laquelle il se téléporte et ses cases voisines\nsont gelées et les personne allant dedans ne peuvent rien faire." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégats : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(ArchimagePrime.class.getResourceAsStream("/Images/Personnes/ArchimagePrime.png"));
    }
}
