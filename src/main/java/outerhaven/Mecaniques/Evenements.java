package outerhaven.Mecaniques;

import outerhaven.Case;
import outerhaven.Plateau;

public class Evenements {
    public static boolean activerEvenement = false;
    public static int pourcentageEvenement;
    public static double fréquenceEvenement;
    Alteration alteration;

    public Evenements(Alteration alteration) {
        this.alteration = alteration;
    }

    public void generationEvenements() {
        if (Plateau.nbTour%fréquenceEvenement == 0) {
            for (Case c : Plateau.listeCase) {
                double random = Math.random() * 100;
                if (random < pourcentageEvenement) {
                    c.ajouterAlter(alteration.getEffet(), alteration.getPuissance(), alteration.getDuree());
                }
            }
        }
    }

    public static void setActiverEvenement(boolean activerEvenement) {
        Evenements.activerEvenement = activerEvenement;
    }

    public static void setPourcentageEvenement(int pourcentageEvenement) {
        Evenements.pourcentageEvenement = pourcentageEvenement;
    }

    public static void setFréquenceEvenement(double fréquenceEvenement) {
        Evenements.fréquenceEvenement = fréquenceEvenement;
    }
}
