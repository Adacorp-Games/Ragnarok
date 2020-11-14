package outerhaven;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

import static outerhaven.Plateau.barre;
import static outerhaven.Plateau.getE1;

public class Equipe {
    private ArrayList<Personne> team;
    private Color couleur;
    private double argent;
    private int nbPersonne;

    public Equipe(Color couleur) {
        this.team = new ArrayList<>();
        this.couleur = couleur;
        this.argent = 0;
        this.nbPersonne = team.size();
        //this.couleur = Color.color(Math.random(), Math.random(), Math.random()); // Couleur RBG aléatoire;
    }

    public Color getCouleur() {
        return couleur;
    }

    public ArrayList<Personne> getTeam() {
        return team;
    }

    public int getNbPersonne(){
        return this.team.size();
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
        Plateau.updateNbPersonne();
    }

    @Override
    public String toString() {
        String affichage = "Equipe : " + "\n";
        for (Personne p : team) {
            affichage += p.toString();
        }
        return affichage;
    }

    public void setArgent(double argent) {
        this.argent = argent;
        barre.updateArgentEquipes();
    }

    public double getArgent() {
        return argent;
    }
}
