package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import outerhaven.Mecaniques.Alteration;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Interface.Effets;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import static outerhaven.Plateau.taille;

public abstract class PersonneMagique extends Personne {
    private double mana;
    private double manaMax;

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, double mana) {
        super(health, armor, cost, damage, range, speed);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, Equipe team, double mana) {
        super(health, armor, cost, damage, range, speed, team);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    public PersonneMagique(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position, double mana) {
        super(health, armor, cost, damage, range, speed, team, position);
        this.mana = mana;
        this.manaMax = mana*2;
    }

    /**
     * Fonction permettant à la personne d'altérer de la case c
     */
    public void ajouterAlter(String effet, int puissance, int duree, Case c, Equipe equipe) {
        if (c.getAlteration() == null) {
            c.setAlteration(new Alteration(effet, puissance, duree, equipe));
            c.getHexagone().setEffect(new Effets().putInnerShadow(this.getTeam().getCouleur()));
            Plateau.listeCaseAlterees.add(c);
        } else {
            c.getAlteration().setDuree(duree);
        }
    }

    /**
     * Fonction permettant à la personne d'altérer les cases voisines de la case c
     */
    public void ajouterAlterVoisine(String effet, int puissance, int duree, Case c, Equipe equipe) {
        for (Case cv : c.getCaseVoisines()) {
            ajouterAlter(effet, puissance, duree, cv, equipe);
        }
    }

    public abstract Personne personneNouvelle(Equipe team, Case position);
    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();
    public abstract Image getImageFace();

    // Affichage santé et nom et mana

    public Group afficherSante() {
        Rectangle barre = new Rectangle(taille, taille/5.3, Color.BLACK);
        Rectangle vie = new Rectangle(taille - 4, taille/10 - 4, Color.RED);
        Rectangle mana = new Rectangle(taille - 4, taille/10 - 4, Color.BLUE);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille/2.2);

        vie.setY(barre.getY() + 2);
        vie.setX(barre.getX() + 2);

        mana.setY(vie.getY() + vie.getHeight() + 2);
        mana.setX(barre.getX() + 2);

        double percentageV = (this.getHealth() / this.getMaxHealth());
        double widthV = (percentageV * (taille - 4));
        vie.widthProperty().setValue(widthV);

        double percentageM = (this.getMana() / this.getManaMax());
        double widthM = (percentageM * (taille - 4));
        mana.widthProperty().setValue(widthM);

        Group group = new Group();
        group.getChildren().add(barre);
        group.getChildren().add(vie);
        group.getChildren().add(mana);

        return group;
    }

    // Getters et setters

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getManaMax() {
        return manaMax;
    }

    public void setManaMax(double manaMax) {
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
