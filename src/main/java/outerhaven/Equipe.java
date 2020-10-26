package outerhaven;

import javafx.scene.paint.Color;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Equipe {
    private ArrayList<Personne> team;
    private Color couleur;

    public Equipe() {
        this.team = new ArrayList<>();
        this.couleur = Color.color(Math.random(), Math.random(), Math.random()); // Couleur RBG aléatoire;
    }

    public Color getCouleur() {
        return couleur;
    }

    public ArrayList<Personne> getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "Equipe : " + "\n" + team;
    }
}
