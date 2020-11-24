package outerhaven.Personnages.PersonnagesEnergetiques;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

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

    public abstract Personne personneNouvelle(Equipe team, Case position);
    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();
    public abstract Image getImageFace();

    /**
     * Méthode qui permet d'attaquer le personnage mit en parametre s'il est d'une équipe différente
     */
    public void attaquer(Personne p) {
        double damageMultiplier = this.getDamage() / (this.getDamage() + this.getArmor() / 5);
        double totalDamage = this.getDamage() * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            // Si son énergie est au dessus de 50 son attaque a ses dégâts triplés
            if (this.getEnergie() >= 50) {
                p.prendreDégâts(totalDamage * 3);
                this.setEnergie(this.getEnergie() - 50);
                if (this.getCooldown() >= 5) {
                    p.stun(2);
                }
            } else {
                p.prendreDégâts(totalDamage);
            }
            if (p.getHealth() <= 0) {
                System.out.println(p.getName() + " est mort !");
            }
        }

        // Gagne de l'énergie pour avoir attaqué
        this.gainEnergie();
    }

    // Affichage santé et nom et mana

    public Group afficherSante() {
        Rectangle barre = new Rectangle(taille, taille/5.3, Color.BLACK);
        Rectangle vie = new Rectangle(taille - 4, taille/10 - 4, Color.RED);
        Rectangle energie = new Rectangle(taille - 4, taille/10 - 4, Color.YELLOW);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille/2.2);

        vie.setY(barre.getY() + 2);
        vie.setX(barre.getX() + 2);

        energie.setY(vie.getY() + vie.getHeight() + 2);
        energie.setX(barre.getX() + 2);

        double percentageV = (this.getHealth() / this.getMaxHealth());
        double widthV = (percentageV * (taille - 4));
        vie.widthProperty().setValue(widthV);

        double percentageE = (this.getEnergie() / this.getEnergieMax());
        double widthE = (percentageE * (taille - 4));
        energie.widthProperty().setValue(widthE);

        Group group = new Group();
        group.getChildren().add(barre);
        group.getChildren().add(vie);
        group.getChildren().add(energie);

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

    public void setEnergieMax(double energieMax) {
        this.energieMax = energieMax;
    }

    public void gainEnergie() {
        if (this.getEnergie() < this.getEnergieMax() - 30) {
            this.setEnergie(this.getEnergie() + 30);
        } else if (this.getEnergie() < this.getEnergieMax()) {
            this.setEnergie(this.getEnergieMax());
        }
    }
}
