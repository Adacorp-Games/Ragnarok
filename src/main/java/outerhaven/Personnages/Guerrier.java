package outerhaven.Personnages;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;

public class Guerrier extends Personne {


    public Guerrier(){
        super(200, 100, 100, 75, 1, 1);
    }

    public Guerrier(Equipe team) {
        super(200, 100, 100, 75, 1, 1, team);
    }

    public Guerrier(Equipe team, Case position) {
        super(200, 100, 100, 75, 1, 1, team, position);
        this.getPosition().setStatus(true);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Guerrier(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Guerrier (" + this.getCost() + "€) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats réduits mais vie et armure plus grande." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    public Image getImageFace(){
        return  new Image("https://media.discordapp.net/attachments/764528562429624391/770287874805071872/fight-151796_1280.png?width=475&height=703");
    }

}
