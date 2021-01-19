package outerhaven;

import javafx.scene.paint.Color;
import outerhaven.Interface.Effets;
import outerhaven.Mecaniques.Enchere;
import outerhaven.Entites.Personnages.Personne;
import java.util.ArrayList;

import static outerhaven.Plateau.*;

/**
 * Cette classe gère une équipe de personnage, avec une couleur, ainsi que de l'argent qu'elle pourra dépenser pour enchérir ou acheter des unités sur le plateau.
 */

public class Equipe {
    /**
     * Liste des personnage quelle contient.
     */
    private final ArrayList<Personne> team;
    /**
     * Couleur qui défini une équipe.
     */
    private final Color couleur;
    /**
     * Argent que possède l'équipe.
     */
    private double argent;

    public Equipe(Color couleur) {
        this.team = new ArrayList<>();
        this.couleur = couleur;
        this.argent = 0;
    }

    /**
     * Cette methode permet de gérer les l'augmentation d'enchère au tour par tour, en remboursant et alternant les équipes.
     * @param prix le prix de l'enchère.
     * @param e équipe qui enchéri.
     */
    public void augmenterEnchere(double prix, Enchere e) {
        if (prix > e.getPrixMinimal()) {
            this.setArgent(this.getArgent() - prix);
            if (equipeSelectionne == getE1()) {
                getE2().setArgent(getE2().getArgent() + e.getPrixMinimal());
            } else {
                getE1().setArgent(getE1().getArgent() + e.getPrixMinimal());
            }
            e.setPrixMinimal(prix);
            e.setEquipeGagnante(this);
            barre.getButtonTeamSelect().setEffect(null);
            equipeSelectionne = Equipe.getOtherTeam();
            barre.getButtonTeamSelect().setEffect(new Effets().putInnerShadow(Plateau.equipeSelectionne.getCouleur()));
        }
    }

    @Override
    public String toString() {
        StringBuilder affichage = new StringBuilder("Equipe : " + "\n");
        for (Personne p : team) {
            affichage.append(p.toString());
        }
        return affichage.toString();
    }

    /**
     * Cette section contient tout les getters et setters de Equipe.
     */

    public Color getCouleur() {
        return couleur;
    }

    public ArrayList<Personne> getTeam() {
        return team;
    }

    public int getNbPersonne(){
        return this.team.size();
    }

    public void setNbPersonne() {
        Plateau.updateNbPersonne();
    }

    public void setArgent(double argent) {
        this.argent = argent;
        barre.updateArgentEquipes();
    }

    public double getArgent() {
        return argent;
    }

    public static Equipe getOtherTeam() {
        if (equipeSelectionne == getE1()) {
            return getE2();
        } else {
            return getE1();
        }
    }
}
