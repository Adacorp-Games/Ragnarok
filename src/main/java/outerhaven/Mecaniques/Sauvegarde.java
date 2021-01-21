package outerhaven.Mecaniques;

import outerhaven.Entites.Personnages.Personne;
import outerhaven.Interface.BarrePersonnage;
import outerhaven.Plateau;

import java.util.ArrayList;

/**
 * Classe permettant de faire une sauvegarde du jeu à un moment précis comme par exemple la fin
 * des enchères afin de pouvoir retourné à cet état facilement (argent et unités achetées).
 */

public class Sauvegarde {
    private final double argentE1;
    private final double argentE2;
    private final ArrayList<Personne> listeClasseE1;
    private final ArrayList<Personne> listeClasseE2;

    public Sauvegarde() {
        this.argentE1 = Plateau.getE1().getArgent();
        this.argentE2 = Plateau.getE2().getArgent();
        this.listeClasseE1 = new ArrayList<>(BarrePersonnage.listeEquipe1);
        this.listeClasseE2 = new ArrayList<>(BarrePersonnage.listeEquipe2);
    }

    public Sauvegarde(double argentE1, double argentE2, ArrayList<Personne> listeClasseE1, ArrayList<Personne> listeClasseE2) {
        this.argentE1 = argentE1;
        this.argentE2 = argentE2;
        this.listeClasseE1 = listeClasseE1;
        this.listeClasseE2 = listeClasseE2;
    }

    public double getArgentE1() {
        return argentE1;
    }

    public double getArgentE2() {
        return argentE2;
    }

    public ArrayList<Personne> getListeClasseE1() {
        return listeClasseE1;
    }

    public ArrayList<Personne> getListeClasseE2() {
        return listeClasseE2;
    }
}
