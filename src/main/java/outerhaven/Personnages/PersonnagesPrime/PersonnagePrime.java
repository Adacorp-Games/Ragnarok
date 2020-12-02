package outerhaven.Personnages.PersonnagesPrime;

import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Personnages.PersonnagesMagiques.PersonneMagique;

public abstract class PersonnagePrime extends PersonneMagique {
    private static int primeMultiplier = 2;

    public PersonnagePrime(double health, double armor, double cost, int damage, int range, int speed, double mana) {
        super(health, armor, cost, damage, range, speed, mana);
    }

    public PersonnagePrime(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position, double mana) {
        super(health, armor, cost, damage, range, speed, team, position, mana);
    }

    public static int getPrimeMultiplier() {
        return primeMultiplier;
    }

    public static void setPrimeMultiplier(int primeMultiplier) {
        PersonnagePrime.primeMultiplier = primeMultiplier;
    }
}
