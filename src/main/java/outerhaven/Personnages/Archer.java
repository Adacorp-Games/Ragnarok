package outerhaven.Personnages;

import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;

public class Archer extends Personne {


    public Archer(){
        super(150, 50, 150, 100, 2, 1);
    }

    public Archer(Equipe team) {
        super(150, 50, 150, 100, 2, 1, team);
    }

    public Archer(Equipe team, Case position) {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1, team, position);
        this.getPosition().setStatus(true);
    }

    @Override
    public Personne personneNouvelle(Equipe team) {
        return new Archer(team);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archer (" + this.getCost() + "€) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }
}
