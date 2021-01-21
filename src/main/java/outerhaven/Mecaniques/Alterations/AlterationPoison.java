package outerhaven.Mecaniques.Alterations;

import outerhaven.Case;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

public class AlterationPoison extends Alteration {

    public AlterationPoison(int puissance, int duree) {
        super(puissance, duree);
        super.setImage(Case.hexagone_imgPoison);
    }

    public AlterationPoison(int puissance, int duree, Equipe equipe) {
        super(puissance, duree, equipe);
        super.setImage(Case.hexagone_imgPoison);
    }

    @Override
    public void appliquerEffet(Personne p) {
        if (p.getClass() != Alchimiste.class) {
            p.prendreDégâts(this.getPuissance() * 5);
        }
    }
}
