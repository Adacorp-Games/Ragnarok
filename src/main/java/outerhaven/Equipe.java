package outerhaven;

import javafx.scene.paint.Color;
import outerhaven.Mecaniques.Enchere;
import outerhaven.Personnages.Personne;
import java.util.ArrayList;

import static outerhaven.Plateau.*;

/**
 * Cette classe gere une equipe de personnage, avec une couleur, ainsi que de l'argent
 */

public class Equipe {
    /**
     * Liste des personnage quelle contient
     */
    private final ArrayList<Personne> team;
    /**
     * Couleur qui defini une equipe
     */
    private final Color couleur;
    /**
     * Argent que possede l'equipe
     */
    private double argent;

    public Equipe(Color couleur) {
        this.team = new ArrayList<>();
        this.couleur = couleur;
        this.argent = 0;
    }

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
     * cette section contient tout les getters et setters de Equipe
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
}
