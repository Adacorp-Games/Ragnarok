package outerhaven.Mecaniques.Alterations;

import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class AlterationVoid extends Alteration {

    public AlterationVoid(int duree) {
        super(duree);
        super.setImage(Case.hexagone_imgBlock);
    }

    public AlterationVoid(int duree, Equipe equipe) {
        super(duree, equipe);
        super.setImage(Case.hexagone_imgBlock);
    }

    @Override
    public void appliquerEffet(Personne p) {
        p.prendreDégâts(p.getHealth());
    }
}
