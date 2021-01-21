package outerhaven.Entites.Personnages;

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
        this.carquoisEstVide = false;
        this.carquois = 15;
    }

    public Archer(Equipe team, Case position) {
        super(600, 60, 150, 108, 15, 1, team, position);
        this.getPosition().setContenu(this);
        this.carquoisEstVide = false;
        this.carquois = 15;
    }

    @Override
    public void attaquer(Personne p) {
        if (!this.carquoisEstVide) {
            super.attaquer(p);
            this.carquois--;
            if (this.carquois == 0) {
                this.carquoisEstVide = true;
                this.damage = 130;
                this.range = 1;
            }
        } else {
            super.attaquer(p);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new Archer(team, position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archer (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée augmentée mais vie, armure et dégâts plus basses.\n" +
                "Se bat au corps-à-corps et fait plus de dégâts une fois les flèches dans \n" +
                "le carquois épuisées." + "\n" +
                "- Limite de flèches : " + this.getCarquois() + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Archer.class.getResourceAsStream("/Images/Personnes/Archer.png"));
    }

    public int getCarquois() {
        return carquois;
    }
}