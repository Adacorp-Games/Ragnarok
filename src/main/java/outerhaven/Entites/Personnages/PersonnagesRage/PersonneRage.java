package outerhaven.Entites.Personnages.PersonnagesRage;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

import static outerhaven.Plateau.taille;

public abstract class PersonneRage extends Personne {
    private double rage;
    private double rageMax;

    public PersonneRage(double health, double armor, double cost, int damage, int range, int speed) {
        super(health, armor, cost, damage, range, speed);
    }

    public PersonneRage(double health, double armor, double cost, int damage, int range, int speed, Equipe team) {
        super(health, armor, cost, damage, range, speed, team);
    }

    public PersonneRage(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position) {
        super(health, armor, cost, damage, range, speed, team, position);
    }

    @Override
    public Group afficherStats() {
        Group group = new Group();
        group.getChildren().addAll(afficherSante(), afficherRage());
        return group;
    }

    public Group afficherRage() {
        Rectangle barre = new Rectangle(taille, taille/10, Color.BLACK);
        Rectangle rage = new Rectangle(taille - 4, taille/10 - 4, Color.DARKRED);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille/10 + taille/2.2);

        rage.setY(barre.getY() + 2);
        rage.setX(barre.getX() + 2);

        double percentageR = (this.getRage() / this.getRageMax());
        double widthR = (percentageR * (taille - 4));
        rage.widthProperty().setValue(widthR);

        Group group = new Group();
        group.getChildren().addAll(barre, rage);

        return group;
    }

    // Getters et setters

    public double getRage() {
        return rage;
    }

    public void setRage(double rage) {
        this.rage = rage;
    }

    public double getRageMax() {
        return rageMax;
    }
}
