package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationPoison;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public class Alchimiste extends PersonneMagique {

    public Alchimiste() {
        //    vie  armr cst  dg rnge spd)
        super(1000, 80, 650, 150, 5, 1, 100);
    }

    public Alchimiste(Equipe team, Case position) {
        super(1000, 80, 650, 150, 5, 1, team, position, 150);
    }

    @Override
    public void action() {
        this.gainMana();
        ArrayList<Case> pathToEnemy = calculerChemin();

        // Capacités de l'alchimiste
        if (pathToEnemy.size() - 1 <= this.getRange() && this.getMana() >= 100) {
            empoisonnerAOE(pathToEnemy.get(pathToEnemy.size() - 1));
            this.setMana(this.getMana() - 100);

        } else if (pathToEnemy.size() - 1 <= this.getRange()) { // Si l'ennemi le plus proche est dans la portée d'attaque de this alors il l'attaque.
            attaquer(pathToEnemy.get(pathToEnemy.size() - 1).getContenu().get(0));
        } else { // Sinon il se déplace pour se rapprocher de lui.
            deplacer(pathToEnemy.get(this.getSpeed()));
        }
    }

    public void empoisonner(Case c) {
        ajouterAlter(new AlterationPoison(50, 10, this.getTeam()), c);
    }

    public void empoisonnerAOE(Case c) {
        ajouterAlter(new AlterationPoison(50, 10, this.getTeam()), c);
        for (Case cv : c.getCaseVoisines()) {
            ajouterAlter(new AlterationPoison(50, 10, this.getTeam()), cv);
        }
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
