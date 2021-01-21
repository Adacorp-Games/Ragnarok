package outerhaven.Mecaniques.Alterations;

import javafx.scene.image.Image;
import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

import java.util.ArrayList;

/**
 * Cette classe aura pour principal but de permettre l'existence d'altération, celle-ci s'applique sur des cases et agit sur
 * les statistiques des unités qu'elles contiennent.
 */
public abstract class Alteration {
    /**
     * Liste d'altérations à supprimer au cours du temps.
     */
    public static ArrayList<Case> AlterSupprimer = new ArrayList<>();
    /**
     * L'image qui changera une case qui sera altérée.
     */
    private Image image;
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

    public Alteration(int duree) {
        this.duree = duree;
    }

    public Alteration(int duree, Equipe equipe) {
        this(duree);
        this.equipe = equipe;
    }

    public Alteration(int puissance, int duree) {
        this.puissance = puissance;
        this.duree = duree;
    }

    public Alteration(int puissance, int duree, Equipe equipe) {
        this(puissance, duree);
        this.equipe = equipe;
    }

    /**
     * Supprime définitivement une alteration.
     */
    public static void nettoieCaseAlter() {
        Case.listeCaseAlterees.removeAll(AlterSupprimer);
        AlterSupprimer.clear();
    }

    /**
     * Applique les effets d'une altération sur un personnage (par tour).
     *
     * @param p esr la Personne a qui on va appliquer l'effet positif ou négatif en fonction de l'altération.
     */
    public abstract void appliquerEffet(Personne p);

    /**
     * Méthode réduisant la durée de l'altération à chaque tour.
     */
    public void passeTour() {
        if (this.duree > 1) { // Si la durée est supérieure 1 tour on la décrémente
            this.duree--;
        } else { // Sinon on la supprime
            for (Case c : Case.listeCaseAlterees) {
                if (c.getAlteration() == this) {
                    c.setAlteration(null);
                    c.getHexagone().setEffect(null);
                    AlterSupprimer.add(c);
                }
            }
        }
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

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
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
