package outerhaven;

public class Archer extends Personne{

    public Archer() {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1);
    }

    public Archer(Case position) {
        super(Personne.getRandomName(), 150, 50, 150, 100, 2, 1, position);
        this.position.setStatus(true);
    }
}
