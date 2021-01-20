package outerhaven.Mecaniques.Poste;

import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;

import java.util.ArrayList;

public abstract class Poste {

    public abstract ArrayList<Case> calculerChemin(Personne p);
}
