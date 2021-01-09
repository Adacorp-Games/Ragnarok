package outerhaven.Mecaniques;

import outerhaven.Case;
import outerhaven.Plateau;

public class Evenement {
    public static boolean activerEvenement = false;
    public static int pourcentageEvenement;
    public static double fréquenceEvenement;
    Alteration alteration;

    public Evenement(Alteration alteration) {
        this.alteration = alteration;
    }

    /**
     * Cette methode permet de lancer un evenement sur une liste de case aleatoires
     */
    public void generationEvenements() {
        if (Plateau.tour % fréquenceEvenement == 0) {
            for (Case c : Plateau.listeCase) {
                double random = Math.random() * 100;
                if (random < pourcentageEvenement) {
                    c.ajouterAlter(alteration.getEffet(), alteration.getPuissance(), alteration.getDuree());
                }
            }
        }
    }

    /**
     * Cette section contient tout les getters et setters de Evenement
     */
    public static void setActiverEvenement(boolean activerEvenement) {
        Evenement.activerEvenement = activerEvenement;
    }

    public static void setPourcentageEvenement(int pourcentageEvenement) {
        Evenement.pourcentageEvenement = pourcentageEvenement;
    }

    public static void setFréquenceEvenement(double fréquenceEvenement) {
        Evenement.fréquenceEvenement = fréquenceEvenement;
    }
}
