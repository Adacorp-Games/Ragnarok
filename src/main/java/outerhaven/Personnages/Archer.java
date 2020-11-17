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
        carquoisEstVide = false;
        carquois = 15;
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
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Archer(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archer (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nPortée augmentée mais vie, armure et dégâts plus bas.\nNombre de flèches limité à 15 :\nSe bat ensuite au corps-à-corps et fait plus de dégâts." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Archer.class.getResourceAsStream("/Images/Personnes/Archer.png"));
    }
}