package outerhaven.Personnages;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Interface.Effets;
import outerhaven.Plateau;

import java.util.ArrayList;
import java.util.Random;

import static outerhaven.Plateau.*;

public abstract class Personne {
    private String name; // Stocké dans un tableau.
    private static ArrayList<String> listName = new ArrayList<>();
    private Group SanteNom = new Group();
    private double health;
    private double maxHealth;
    private double armor;
    private double cost;
    private int damage;
    private int range; // Portée d'attaque en nombre de case.
    private int speed; // Nombre de case qu'il parcourt chaque tour.
    private Equipe team;

    // A revoir en fonction du deplacement des personnages
    private Case position;

    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();

    public Personne(double health, double armor, double cost, int damage, int range, int speed) {
        this.name = getRandomName();
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        personnages.add(this);
    }

    public Personne(double health, double armor, double cost, int damage, int range, int speed, Equipe team) {
        this.name = getRandomName();
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

    public Personne(double health, double armor, double cost, int damage, int range, int speed, Equipe team, Case position) {
        this(health, armor, cost, damage, range, speed, team);
        this.position = position;
        this.position.setContenu(this);
    }

    public abstract Personne personneNouvelle(Equipe team, Case position);

    public void attaquer(Personne p) {
        double damageMultiplier = damage / (damage + armor/5);
        double totalDamage = damage * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            p.setHealth(p.getHealth() - totalDamage);
        }
        if (p.getHealth() <= 0) {
            System.out.println(p.getName() + " est mort !");
            personnages.remove(p);
            this.team.getTeam().remove(p);
            morts.add(p);
            // Voir si on peut le mettre dans une liste de morts (pour les compter) ou juste les supprimer d'une liste des Personnes sur le plateau.
        }
    }

    public void déplacer(Case c) {
        this.position = c;
    }

    /*public void déplacer(Case[] c) {
        //this.position = c;
    }*/

    public void action() {
        if (position.pathToPerso(getOtherTeam()).size() <= range) {
            attaquer(position.pathToPerso(getOtherTeam()).get(position.pathToPerso(getOtherTeam()).size()).getContenu().get(0));
        } else {
            déplacer(position.pathToPerso(getOtherTeam()).get(0));
        }
    }

    public Group affichagePersonnageBarre(int i){
        Group group = new Group();
        ImageView imageperson = new ImageView(this.getImageFace());
        imageperson.setFitHeight(130);
        imageperson.setFitWidth(100);
        imageperson.setY(Screen.getPrimary().getVisualBounds().getHeight()-160);
        imageperson.setX(200+i*(imageperson.getFitWidth()+50));
        imageperson.setOnMouseEntered((mouseEvent) -> {
            group.getChildren().add(this.afficherInfo(imageperson.getX(),imageperson.getY()));
        });
        imageperson.setOnMouseExited((mouseEvent) -> {
            group.getChildren().remove(1);
        });
        imageperson.setOnMouseClicked((mouseEvent) -> {
            if(Plateau.personneSelectionné==null) {
                Plateau.scene.setCursor(new ImageCursor(getImageFace()));
                Plateau.personneSelectionné=this;
            }
            else{
                Plateau.scene.setCursor(Cursor.DEFAULT);
                Plateau.personneSelectionné=null;
            }
        });
        group.getChildren().add(imageperson);
        return group;
    }

    public Group afficherInfo(double X, double Y) {
        Group description = new Group();
        Rectangle barre = new Rectangle(400 , 130, Color.LIGHTGRAY);
        barre.setX(X);
        barre.setY(Y - 150);
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);
        /*barre.setStyle("-fx-background-color: grey");
        barre.setStyle("-fx-border-width: 2px");
        barre.setStyle("-fx-border-style: solid");
        barre.setStyle("-fx-border-color: black");*/

        Text title = this.getinfoTitleText();
        title.setX(X + 10);
        title.setY(Y + 20 - 130);
        //title.setStyle("-fx-font-style: bold");

        Text descrip = this.getinfoDescText();
        descrip.setX(X + 10);
        descrip.setY(Y + 20 - 130);

        description.getChildren().add(barre);
        description.getChildren().add(title);
        description.getChildren().add(descrip);
        return description;
    }

    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();

    public Group affichagePersonnage() {
        Group group = new Group();
        group.getChildren().add(this.afficherImageFace());
        return group;
    }

    public Group afficherNom() {
        Text name = new Text();
        name.setText(this.getName());
        name.setX(getPosition().getPosX() + 10);
        name.setY(getPosition().getPosY() + taille/2.6);
        name.setStyle("-fx-font-weight: bold");
        name.setEffect(new Effets().putInnerShadow(this.team.getCouleur()));

        Group group = new Group();
        group.getChildren().add(name);
        return group;
    }

    public Group afficherImageFace() {
        ImageView person = new ImageView(this.getImageFace());
        person.setFitHeight(taille/1.5);
        person.setFitWidth(taille/2);
        person.setX(position.getPosX() + taille/3);
        person.setY(position.getPosY() - taille/20);

        Group group = new Group();
        group.getChildren().add(person);
        //if (Plateau.getStatusPartie() != false) {
            person.setOnMouseClicked((mouseEvent) -> {
                selfDelete();
            });
        //}

        return group;
    }

    public void afficherSanteEtNom(){
        SanteNom.getChildren().addAll(afficherSante(),afficherNom());
        if (!group.getChildren().contains(SanteNom)) {
                    group.getChildren().add(SanteNom);
        } else {
                    group.getChildren().remove(SanteNom);
        }
    }

    public void selfDelete() {
        position.seVider();
    }

    public Group afficherSante() {
        Rectangle barre = new Rectangle(taille, taille/10, Color.BLACK);
        Rectangle vie = new Rectangle(taille-4, taille/10-4, Color.RED);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille/2.2);

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

    // Gestion nomRandomName

    public static String getRandomName() {
        if(listName.isEmpty()){
            ajouteNom();
        }
        return listName.get(new Random().nextInt(listName.size()));
    }

    private static void ajouteNom(){
        listName.add("Alex");
        listName.add("Ilyes");
        listName.add("Pierre-Antoine");
        listName.add("Julien");
        listName.add("Hamza");
        listName.add("Jérôme");
        listName.add("Erwan");
        listName.add("Gaël");
        listName.add("Maxime");
        listName.add("Benjamin");
        listName.add("Tanguy");
        listName.add("Samuel");
        listName.add("Santiago");
        listName.add("Mateusz");
        listName.add("Obama");
        listName.add("Johannides");
        listName.add("Molnar");
        listName.add("Mou");
        listName.add("Macron");
        listName.add("Micron");
        listName.add("Basile");
        listName.add("Sarkozy");
        listName.add("Stéphanie");
        listName.add("Hugo");
        listName.add("David");
        listName.add("Pierrick");
        listName.add("Benoit");
        listName.add("Cedric");
        listName.add("Sars-Cov3");
        listName.add("COVID-20");
        listName.add("Nostradamus");
        listName.add("Thanos");
        listName.add("Shrek");
        listName.add("Beanos");
        listName.add("Charles");
        listName.add("KimJong2");
        listName.add("Araki");
        listName.add("Toriyama");
    }

    // Getteur et setteur

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

    public Equipe getOtherTeam() {
        if (listeEquipe.get(0) == this.team) {
            return listeEquipe.get(1);
        } else {
            return listeEquipe.get(0);
        }
    }

    public Image getImageFace() {
        return new Image(Personne.class.getResourceAsStream("../Images/inconnu.png"));
    }
}