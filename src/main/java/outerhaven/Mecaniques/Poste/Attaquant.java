package outerhaven.Mecaniques.Poste;

import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

import static outerhaven.Plateau.activerDijkstra;

public class Attaquant extends Poste {

    @Override
    public ArrayList<Case> calculerChemin(Personne p) {
        ArrayList<Case> chemin;
        if (activerDijkstra && p.getPosition().nbVoisinsLibres() > 0) { // Chemin calculé avec l'algorithme de Dijkstra si option activée.
            chemin = p.getPosition().pathDijkstra();
        } else { // Sinon calcul du chemin avec l'utilisation du calcul vectoriel lambda.
            chemin = p.getPosition().pathToPerso(p.getOtherTeam());
        }
        if (activerDijkstra && chemin.size() <= 0) {
            // Si le personnage (this) est bloqué où a son champ de vision bloqué (ligne d'alliés) on utilise le calcul vectoriel.
            chemin = p.getPosition().pathToPerso(p.getOtherTeam());
        }
        return chemin;
    }
}
