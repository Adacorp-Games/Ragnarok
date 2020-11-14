package outerhaven.Personnages;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;

public class Archer extends Personne {

    private int carquois;
    private boolean carquoisEstVide;
    public Archer() {
        //  vie armor cost degat range speed)
        super(600, 60, 150, 108, 15, 1);
        carquois = 15;
        carquoisEstVide = false;
    }

    public Archer(Equipe team, Case position) {
        super(600, 60, 150, 108, 15, 1, team, position);
        this.getPosition().setContenu(this);
        carquoisEstVide = false;
        carquois = 15;
    }

    @Override
    public void attaquer(Personne p) {
        if (!carquoisEstVide) {
            super.attaquer(p);
            carquois--;
            if (carquois==0) {
                carquoisEstVide = true;
                damage = 130;
                range = 1;
            }
        }else{
        super.attaquer(p);
        }
        
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Archer(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archer (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée et dégats augmentés mais vie et armure plus basses." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Archer.class.getResourceAsStream("/Images/Personnes/Archer.png"));
        //return new Image("https://cdn.discordapp.com/attachments/653027161862832128/772186706967396352/giphy.gif");
    }
}