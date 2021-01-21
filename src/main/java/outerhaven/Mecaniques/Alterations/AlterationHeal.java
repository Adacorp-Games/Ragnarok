package outerhaven.Mecaniques.Alterations;

import outerhaven.Case;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Equipe;

public class AlterationHeal extends Alteration {

    public AlterationHeal(int puissance, int duree) {
        super(puissance, duree);
        super.setImage(Case.hexagone_imgHeal);
    }

    public AlterationHeal(int puissance, int duree, Equipe equipe) {
        super(puissance, duree, equipe);
        super.setImage(Case.hexagone_imgHeal);
    }

    @Override
    public void appliquerEffet(Personne p) {
        if (this.getEquipe() == p.getTeam() && p.getHealth() <= p.getMaxHealth() - this.getPuissance() * 5) {
            p.soigner(this.getPuissance() * 5);
        } else if (this.getEquipe() == p.getTeam() && p.getHealth() > p.getMaxHealth() - this.getPuissance() * 5) {
            p.setHealth(p.getMaxHealth());
        }
    }
}
