package outerhaven.Entites.Personnages.Invocations;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class Golem extends Invocation {

    public Golem() {
        super(4000, 1000, 400, 200, 1, 1);
    }

    public Golem(Equipe team, Case position) {
        super(4000, 1000, 400, 200, 1, 1, team, position);
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new Golem(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Golem (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nGolem à la vie, armure et aux dégâts très élevés." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Golem.class.getResourceAsStream("/Images/Personnes/Golem.png"));
    }
}
