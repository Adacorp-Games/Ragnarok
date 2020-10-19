package outerhaven;

public class Guerrier extends Personne {

    public Guerrier(Equipe team) {
        super(Personne.getRandomName(), 200, 100, 100, 75, 1, 1, team);
    }

    public Guerrier(Equipe team, Case position) {
        super(Personne.getRandomName(), 200, 100, 100, 75, 1, 1, team, position);
        this.position.setStatus(true);
    }
}
