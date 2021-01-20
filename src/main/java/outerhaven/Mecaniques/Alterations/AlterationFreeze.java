package outerhaven.Mecaniques.Alterations;

import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Entites.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Entites.Personnages.Personne;

public class AlterationFreeze extends Alteration {

    public AlterationFreeze(int puissance, int duree) {
        super(puissance, duree);
        super.setImage(Case.hexagone_imgFreeze);
    }

    public AlterationFreeze(int puissance, int duree, Equipe equipe) {
        super(puissance, duree, equipe);
        super.setImage(Case.hexagone_imgFreeze);
    }

    @Override
    public void appliquerEffet(Personne p) {
        if (p.getClass() != Archimage.class) {
            p.prendreDégâts(this.getPuissance() * 2);
            p.changeStatus("freeze", this.getDuree());
        }
    }
}
