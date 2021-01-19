package outerhaven.Entites.Personnages.Invocations;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class Mort extends Invocation {

    public Mort() {
        super(300, 0, 70, 100, 1, 1);
    }

    public Mort(Equipe team, Case position) {
        super(300, 0, 70, 100, 1, 1, team, position);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Mort(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Mort (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nSkelette à la vie, armure et dégâts faibles." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Mort.class.getResourceAsStream("/Images/Personnes/Dead.png"));
    }
}
