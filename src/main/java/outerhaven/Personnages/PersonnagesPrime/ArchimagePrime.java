package outerhaven.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class ArchimagePrime extends Archimage {

    public ArchimagePrime() {
        this.augmenterStats(2);
    }

    public ArchimagePrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void action() {
        this.gainMana();
        boolean danger = false;

        // Vérification si les cases voisines contiennent au moins un ennemi
        for (Case c : this.getPosition().voisinsLibres(false)) {
            // Si la case voisine n'est pas vide
            if (c.getContenu().size() != 0) {
                // Si le contenu de la case n'est pas un allié
                if (c.getContenu().get(0).getTeam() != this.getTeam()) {
                    danger = true;
                }
            }
        }

        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

        // Téléportation si cible trop proche (danger détecté)
        if (danger && this.getPosition().nbVoisinsLibres() > 0 && this.getMana() >= 50) {

            // Freeze les cases autour de lui et les personnes qui y sont ou y rentrent
            ajouterAlter("freeze",60,15,this.getPosition(), this.getTeam());
            ajouterAlterVoisine("freeze",60,15,this.getPosition(), this.getTeam());

            // Cherche un voisin libre et un voisin libre du voisin libre si possible
            Case voisinLibre = this.getPosition().getRandomVoisinLibre().get(0);
            if (voisinLibre.nbVoisinsLibres() > 0) {
            /*if (voisinLibre.getRandomVoisinLibre().get(0).nbVoisinsLibres() > 0) {
                déplacer(this.getPosition().getRandomVoisinLibre().get(0).getRandomVoisinLibre().get(0));
            } else {*/
                deplacer(voisinLibre);
                //}
            }
            this.setMana(this.getMana() - 50);

        } else if (pathToEnnemy.size() - 1 <= this.getRange()) {
            System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
            attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
        } else {
            System.out.println(this.getName() + " se déplace");
            deplacer(pathToEnnemy.get(this.getSpeed()));
        }
        // System.out.println("Vie restante de la cible " + getHealth());
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new ArchimagePrime(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("ArchimagePrime (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nAttaque puissante et grande portée mais vie et armure plus basses.\nPeut altérer les cases.\nGagne 25 de mana par tour.\nPeut se téléporter dans une case voisine en cas d'ennemi dans une case\nvoisine. La case sur laquelle il se téléporte et ses cases voisines\nsont gelées et les personne allant dedans ne peuvent rien faire." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégats : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(ArchimagePrime.class.getResourceAsStream("/Images/Personnes/ArchimagePrime.png"));
    }
}
