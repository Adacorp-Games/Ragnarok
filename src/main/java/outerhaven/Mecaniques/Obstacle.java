package outerhaven.Mecaniques;

import outerhaven.Case;

/**
 * classe en cour de developpement
 */
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
