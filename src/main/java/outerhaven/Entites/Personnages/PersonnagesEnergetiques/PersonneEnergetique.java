package outerhaven.Entites.Personnages.PersonnagesEnergetiques;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

import static outerhaven.Plateau.taille;

public abstract class PersonneEnergetique extends Personne {
    private double energie;
    private double energieMax;

    public PersonneEnergetique(double health, double armor, double cost, int damage, int range, int speed) {
        super(health, armor, cost, damage, range, speed);
        this.energie = 0;
        this.energieMax = 100;
    }

    public PersonneEnergetique(double health, double armor, double cost, int damage, int range, int speed, Equipe team) {
        super(health, armor, cost, damage, range, speed, team);
        this.energie = 0;
        this.energieMax = 100;
    }

    public PersonneEnergetique(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position) {
        super(health, armor, cost, damage, range, speed, team, position);
        this.energie = 0;
        this.energieMax = 100;
    }

    /**
     * Méthode qui permet d'attaquer le personnage mit en paramètre s'il est d'une équipe différente
     */
    @Override
    public void attaquer(Personne p) {
        double damageMultiplier = this.getDamage() / (this.getDamage() + this.getArmor() / 5);
        double totalDamage = this.getDamage() * damageMultiplier;
        seRenforce(5);
        if (this.getTeam() != p.getTeam()) {
            // Si son énergie est au dessus de 50 son attaque a ses dégâts triplés
            if (this.getEnergie() >= 50) {
                p.prendreDégâts(totalDamage * 3);
                this.setEnergie(this.getEnergie() - 50);
                if (this.getCooldown() >= 5) {
                    p.changeStatus("stun", 2);
                }
            } else {
                p.prendreDégâts(totalDamage);
            }
        }

        // Gagne de l'énergie pour avoir attaqué
        this.gainEnergie();
    }

    @Override
    public Group afficherStats() {
        Group group = new Group();
        group.getChildren().addAll(afficherSante(), afficherEnergie());
        return group;
    }

    public Group afficherEnergie() {
        Rectangle barre = new Rectangle(taille, taille / 10, Color.BLACK);
        Rectangle energie = new Rectangle(taille - 4, taille / 10 - 4, Color.YELLOW);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille / 10 + taille / 2.2);

        energie.setY(barre.getY() + 2);
        energie.setX(barre.getX() + 2);

        double percentageE = (this.getEnergie() / this.getEnergieMax());
        double widthE = (percentageE * (taille - 4));
        energie.widthProperty().setValue(widthE);

        Group group = new Group();
        group.getChildren().addAll(barre, energie);

        return group;
    }

    // Getters et setters

    public double getEnergie() {
        return energie;
    }

    public void setEnergie(double energie) {
        this.energie = energie;
    }

    public double getEnergieMax() {
        return energieMax;
    }

    public void gainEnergie() {
        if (this.getEnergie() < this.getEnergieMax() - 30) {
            this.setEnergie(this.getEnergie() + 30);
        } else if (this.getEnergie() < this.getEnergieMax()) {
            this.setEnergie(this.getEnergieMax());
        }
    }
}
