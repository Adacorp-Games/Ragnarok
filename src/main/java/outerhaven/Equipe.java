package outerhaven;

import javafx.scene.paint.Color;
import outerhaven.Personnages.Personne;
import java.util.ArrayList;
import static outerhaven.Plateau.barre;
/**
 *Cette classe gere une equipe de personnage, avec une couleur, ainsi que de l'argent
 */
public class Equipe {
    /**
     * liste des personnage quelle contient
     */
    private final ArrayList<Personne> team;
    /**
     * Couleur qui defini une equipe
     */
    private final Color couleur;
    /**
     * argent que possede l'equipe
     */
    private double argent;

    public Equipe(Color couleur) {
        this.team = new ArrayList<>();
        this.couleur = couleur;
        this.argent = 0;
    }

    /**
     * cette section contient tout les getteur et setteur de Equipe
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

    @Override
    public String toString() {
        StringBuilder affichage = new StringBuilder("Equipe : " + "\n");
        for (Personne p : team) {
            affichage.append(p.toString());
        }
        return affichage.toString();
    }

    public void setArgent(double argent) {
        this.argent = argent;
        barre.updateArgentEquipes();
    }

    public double getArgent() {
        return argent;
    }
}
