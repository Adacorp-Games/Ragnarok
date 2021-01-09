package outerhaven.Mecaniques;

import outerhaven.Case;

/**
 * Classe en cour de développement.
 * Elle aura pour but de permettre de générer des obstacle destructibles ou non sur le Plateau.
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
