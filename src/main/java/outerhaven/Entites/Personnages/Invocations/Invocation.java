package outerhaven.Entites.Personnages.Invocations;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

public abstract class Invocation extends Personne {
    public Invocation(double health, double armor, double cost, int damage, int range, int speed) {
        super(health, armor, cost, damage, range, speed);
        Personne.invocationAttente.add(this);
    }

    public Invocation(double health, double armor, double cost, int damage, int range, int speed, Equipe team) {
        super(health, armor, cost, damage, range, speed, team);
        Personne.invocationAttente.add(this);
        this.getTeam().getTeam().add(this);
    }

    public Invocation(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position) {
        super(health, armor, cost, damage, range, speed, team, position);
        Personne.invocationAttente.add(this);
        this.getTeam().getTeam().add(this);
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return null;
    }

    @Override
    public Text getinfoTitleText() {
        return null;
    }

    @Override
    public Text getinfoDescText() {
        return null;
    }

    @Override
    public Image getImageFace() {
        return null;
    }
}
