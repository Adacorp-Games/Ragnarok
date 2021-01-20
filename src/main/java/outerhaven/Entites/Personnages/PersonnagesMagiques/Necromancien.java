package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Invocations.Mort;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Plateau;

import java.util.ArrayList;

public class Necromancien extends PersonneMagique {

    public Necromancien() {
        super(800, 50, 1000, 100, 4, 1, 100);
    }

    public Necromancien(Equipe team, Case position) {
        super(800, 50, 1000, 100, 4, 1, team, position, 100);
    }

    @Override
    public void attaquer(Personne p) {
        double damageMultiplier = this.getDamage() / (this.getDamage() + this.getArmor() / 5);
        double totalDamage = this.getDamage() * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            p.prendreDégâts(totalDamage);
            this.volDeVie(totalDamage);
        }
    }

    /**
     * Action de toute personneMagique en fonction de la distance avec sa cible la plus proche et de son/ses pouvoir(s).
     */
    @Override
    public void action() {
        this.gainMana(); // this gagne du mana si this peut effectuer une action.
        this.genererChemin(); // Créer le chemin vers l'objectif de this (attaquer, protéger un bâtiment ...).
        if (this.getCooldown() >= 3) {
            this.pouvoir(); // Utilise le pouvoir de this.
        } else {
            this.comportementsBasiques(); // Se déplace ou attaque.
        }
    }

    /*@Override
    public void action() {
        this.gainMana(); // Gagne de la mana chaque tour.
        ArrayList<Case> pathToEnemy = calculerChemin();
        ArrayList<Case> pathToEnemyD = this.getPosition().pathToPerso(getOtherTeam());
        if (this.getMana() > 100 && this.getPosition().nbVoisinsLibres() > 0 && this.getCooldown() >= 3) {
            // Invocation de morts dans les cases libres autour de lui si cases voisines libres et CD rechargé.
            invocation();
            // Décrémentation de son mana après le sort lancé.
            this.setMana(this.getMana() - 100); // Draine 100 de mana afin de lancer cette invocation.
            this.setCooldown(0); // Met un CD de 3 tous sur l'invocation.
        } else {
            // Partie usuelle de la méthode action().
            if (pathToEnemyD.size() - 1 <= this.getRange()) {
                attaquer(pathToEnemyD.get(pathToEnemyD.size() - 1).getContenu().get(0));
            } else {
                deplacer(pathToEnemy.get(this.getSpeed()));
            }
        }
    }*/

    @Override
    public void pouvoir() {
        if (this.getMana() > 100 && this.getPosition().nbVoisinsLibres() > 0) {
            // Invocation de morts dans les cases libres autour de lui si cases voisines libres et CD rechargé.
            invocation();
            // Décrémentation de son mana après le sort lancé.
            this.setMana(this.getMana() - 100); // Draine 100 de mana afin de lancer cette invocation.
            this.setCooldown(0); // Met un CD de 3 tous sur l'invocation.
        }
    }

    /**
     * Methode permettant l'invocation d'unité par le Necromantien.
     */
    public void invocation() {
        for (Case c : this.getPosition().voisinsLibres(true)) {
            // Remplissage des cases voisines vides par des invocations
            c.getContenu().add(new Mort(this.getTeam(), c));
            c.setAffichageContenu(c.getContenu().get(0).affichagePersonnage());
            c.getContenu().get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(c.getAffichageContenu());
        }
    }

    public void volDeVie(double totalDamage) {
        // Vol en vie à la cible le montant de dégâts infligés
        if (this.getHealth() < this.getMaxHealth() - totalDamage) {
            this.soigner(totalDamage);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Necromancien(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Nécromancien (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nInvocateur de squelettes.\nGagne 25 de mana par tour.\nPossède un vol de vie sur ses attaques.\nPour 100 de mana, rempli les cases voisines libres avec des skelettes." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Necromancien.class.getResourceAsStream("/Images/Personnes/Necromancer.png"));
    }
}
