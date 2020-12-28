package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Pretre extends PersonneMagique {

    public Pretre() {
        //    vie  armr cst  dg rnge spd)
        super(700, 50, 350, 110, 2, 1, 100);
    }

    public Pretre(Equipe team, Case position) {
        super(700, 50, 350, 100, 2, 1, team, position, 100);
    }

    public void action() {
        // Gagne de la mana chaque tour et se soigne
        this.gainMana();
        this.soigner(200);

        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        if (this.getPosition().nbVoisinsLibres() == 0) {
            System.out.println(this.getName() + " patiente");
        } else {
            if (getPosition().pathToPerso(getOtherTeam()).size() == 0) {
                System.out.println(this.getName() + " patiente");
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

        // Soigne les cases voisines
        ajouterAlter("heal", 60, 1, this.getPosition(), this.getTeam());
        for (Case c1 : this.getPosition().getCaseVoisines()) {
            ajouterAlter("heal", 60, 1, c1, this.getTeam());
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Pretre(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Prêtre (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nUnité pouvant altérer des cases." + "\n" +
                "Gagne 25 de mana par tour." + "\n" +
                "Soigne les unités alliées dans les cases voisines à la sienne." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Necromancien.class.getResourceAsStream("/Images/Personnes/Priest.png"));
    }
}
