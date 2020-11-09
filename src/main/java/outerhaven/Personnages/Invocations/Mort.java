package outerhaven.Personnages.Invocations;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

public class Mort extends Personne {

    public Mort() {
        super(500, 0, 50, 100, 1, 1);
        Plateau.invocationAttente.add(this);
    }

    public Mort(Equipe team, Case position) {
        super(500, 0, 50, 100, 1, 1, team, position);
        //this.getPosition().setContenu(this);
        Plateau.invocationAttente.add(this);
        this.getTeam().getTeam().add(this);
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
        return new Image(Guerrier.class.getResourceAsStream("/Images/Dead.png"));
    }
}
