package outerhaven.Personnages;

import javafx.scene.image.Image;
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
        super(150, 50, 150, 100, 2, 1, team, position);
        this.getPosition().setContenu(this);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Archer(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archer (" + this.getCost() + "€) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }
    public Image getImageFace() {
        return new Image("https://cdn.discordapp.com/attachments/653027161862832128/772186706967396352/giphy.gif");
    }
}