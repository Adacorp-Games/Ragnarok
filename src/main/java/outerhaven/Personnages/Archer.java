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
    public Text getinfoText(){
        return new Text("L'Archer il est eclaté(e) : \n" + this.getHealth() +" : Pv\n" + this.getArmor()+ " : Armure\n");
    }


    public Archer(Equipe team, Case position) {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1, team, position);
        this.getPosition().setStatus(true);
    }
}
