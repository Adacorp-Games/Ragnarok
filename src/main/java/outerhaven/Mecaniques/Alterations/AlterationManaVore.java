package outerhaven.Mecaniques.Alterations;

import outerhaven.Equipe;
import outerhaven.Entites.Personnages.Personne;

public class AlterationManaVore extends Alteration {

    public AlterationManaVore(int puissance, int duree) {
        super(puissance, duree);
    }

    public AlterationManaVore(int puissance, int duree, Equipe equipe) {
        super(puissance, duree, equipe);
    }

    @Override
    public void appliquerEffet(Personne p) {
        // TODO (un jour)
    }

    /*public void appliquerEffet(PersonneMagique p) {
        if (this.effet == "manaVore") {
            if (p.getMana() >= 35) {
                p.setMana(p.getMana() - 35);
            } else if (p.getMana() < 35) {
                p.setMana(0);
            }
        }
    }*/
}
