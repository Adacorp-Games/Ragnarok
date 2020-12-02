package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Invocations.Lich;
import outerhaven.Personnages.PersonnagesPrime.PersonnagePrime;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import java.util.ArrayList;

public class NecromancienPrime extends PersonnagePrime {

    public NecromancienPrime() {
        super(800 * getPrimeMultiplier(), 50 * getPrimeMultiplier(), 1000 * getPrimeMultiplier(), 100 * getPrimeMultiplier(), 4 * getPrimeMultiplier(), 1, 100 * getPrimeMultiplier());
        // super(2000, 50, 1000, 100, 4, 1, 100);
    }

    public NecromancienPrime(Equipe team, Case position) {
        super(800 * getPrimeMultiplier(), 50 * getPrimeMultiplier(), 1000 * getPrimeMultiplier(), 100 * getPrimeMultiplier(), 4 * getPrimeMultiplier(), 1, team, position, 100 * getPrimeMultiplier());
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

    public void action() {
        // Gagne de la mana chaque tour
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        if (this.getPosition().nbVoisinsLibres() == 0) {
            System.out.println(this.getName() + " patiente");
        } else {
            if (getPosition().pathToPerso(getOtherTeam()).size() == 0) {
                System.out.println(this.getName() + " patiente");

                // Invocation de morts dans les cases libres autour de lui
            } else if (this.getMana() > 100 && this.getPosition().nbVoisinsLibres() > 0 && this.getCooldown() >= 3) {
                //while (this.getMana() > 0) {
                System.out.println("Invoque");
                invocation();
                //}
                // Décrémentation de son mana après le sort lancé
                this.setMana(this.getMana() - 100);
                this.setCooldown(0);

            } else {
                ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
                System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));
                if (pathToEnnemy.size() - 1 <= this.getRange()) {
                    System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
                    attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
                } else {
                    System.out.println(this.getName() + " se déplace");
                    deplacer(pathToEnnemy.get(this.getSpeed()));
                }
                // System.out.println("Vie restante de la cible " + getHealth());
            }
        }
    }

    public void invocation() {
        for (Case c : this.getPosition().voisinsLibres(true)) {
            // Remplissage des cases voisines vides par des invocations
            c.getContenu().add(new Lich(this.getTeam(), c));
            c.setAffichagecontenu(c.getContenu().get(0).affichagePersonnage());
            c.getContenu().get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(c.getAffichagecontenu());
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new NecromancienPrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("NécromancienPrime (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nInvocateur de liches.\nGagne 25 de mana par tour.\nPossède un vol de vie sur ses attaques.\nPour 100 de mana, rempli les cases voisines libres avec des skelettes." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(NecromancienPrime.class.getResourceAsStream("/Images/Personnes/NecromancerPrime.png"));
    }
}
