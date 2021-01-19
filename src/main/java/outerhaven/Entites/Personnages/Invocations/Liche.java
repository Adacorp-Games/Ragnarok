package outerhaven.Entites.Personnages.Invocations;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class Liche extends Invocation {

    public Liche() {
        super(600, 0, 140, 200, 3, 1);
    }

    public Liche(Equipe team, Case position) {
        super(600, 0, 140, 200, 3, 1, team, position);
    }

    @Override
    public void attaquer(Personne p) {
        double damageMultiplier = this.getDamage() / (this.getDamage() + this.getArmor() / 5);
        double totalDamage = this.getDamage() * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            p.prendreDégâts(totalDamage);

            // Vol en vie à la cible le montant de dégâts infligés
            if (this.getHealth() < this.getMaxHealth() - totalDamage) {
                this.soigner(totalDamage);
            }

            if (p.getHealth() <= 0) {
                System.out.println(p.getName() + " est mort !");
            }
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Liche(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Lich (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nLich à la vie, armure et dégâts faibles mais avec de la porter." + "\n" + "PV : " + this.getHealth() + "\n" + "Armure : " + this.getArmor() + "\n" + "Dégats : " + this.getDamage() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Liche.class.getResourceAsStream("/Images/Personnes/Lich.png"));
    }
}
