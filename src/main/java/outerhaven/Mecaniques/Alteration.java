package outerhaven.Mecaniques;

import javafx.scene.image.Image;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Personnages.PersonnagesMagiques.PersonneMagique;
import outerhaven.Personnages.Personne;
import outerhaven.Plateau;

import java.util.ArrayList;

public class Alteration {
    /**
     * L'image qui changera une case qui sera altérée.
     */
    private Image image;
    /**
     * Nom caractérisant une alteration.
     */
    private String effet;
    /**
     * Puissance d'une alteration.
     */
    private int puissance;
    /**
     * Durée en tour d'une alteration.
     */
    private int duree;
    /**
     * Équipe a qui appartient l'altération (pas obligatoire).
     */
    private Equipe equipe;
    /**
     * Liste d'altérations à supprimer au cours du temps.
     */
    public static ArrayList<Case> AlterSupprimer = new ArrayList<>();

    public Alteration(String effet, int puissance, int duree) {
        this.effet = effet;
        this.puissance = puissance;
        this.duree = duree;
        if (effet == "poison") {
            this.image = Case.hexagone_imgPoison;
        } else if (effet == "manaVore") {
            this.image = Case.hexagone_imgManaVore;
        } else if (effet == "heal") {
            this.image = Case.hexagone_imgHeal;
        } else if (effet == "freeze") {
            this.image = Case.hexagone_imgFreeze;
        } else if (effet == "void") {
            this.image = Case.hexagone_imgBlock;
        }
    }

    public Alteration(String effet, int puissance, int duree, Equipe equipe) {
        this(effet, puissance, duree);
        this.equipe = equipe;
    }

    /**
     * Applique les effets d'une altération sur un personnage (par tour).
     * @param p
     */
    public void appliquerEffet(Personne p) {
        if (this.effet == "poison" && p.getClass() != Alchimiste.class) {
            p.prendreDégâts(this.puissance * 5);
        } else if (this.effet == "heal" && this.equipe == p.getTeam() && p.getHealth() <= p.getMaxHealth() - this.puissance * 5) {
            p.soigner(this.puissance * 5);
        } else if (this.effet == "heal" && this.equipe == p.getTeam() && p.getHealth() > p.getMaxHealth() - this.puissance * 5) {
            p.setHealth(p.getMaxHealth());
        } else if (this.effet == "freeze" && p.getClass() != Archimage.class) {
            p.prendreDégâts(this.puissance * 2);
        } else if (this.effet == "void") {
            p.prendreDégâts(p.getHealth());
        }
    }

    /**
     * Applique les effets d'un vol de mana sur les PersonneMagique.
     * @param p est la personne à affecter
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
     * Méthode réduisant la durée de l'altération à chaque tour.
     */
    public void passeTour() {
        if (duree > 1) {
            duree--;
        } else {
            for (Case c : Plateau.listeCaseAlterees) {
                if (c.getAlteration() == this) {
                    c.setAlteration(null);
                    c.getHexagone().setEffect(null);
                    AlterSupprimer.add(c);
                }
            }
        }
    }

    /**
     * Supprime définitivement une alteration.
     */
    public static void nettoiCaseAlter() {
        Plateau.listeCaseAlterees.removeAll(AlterSupprimer);
        AlterSupprimer.clear();
    }

    /**
     * Cette section contient tout les getters et setters de Alteration.
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
