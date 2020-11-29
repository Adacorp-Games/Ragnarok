package outerhaven.Mecaniques;

import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Enchere {
    public static ArrayList<Enchere> listeEnchere = new ArrayList<>();
    private Personne produit;
    private double prixMinimal;
    private boolean status;

    public Enchere(Personne produit) {
        this.produit = produit;
        this.prixMinimal = 0;
        this.status = true;
    }

    public void cloreEnchere() {
        this.setStatus(false);
        Enchere.listeEnchere.remove(this);
    }

    public void augmenterEnchere(double prix) {
        if (prix > this.getPrixMinimal()) {
            this.setPrixMinimal(this.getPrixMinimal() + prix);
        }
    }

    public static ArrayList<Enchere> getListeEnchere() {
        return listeEnchere;
    }

    public static void ajouterEnchere(Enchere e) {
        Enchere.listeEnchere.add(e);
    }

    public Personne getProduit() {
        return produit;
    }

    public void setProduit(Personne produit) {
        this.produit = produit;
    }

    public double getPrixMinimal() {
        return prixMinimal;
    }

    public void setPrixMinimal(double prixMinimal) {
        this.prixMinimal = prixMinimal;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
