package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Alterations.AlterationFreeze;

public class Archimage extends PersonneMagique {

    public Archimage() {
        super(1200, 60, 800, 450, 8, 1, 100);
    }

    public Archimage(Equipe team, Case position) {
        super(1200, 60, 800, 450, 8, 1, team, position, 110);
    }

    @Override
    public void pouvoir() {
        // Téléportation si cible trop proche (danger détecté)
        if (this.getDanger() && this.getPosition().nbVoisinsLibres() > 0 && this.getMana() >= 50) {

            // Freeze les cases autour de lui et les personnes qui y sont ou y rentrent
            gelAOE(this.getPosition());

            // Cherche un voisin libre et un voisin libre du voisin libre si possible
            Case voisinLibre = this.getPosition().getRandomVoisinLibre().get(0);
            if (voisinLibre.nbVoisinsLibres() > 0) {
                if (voisinLibre.getRandomVoisinLibre().get(0).nbVoisinsLibres() > 0) {
                    deplacer(voisinLibre.getRandomVoisinLibre().get(0).getRandomVoisinLibre().get(0));
                } else {
                    deplacer(voisinLibre);
                }
            }
            this.setMana(this.getMana() - 50);

        }
    }

    public void gelAOE(Case c) {
        ajouterAlter(new AlterationFreeze(50, 10, this.getTeam()), c);
        for (Case cv : c.getCaseVoisines()) {
            ajouterAlter(new AlterationFreeze(50, 10, this.getTeam()), cv);
        }
    }

    @Override
    public Personne personneNouvelle(Equipe team, Case position) {
        return new Archimage(team, position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Archimage (" + this.getCost() + " €) :\n");
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
        return new Image(Archimage.class.getResourceAsStream("/Images/Personnes/Archimage.png"));
    }
}
