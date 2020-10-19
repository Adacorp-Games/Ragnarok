package outerhaven;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static outerhaven.Plateau.taille;
import static outerhaven.Plateau.personnages;

public abstract class Personne {
    private String name; // Stocké dans un tableau.
    private double health;
    private double maxHealth;
    private double armor;
    private double cost;
    private int damage;
    private int range; // Portée d'attaque en nombre de case.
    private int speed; // Nombre de case qu'il parcourt chaque tour.
    Case position;
    private Equipe team;
    public static Image person_img1 = new Image("https://cdn.discordapp.com/attachments/764528562429624391/766184794068877332/person.png");
    private ImageView person = new ImageView(person_img1);
    /*double positionX = position.getPosX();
    double positionY = position.getPosY();*/

    public Personne(String name, double health, double armor, double cost, int damage, int range, int speed, Equipe team) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        personnages.add(this);
        this.team = team;
        this.team.getTeam().add(this);
    }

    public Personne(String name, double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position) {
        this(name, health, armor, cost, damage, range, speed, team);
        this.position = position;
        this.position.setStatus(true);
        //this.team.addPersonne(this);
        //this.position.setCouleur(this.team.getCouleur());
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

    public Equipe getTeam() {
        return team;
    }

    public void subirDegats(Personne p) {
        double damageMultiplier = damage / (damage + armor/5);
        double totalDamage = damage * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            p.setHealth(p.getHealth() - totalDamage);
        }
        if (p.getHealth() <= 0) {
            System.out.println(p.getName() + " est mort !");

            // Voir si on peut le mettre dans une liste de morts (pour les compter) ou juste les supprimer d'une liste des Personnes sur le plateau.
        }
    }

    public void inter() {
        person.setOnMouseClicked((mouseEvent) -> {

        });
    }

    public void déplacer(Case c) {
        this.position = c;
    }

    /*public void déplacer(Case[] c) {
        //this.position = c;
    }*/

    public void action(String a) {
        if (a == "attaquer") {

        }
        if (a == "déplacer") {
            Case c = position;
            //déplacer(c.getCaseVoisines());
        }
    }

    public Group affichagePersonnage() {
        Text name = new Text();
        name.setText(this.getName());
        name.setX(position.getPosX());
        name.setY(position.getPosY());

        Group group = new Group();
        group.getChildren().add(afficherImage());
        group.getChildren().add(name);
        return group;
    }

    public Group afficherImage() {
        ImageView person = new ImageView(person_img1);
        person.setFitHeight(taille/1.5);
        person.setFitWidth(taille/3);
        person.setX(position.getPosX() + taille/3);
        person.setY(position.getPosY() - taille/20);

        Group group = new Group();
        Group sante = afficherSante();
        group.getChildren().add(person);

        person.setOnMouseClicked((mouseEvent) -> {
            if (!group.getChildren().contains(sante)) {
                group.getChildren().add(sante);
            } else {
                group.getChildren().remove(sante);
            }
        });

        return group;
    }

    public Group afficherSante() {
        Rectangle barre = new Rectangle(taille, taille/10, Color.BLACK);
        Rectangle vie = new Rectangle(taille-4, taille/10-4, Color.RED);

        barre.setX(getPosition().posX);
        barre.setY(getPosition().posY + taille/2.2);

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
    }
}