package outerhaven.Personnages;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;

public class Guerrier extends Personne {

    public Guerrier() {
        super(200, 100, 100, 75, 1, 1);
    }

    public Guerrier(Equipe team, Case position) {
        super(200, 100, 100, 75, 1, 1, team, position);
        this.getPosition().setContenu(this);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Guerrier(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Guerrier (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats réduits mais vie et armure plus grande." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    public Image getImageFace() {
        return new Image(Guerrier.class.getResourceAsStream("/Images/Warrior.png"));
        //return new Image("https://images108.fotki.com/v156/photos/6/830356/8792277/joyDancingstickmantransparent-vi.gif");
    }
}