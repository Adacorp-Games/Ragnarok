package outerhaven.Entites.Personnages.PersonnagesPrime;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Invocations.Liche;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Necromancien;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Plateau;

public class NecromancienPrime extends Necromancien {

    public NecromancienPrime() {
        this.augmenterStats(2);
    }

    public NecromancienPrime(Equipe team, Case position) {
        super(team, position);
        this.augmenterStats(2);
    }

    @Override
    public void invocation() {
        for (Case c : this.getPosition().voisinsLibres(true)) {
            // Remplissage des cases voisines vides par des invocations
            c.getContenu().add(new Liche(this.getTeam(), c));
            c.setAffichageContenu(c.getContenu().get(0).affichagePersonnage());
            c.getContenu().get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(c.getAffichageContenu());
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
        return new Text("\nInvocateur de liches.\nGagne 25 de mana par tour.\nPossède un vol de vie sur ses attaques.\nPour 100 de mana, rempli les cases voisines libres avec des liches." + "\n" +
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
