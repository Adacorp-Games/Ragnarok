package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationFreeze;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Entites.Personnages.Personne;

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
        boolean danger = getDanger();

        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

        // Téléportation si cible trop proche (danger détecté)
        if (danger && this.getPosition().nbVoisinsLibres() > 0 && this.getMana() >= 50) {

            // Freeze les cases autour de lui et les personnes qui y sont ou y rentrent
            ajouterAlter(new AlterationFreeze(60, 15, this.getTeam()), this.getPosition());
            ajouterAlterVoisine(new AlterationFreeze(60, 15, this.getTeam()), this.getPosition());

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
