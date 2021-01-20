package outerhaven.Mecaniques.Poste;

import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public class Defenseur extends Poste {
    private Personne protegee;

    public Defenseur(Personne protegee) {
        this.protegee = protegee;
    }

    @Override
    public ArrayList<Case> calculerChemin(Personne p) {
        ArrayList<Case> chemin;
        if (this.protegee.getHealth() > 0
                && !this.protegee.getPosition().getContenu().get(0).getDanger()
                && this.protegee.getPosition().nbVoisinsLibres() > 0) {
            chemin = p.getPosition().pathDijkstra(this.protegee);
        } else if (p.getPosition().nbVoisinsLibres() > 0) {
            p.setPoste(new Attaquant());
            chemin = p.getPosition().pathDijkstra();
        } else {
            chemin = p.getPosition().pathToPerso(p.getOtherTeam());
        }
        return chemin;
    }

    public Personne getProtegee() {
        return protegee;
    }
}
