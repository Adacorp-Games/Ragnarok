package outerhaven.Mecaniques.Evenements;

import outerhaven.Case;
import outerhaven.Mecaniques.Alterations.Alteration;
import outerhaven.Mecaniques.Alterations.AlterationPoison;
import outerhaven.Plateau;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe permettant la présente d'événements aléatoires dans la partie si l'option est activée.
 */

public class Evenement {
    public static ArrayList<Evenement> listeEvents = new ArrayList<>();
    public static boolean activerEvenement = false;
    public static int pourcentageEvenement;
    public static double fréquenceEvenement;
    private Alteration alteration;
    public static final Evenement event = new Evenement(new AlterationPoison(50, 15));

    public Evenement(Alteration alteration) {
        this.alteration = alteration;
    }

    // TODO : Faire des classes filles poison, gel ... + boutons events et liste<Event>

    /**
     * Cette methode permet de lancer un événement sur une liste de case aléatoires.
     */
    public static void generationEvenements() {
        if (Plateau.tour % fréquenceEvenement == 0) {
            for (Case c : Case.listeCase) {
                double random = Math.random() * 100;
                if (random < pourcentageEvenement) {
                    Collections.shuffle(listeEvents);
                    c.ajouterAlter(listeEvents.get(0).alteration);
                    // c.ajouterAlter(event.alteration);
                }
            }
        }
    }

    /**
     * Cette section contient tout les getters et setters de Evenement.
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
