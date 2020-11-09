package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

public abstract class PersonneMagique extends Personne {
    private int mana;
    private int manaMax = 200;

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, int mana) {
        super(health, armor, cost, damage, range, speed);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, Equipe team, int mana) {
        super(health, armor, cost, damage, range, speed, team);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position, int mana) {
        super(health, armor, cost, damage, range, speed, team, position);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    public abstract Personne personneNouvelle(Equipe team, Case position);
    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();

    // Getter et setter

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getManaMax() {
        return manaMax;
    }

    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }

    public void gainMana() {
        if (this.getMana() < this.getManaMax() - 25) {
            this.setMana(this.getMana() + 25);
        } else if (this.getMana() < this.getManaMax()) {
            this.setMana(this.getManaMax());
        }
    }
}
