package outerhaven;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Equipe {
    ArrayList<Personne> team = new ArrayList<>();
    private Color couleur = Color.color(Math.random(), Math.random(), Math.random()); // Couleur RBG aléatoire

    public Color getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {
        return "Equipe : " + "\n" + team;
    }
}
