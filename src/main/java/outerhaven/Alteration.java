package outerhaven;

import javafx.scene.image.Image;
import outerhaven.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Personnages.PersonnagesMagiques.PersonneMagique;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Alteration {
    /**
     * L'image qui changera une case qui sera alterés
     */
    private Image image;
    /**
     * Nom caracterisant une alteration
     */
    private String effet;
    /**
     * Puissance d'une alteration
     */
    private int puissance;
    /**
     * Durée en tour d'une alteration
     */
    private int duree;
    /**
     * Liste d'alteration à supprimer au cours du temps
     */
    public static ArrayList<Case> AlterSupr = new ArrayList<>();

    public Alteration(String effet, int puissance, int duree) {
        this.effet = effet;
        this.puissance = puissance;
        this.duree = duree;
        if (effet == "poison") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonToxic.png"));
        } else if (effet == "manaVore") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonManaVore.png"));
        } else if (effet == "heal") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonHeal.png"));
        }
    }

    /**
     * applique les effets d'une alteration sur un personnage (par tour)
     * @param p
     */
    public void appliquerEffet(Personne p) {
        if (this.effet == "poison" && p.getClass() != Alchimiste.class) {
            p.setHealth(p.getHealth() - this.puissance * 5);
        } else if (this.effet == "heal" && p.getHealth() <= p.getMaxHealth() - this.puissance * 5) {
            p.setHealth(p.getHealth() + this.puissance * 5);
        } else if (this.effet == "heal" && p.getHealth() > p.getMaxHealth() - this.puissance * 5) {
            p.setHealth(p.getMaxHealth());
        }
    }

    /**
     * Applique les effets d'un vole de mana sur les Personne magique
     * @param p
     */
    public void appliquerEffet(PersonneMagique p) {
        if (this.effet == "manaVore") {
            if (p.getMana() >= 35) {
                p.setMana(p.getMana() - 35);
            } else if (p.getMana() < 35) {
                p.setMana(0);
            }
        }
    }

    /**
     * Methode reduisant la durée à chaque tour
     */
    public void passeTour() {
        if (duree > 1) {
            duree--;
        } else {
            for (Case c : Plateau.listeCaseAlterees) {
                if (c.getAlteration() == this) {
                    c.setAlteration(null);
                    AlterSupr.add(c);
                }
            }
        }
    }

    /**
     * Supprime definitivement une alteration
     */

    public static void nettoiCaseAlter(){
        Plateau.listeCaseAlterees.removeAll(AlterSupr);
        AlterSupr.clear();
    }

    /**
     * cette section contient tout les getteur et setteur de Equipe
     */

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getEffet() {
        return effet;
    }

    public void setEffet(String effet) {
        this.effet = effet;
    }

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
