package outerhaven;

import javafx.scene.paint.Color;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

import static outerhaven.Plateau.barre;

public class Equipe {
    private ArrayList<Personne> team;
    private Color couleur;
    private double argent;

    public Equipe(Color couleur) {
        this.team = new ArrayList<>();
        this.couleur = couleur;
        this.argent = 0;
        //this.couleur = Color.color(Math.random(), Math.random(), Math.random()); // Couleur RBG aléatoire;
    }

    public Color getCouleur() {
        return couleur;
    }

    public ArrayList<Personne> getTeam() {
        return team;
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
        //barre.updateArgentEquipes();
    }

    public double getArgent() {
        return argent;
    }
}
