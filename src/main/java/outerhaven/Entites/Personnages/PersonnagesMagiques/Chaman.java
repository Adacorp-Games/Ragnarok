package outerhaven.Entites.Personnages.PersonnagesMagiques;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Invocations.Golem;
import outerhaven.Entites.Personnages.Invocations.Mort;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;
import outerhaven.Mecaniques.Poste.Defenseur;
import outerhaven.Plateau;

public class Chaman extends PersonneMagique {

    public Chaman() {
        super(1500, 50, 1000, 100, 10, 1, 100);
    }

    public Chaman(Equipe team, Case position) {
        super(1500, 50, 1000, 100, 10, 1, team, position, 100);
    }

    @Override
    public void pouvoir() {
        if (this.getMana() > 100 && this.getPosition().nbVoisinsLibres() > 0 && this.getCooldown() >= 5) {
            // Invocation d'un golem dans une case libre autour de lui si cases voisines libres et CD rechargé.
            invocation();
            // Décrémentation de son mana après le sort lancé.
            this.setMana(this.getMana() - 100); // Draine 100 de mana afin de lancer cette invocation.
            this.setCooldown(0); // Met un CD de 3 tous sur l'invocation.
        }
    }

    /**
     * Methode permettant l'invocation d'unité par le Chaman.
     */
    public void invocation() {
        Case c = this.getPosition().getRandomVoisinLibre().get(0);
        c.getContenu().add(new Golem(this.getTeam(), c));
        c.getContenu().get(0).setPoste(new Defenseur(this));
        c.setAffichageContenu(c.getContenu().get(0).affichagePersonnage());
        c.getContenu().get(0).afficherSanteEtNom();
        Plateau.group.getChildren().add(c.getAffichageContenu());
    }

    @Override
    public Personne personneNouvelle(Equipe team,Case position) {
        return new Chaman(team,position);
    }

    @Override
    public Text getinfoTitleText() {
        return new Text("Chaman (" + this.getCost() + " €) :\n");
    }

    @Override
    public Text getinfoDescText() {
        return new Text("\nInvoque des golems qui le protègent.\nGagne 25 de mana par tour.\nPossède un vol de vie sur ses attaques.\nPour 100 de mana, rempli les cases voisines libres avec des skelettes." + "\n" +
                "- PV : " + this.getHealth() + "\n" +
                "- Mana : " + this.getMana() + "\n" +
                "- Armure : " + this.getArmor() + "\n" +
                "- Dégâts : " + this.getDamage() + "\n" +
                "- Portée : " + this.getRange() + "\n");
    }

    @Override
    public Image getImageFace() {
        return new Image(Chaman.class.getResourceAsStream("/Images/Personnes/Shaman.png"));
    }
}
