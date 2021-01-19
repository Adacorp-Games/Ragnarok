package outerhaven.Entites.Personnages;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;

public class Mage extends Personne {

    public Mage() {
        //  vie armor cost degat range speed)
        super(300, 40, 250, 325, 8, 1);
    }

    public Mage(Equipe team, Case position) {
        super(300, 40, 250, 325, 8, 1, team, position);
        this.getPosition().setContenu(this);
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Mage(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Mage (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Mage.class.getResourceAsStream("/Images/Personnes/Mage.png"));
        //return new Image("https://cdn.discordapp.com/attachments/513075072156827651/774392108248989706/tumblr_mepk78hspV1rgpyeqo1_400.gif");
    }
}