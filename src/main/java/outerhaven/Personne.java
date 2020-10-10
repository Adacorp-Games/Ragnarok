package outerhaven;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public abstract class Personne {
    String name; // Stocké dans un tableau.
    double health;
    double maxHealth;
    double armor;
    double cost;
    int damage;
    int range; // Portée d'attaque en nombre de case.
    int speed; // Nombre de case qu'il parcourt chaque tour.
    Case position;

    public Personne(String name, double health, double armor, double cost, int damage, int range, int speed) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.position = position;
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

    public Case getPosition() {
        return position;
    }

    public void setPosition(Case position) {
        this.position = position;
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

    public void déplacer(Case[] c) {
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

    public Group afficherSante() {
        double taille = 100;
        Rectangle barre = new Rectangle(taille, taille/10, Color.BLACK);
        Rectangle vie = new Rectangle(taille-4, taille/10-4, Color.RED);

        barre.setX(300);
        barre.setY(200);

        vie.setY(barre.getY()+2);
        vie.setX(barre.getX()+2);

        DoubleProperty healthPercentage = new SimpleDoubleProperty(1.0);
        DoubleBinding b = vie.widthProperty().multiply(healthPercentage);

        double percentage = (this.getHealth() / maxHealth);
        double width = (percentage * (taille-4));
        vie.widthProperty().setValue(width);

        Group group = new Group();
        group.getChildren().add(barre);
        group.getChildren().add(vie);

        return group;
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
        return listName.get(new Random().nextInt(listName.size()));

        /*String name = "Alex" "Ilyes", "Pierre-Antoine", "Julien", "Hamza" , "Jérôme" , "Erwan" , "Gaël";
        int r = (int) (Math.random()*5);
        String rand = new String [] {"Alex", "Ilyes", "Pierre-Antoine", "Julien", "Hamza" , "Matthieu", "Jérôme" , "Erwan" , "Gaël"}[r];
        return rand;*/
    }
}