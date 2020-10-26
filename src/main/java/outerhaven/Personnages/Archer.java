package outerhaven.Personnages;

import outerhaven.Case;
import outerhaven.Equipe;

public class Archer extends Personne {

    public Archer(Equipe team) {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1, team);
    }

    public Archer(Equipe team, Case position) {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1, team, position);
        this.position.setStatus(true);
    }
}
