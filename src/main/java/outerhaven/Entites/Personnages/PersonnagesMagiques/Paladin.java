package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class Paladin extends PersonneMagique {

    public Paladin(double health, double armor, double cost, int damage, int range, int speed, double mana) {
        super(health, armor, cost, damage, range, speed, mana);
    }

    public Paladin(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position, double mana) {
        super(health, armor, cost, damage, range, speed, team, position, mana);
    }

    public Paladin() {
        super(3000, 250, 300, 300, 1, 1, 100);
    }

    public Paladin(Equipe team, Case position) {
        super(3000, 250, 300, 300, 1, 1, team, position, 100);
    }

    @Override
    public void pouvoir() {
        if (this.getMana() > 150 && this.getHealth() <= this.getMaxHealth()/4) {
            this.soigner(getMaxHealth()/4);
            this.setMana(this.getMana() - 150);

        } else if (this.getMana() > 150 && this.getHealth() > this.getMaxHealth() - this.getMaxHealth()/4) {
            this.setHealth(getMaxHealth());
            this.setMana(this.getMana() - 150);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Paladin(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Paladin (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nAttaque augmentées, vie et armure plus élevées mais portée réduite.\nGagne 25 de mana par tour.\nPeut se soigner 1 quart de sa vie pour 50 de mana." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Paladin.class.getResourceAsStream("/Images/Personnes/Paladin.png"));
    }
}
