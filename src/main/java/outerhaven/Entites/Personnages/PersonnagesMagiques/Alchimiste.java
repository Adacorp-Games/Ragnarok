package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationPoison;

public class Alchimiste extends PersonneMagique {

    public Alchimiste() {
        super(1000, 80, 650, 150, 5, 1, 100);
    }

    public Alchimiste(Equipe team, Case position) {
        super(1000, 80, 650, 150, 5, 1, team, position, 150);
    }

    @Override
    public void pouvoir() {
        if (this.getPathToEnemy().size() - 1 <= this.getRange() && this.getMana() >= 100) {
            this.empoisonnerAOE(this.getPathToEnemy().get(this.getPathToEnemy().size() - 1), 50, 10);
            this.setMana(this.getMana() - 100);
        }
    }

    public void empoisonnerAOE(Case c, int puissance, int duree) {
        ajouterAlter(new AlterationPoison(puissance, duree, this.getTeam()), c);
        for (Case cv : c.getCaseVoisines()) {
            ajouterAlter(new AlterationPoison(puissance, duree, this.getTeam()), cv);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
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
