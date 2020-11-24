package outerhaven.Mecaniques;

import outerhaven.Case;

public class Obstacle {
    private Case position;
    private double vie;

    public Obstacle(Case position, double vie) {
        this.position = position;
        this.vie = vie;
    }

    public Obstacle(Case position) {
        this.position = position;
    }
}
