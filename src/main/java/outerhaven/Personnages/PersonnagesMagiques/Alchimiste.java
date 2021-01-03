package outerhaven.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Alchimiste extends PersonneMagique {

    public Alchimiste() {
        //    vie  armr cst  dg rnge spd)
        super(1000, 80, 650, 150, 4, 1, 100);
    }

    public Alchimiste(Equipe team, Case position) {
        super(1000, 80, 650, 150, 4, 1, team, position, 150);
    }

    public void action() {
        this.gainMana();
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.getPosition().nbVoisinsLibres());
        ArrayList<Case> pathToEnnemy = new ArrayList<>(this.getPosition().pathToPerso(getOtherTeam()));
        System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));

        // Capacités de l'alchimiste
        if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() >= 100) {
            ajouterAlter("poison", 50, 10, pathToEnnemy.get(pathToEnnemy.size() - 1), this.getTeam());
            ajouterAlterVoisine("poison", 50, 10, pathToEnnemy.get(pathToEnnemy.size() - 1), this.getTeam());
            this.setMana(this.getMana() - 100);

        } else if (pathToEnnemy.size() - 1 <= this.getRange() && this.getMana() < 100) {
            System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
            attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
        } else {
            System.out.println(this.getName() + " se déplace");
            deplacer(pathToEnnemy.get(this.getSpeed()));
        }
        // System.out.println("Vie restante de la cible " + getHealth());
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Alchimiste(team, position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Alchimiste (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nUnité pouvant altérer des cases." + "\n" +
                "Gagne 25 de mana par tour." + "\n" +
                "Peut dépenser 100 de mana pour mettre du poison dans la case sa\ncible et celles voisines.\nLes personnages (hors Alchimistes) marchant dessus subiront des dégâts." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Alchimiste.class.getResourceAsStream("/Images/Personnes/Alchemist.png"));
    }
}
