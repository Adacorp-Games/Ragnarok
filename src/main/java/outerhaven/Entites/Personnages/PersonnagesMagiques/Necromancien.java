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
        // super(2000, 50, 1000, 100, 4, 1, 100);
    }

    public Necromancien(Equipe team, Case position) {
        super(800, 50, 1000, 100, 4, 1, team, position, 100);
    }

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
    public void action() {
        this.gainMana(); // Gagne de la mana chaque tour.
        if (this.getMana() > 100 && this.getPosition().nbVoisinsLibres() > 0 && this.getCooldown() >= 3) {
            // Invocation de morts dans les cases libres autour de lui si cases voisines libres et CD rechargé.
            invocation();
            // Décrémentation de son mana après le sort lancé.
            this.setMana(this.getMana() - 100); // Draine 100 de mana afin de lancer cette invocation.
            this.setCooldown(0); // Met un CD de 3 tous sur l'invocation.
        } else {
            // Partie usuelle de la méthode action().
            ArrayList<Case> pathToEnemy = calculerChemin();
            ArrayList<Case> pathToEnemyD = this.getPosition().pathToPerso(getOtherTeam());
            if (pathToEnemyD.size() - 1 <= this.getRange()) {
                attaquer(pathToEnemyD.get(pathToEnemyD.size() - 1).getContenu().get(0));
            } else {
                deplacer(pathToEnemy.get(this.getSpeed()));
            }
        }
    }

    /**
     * Methode permettant l'invocation d'unité par le Necromantien.
     */
    public void invocation() {
        for (Case c : this.getPosition().voisinsLibres(true)) {
            // Remplissage des cases voisines vides par des invocations
            c.getContenu().add(new Mort(this.getTeam(), c));
            c.setAffichagecontenu(c.getContenu().get(0).affichagePersonnage());
            c.getContenu().get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(c.getAffichagecontenu());
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
        return new Text("\nInvocateur de skelettes.\nGagne 25 de mana par tour.\nPossède un vol de vie sur ses attaques.\nPour 100 de mana, rempli les cases voisines libres avec des skelettes." + "\n" +
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
