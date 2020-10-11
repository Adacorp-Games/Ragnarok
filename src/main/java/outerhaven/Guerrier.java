package outerhaven;

public class Guerrier extends Personne {

    public Guerrier() {
        super(Personne.getRandomName(), 200, 100, 100, 75, 1, 1);
    }

    public Guerrier(Case position) {
        super(Personne.getRandomName(), 200, 100, 100, 75, 1, 1, position);
        this.position.setStatus(true);
    }
}
