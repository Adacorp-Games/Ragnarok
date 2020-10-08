package outerhaven;

import java.util.ArrayList;
import java.util.Random;

public abstract class Personne {
    String name; // Stocké dans un tableau.
    double health;
    double armor;
    double cost;
    int damage;
    int range; // Portée d'attaque en nombre de case.
    int speed; // Nombre de case qu'il parcourt chaque tour.
    Case position;

    public Personne(String name, double health, double armor, double cost, int damage, int range, int speed /*, Case position*/) {
        this.name = name;
        this.health = health;
        this.armor = armor;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        //this.position = position;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getArmor() {
        return armor;
    }

    public double getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void subirDegats(Personne p) {
        double damageMultiplier = damage / (damage + armor/5);
        double totalDamage = damage * damageMultiplier;
        p.setHealth(p.getHealth() - totalDamage);
        if (p.getHealth() <= 0) {
            System.out.println(p.getName() + " est mort !");

            // Voir si on peut le mettre dans une liste de morts (pour les compter) ou juste les supprimer d'une liste des Personnes sur le plateau.
        }
    }

    public void déplacer(ArrayList<Case> c) {
        //this.position = c;
    }

    public void action(String a) {
        if (a == "attaquer") {

        }
        if (a == "déplacer") {
            Case c = position;
            déplacer(c.getCaseVoisines());
        }
    }
    public static String getRandomName() {
        // Cela pourrait être mis dans une classe séparée.
        ArrayList<String> listName = new ArrayList<>();
        listName.add("Alex");
        listName.add("Ilyes");
        listName.add("Pierre-Antoine");
        listName.add("Julien");
        listName.add("Hamza");
        listName.add("Jérôme");
        listName.add("Erwan");
        listName.add("Gaël");
        String randName = listName.get(new Random().nextInt(listName.size()));
        /*listName.remove(randName);
        System.out.println(listName.size());*/
        return randName;

        /*String name = "Alex" "Ilyes", "Pierre-Antoine", "Julien", "Hamza" , "Jérôme" , "Erwan" , "Gaël";
        int r = (int) (Math.random()*5);
        String rand = new String [] {"Alex", "Ilyes", "Pierre-Antoine", "Julien", "Hamza" , "Matthieu", "Jérôme" , "Erwan" , "Gaël"}[r];
        return rand;*/
    }

}
