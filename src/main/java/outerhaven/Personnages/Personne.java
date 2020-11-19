package outerhaven.Personnages;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Interface.Effets;
import outerhaven.Personnages.Invocations.Mort;
import outerhaven.Plateau;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static outerhaven.Plateau.*;

public abstract class Personne {
    private String name; // Stocké dans un tableau.
    /**
     * Liste des noms stockés dans un fichier texte modifiable par n'importe qui pour ajouter de la personnalisation
     */
    private static ArrayList<String> listName = new ArrayList<>();
    private Group SanteNom = new Group();
    private double health;      // Vie de la personne
    private double maxHealth;   // Vie maximale de la personne qui est égale à sa vie en début de partie
    private double armor;       // Armure de la personne
    private double cost;        // Coût de la personne
    protected int damage;       // Dégâts de la personne
    protected int range;        // Portée d'attaque en nombre de case
    private int speed;          // Nombre de case qu'il parcourt chaque tour
    private Equipe team;        // Equipe de la personne
    public static boolean barreVisible = false;
    private ImageView imageperson = new ImageView(this.getImageFace());
    private Case position;
    private String status = "normal";

    public double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    public double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();

    public Personne(double health, double armor, double cost, int damage, int range, int speed) {
        this.name = getRandomName();
        this.health = health;
        this.maxHealth = health;
        this.armor = armor;
        this.cost = cost;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        if (this.getClass() != Mort.class) {
            personnages.add(this);
        }
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
        this.team = team;
        if (this.getClass() != Mort.class) {
            personnages.add(this);
            this.team.getTeam().add(this);
        }
    }

    public Personne(double health, double armor, double cost, int damage, int range, int speed, Equipe team,
            Case position) {
        this(health, armor, cost, damage, range, speed, team);
        this.position = position;
        this.position.setContenu(this);
    }

    /**
     * Méthode qui permet d'attaquer le personnage mit en parametre s'il est d'une équipe différente
     */
    public void attaquer(Personne p) {
        double damageMultiplier = damage / (damage + armor / 5);
        double totalDamage = damage * damageMultiplier;
        if (this.getTeam() != p.getTeam()) {
            p.setHealth(p.getHealth() - totalDamage);
            if (p.getHealth() <= 0) {
                System.out.println(p.getName() + " est mort !");
            }
        }
    }

    /**
     * Méthode qui permet de se déplacer dans une case mis en parametre et vidant la case précédente avec une animation
     */
    public void deplacer(Case fin) {
        fin.getContenu().add(this);
        Case casePrecedente = this.position;
        this.setPosition(fin);
        casePrecedente.getContenu().clear();
        AtomicReference<Double> x = new AtomicReference<>(casePrecedente.getAffichagecontenu().getLayoutX());
        AtomicReference<Double> y = new AtomicReference<>(casePrecedente.getAffichagecontenu().getLayoutY());
        double xVec = (casePrecedente.getPosX() - fin.getPosX())/20;
        double yVec = (casePrecedente.getPosY() - fin.getPosY())/20;
        AtomicInteger count= new AtomicInteger(0);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(temps/20), ev -> {
            x.set(x.get() - xVec);
            y.set(y.get() - yVec);
            casePrecedente.getAffichagecontenu().setLayoutX(x.get());
            casePrecedente.getAffichagecontenu().setLayoutY(y.get());
            count.getAndIncrement();
            if(count.get()==20){
                this.position = fin;
                deplacementFinal(casePrecedente,fin);
            }
            }));
        timeline.setCycleCount(20);
        timeline.play();
       
    }

    /**
     * Algo gerant l'animationfinale des deplacements
     * @param depart
     * @param fin
     */
    public void deplacementFinal(Case depart,Case fin ){
        depart.getContenu().add(this);
        fin.seVider();
        depart.seVider();
        fin.rentrePersonnage(this);
        if (Personne.barreVisible) {
            this.afficherSanteEtNom();
        }
    }

    /**
     * Action basique de toute personne en fonction de la distance avec sa cible la plus proche : attaquer ou se déplacer
     */
    public void action() {
        System.out.println("Nombre de case vide autour de " + this.getName() + " : " + this.position.nbVoisinsLibres());
        if (this.position.nbVoisinsLibres() == 0) {
            System.out.println(this.getName() + " patiente");
        } else {
            if (position.pathToPerso(getOtherTeam()).size() == 0) {
                System.out.println(this.getName() + " patiente");
            } else {
                ArrayList<Case> pathToEnnemy = new ArrayList<>(position.pathToPerso(getOtherTeam()));
                System.out.println("Taille du chemin vers l'ennemis le plus proche pour " + this.getName() + " : " + (pathToEnnemy.size() - 1));
                if (pathToEnnemy.size() - 1 <= range) {
                    System.out.println(this.getName() + " (" + this.getHealth() + ") attaque " + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getName() + " (" + pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0).getHealth() + ")");
                    attaquer(pathToEnnemy.get(pathToEnnemy.size() - 1).getContenu().get(0));
                } else {
                    System.out.println(this.getName() + " se déplace");
                    deplacer(pathToEnnemy.get(speed));
                }
            }
        }
    }

    /**
     * Méthode pour ralentir une autre méthode
     * @param nb : int d'attente
     */
    public void waitTEST(long nb) {
        long i = 0;
        for (long j = 0; j < nb; j++) {
            i = j -i + 5;
        }
    }

    /**
     * Affiche les personnages dans la barre
     * @param i : parametre pour la méthode genereBarre() de la classe BarrePersonnage afin de générer chaque personne ne la liste listClasse
     * @return un groupe contenant les personnages dans la barre avec les effets et leur description
     */
    public Group affichagePersonnageBarre(int i) {
        Group group = new Group();
        imageperson.setFitHeight(130);
        imageperson.setFitWidth(100);
        imageperson.setY(Screen.getPrimary().getVisualBounds().getHeight() - 160);
        imageperson.setX(200 + i * (imageperson.getFitWidth() + 50));

        // Création des intérations avec les images dans la barre
        imageperson.setOnMouseEntered((mouseEvent) -> {
            group.getChildren().add(this.afficherInfo(imageperson.getX(), imageperson.getY()));
        });
        imageperson.setOnMouseExited((mouseEvent) -> {
            group.getChildren().remove(1);
        });
        imageperson.setOnMouseClicked((mouseEvent) -> {
            Plateau.personneSelectionne = this;
            if (equipeSelectionne != null) {
                imageperson.setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                for (Personne p : barre.getListClasse()) {
                    if (personneSelectionne == p) {
                        p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionne.getCouleur()));
                    } else {
                        p.getImageperson().setEffect(null);
                    }
                }
            }
        });
        group.getChildren().add(imageperson);
        return group;
    }

    /**
     * Fonction permettant de créer la barre d'informations d'un personnage
     * @param X : position en X de la description d'une personne
     * @param Y : position en Y de la description d'une personne
     * @return un groupe contenant les informations et la description d'une personne
     */
    public Group afficherInfo(double X, double Y) {
        Group description = new Group();
        Rectangle barre = new Rectangle(400 , 150, Color.LIGHTGRAY);
        barre.setX(X);
        barre.setY(Y - 150);
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);

        Text title = this.getinfoTitleText();
        title.setStyle("-fx-font-weight: bold");
        title.setX(X + 10);
        title.setY(Y - 130);
        //title.setStyle("-fx-font-style: bold");

        Text descrip = this.getinfoDescText();
        //descrip.getStyleClass().add("textBox");
        descrip.setX(X + 10);
        descrip.setY(Y - 130);

        description.getChildren().add(barre);
        description.getChildren().add(title);
        description.getChildren().add(descrip);
        return description;
    }

    /**
     * Fonctions abstraites devant être présentes dans toutes les classes filles
     */
    public abstract Personne personneNouvelle(Equipe team, Case position);
    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();
    public abstract Image getImageFace();

    /**
     * Méthode permettant d'afficher l'image de la personne
     * @return un groupe contenant son image
     */
    public Group affichagePersonnage() {
        Group group = new Group();
        group.getChildren().add(this.afficherImageFace());
        return group;
    }

    /**
     * Fonction permettant d'afficher le nom de la personne
     * @return un groupe contenant son nom avec la couleur de son équipe
     */
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

    /**
     * Fonction permettant d'afficher l'image d'une personne et ajoute la possibilité de le supprimer en cliquant dessus hors partie lancée (!statusPartie)
     * @return un groupe contenant l'image de la personne
     */
    public Group afficherImageFace() {
        ImageView person = new ImageView(this.getImageFace());
        person.setFitHeight(taille/1.5);
        person.setFitWidth(taille/2);
        person.setX(position.getPosX() + taille/3);
        person.setY(position.getPosY() - taille/20);

        Group group = new Group();
        group.getChildren().add(person);

        // Cliquer sur l'image hors partie permet de supprimer la personne
        person.setOnMouseClicked((mouseEvent) -> {
            if (!statusPartie) {
                if (argentPartie != 0) {
                    position.getContenu().get(0).getTeam().setArgent(position.getContenu().get(0).getTeam().getArgent() + this.getCost());
                }
                selfDelete();
            }
        });

        // Affichage des case voisines en fonction de la portée de déplacement
        if (!statusPartie) {
            person.setOnMouseEntered((mouseEvent) -> {
                position.afficherCaseVoisines(speed, true);
            });

            person.setOnMouseExited((mouseEvent) -> {
                position.afficherCaseVoisines(speed, false);
            });
        }

        return group;
    }

    /**
     * Méthode permettant d'afficher le nom et la santé des personnes sur le plateau si pas affichés
     */
    public void afficherSanteEtNom() {
        SanteNom.getChildren().clear();
        SanteNom.getChildren().addAll(afficherSante(), afficherNom());
        if (barreVisible) {
            supprimerSanteEtNom();
            group.getChildren().add(SanteNom);
        } else if(!barreVisible && group.getChildren().contains(SanteNom)) {
            supprimerSanteEtNom();
        }
    }

    /**
     * Méthode permettant d'enlever le nom et la santé des personnes de l'affichage du plateau si affichés
     */
    public void supprimerSanteEtNom() {
        if (group.getChildren().contains(SanteNom)) {
            group.getChildren().remove(SanteNom);
        }
    }

    /**
     * Méthode permettant à une personne de disparaître complétement (vider sa case, l'enlever des listes le contenant ...)
     */
    public void selfDelete() {
        this.position.seVider();
        SanteNom.getChildren().clear();
        this.getTeam().getTeam().remove(this);
        personnages.remove(this);
    }

    /**
     * Fonction qui crée la barre de vie de chaque personne en fonction de sa vie restant en pourcentage
     * @return un groupe contenant la barre de vie pour une personne
     */
    public Group afficherSante() {
        Rectangle barre = new Rectangle(taille, taille/10, Color.BLACK);
        Rectangle vie = new Rectangle(taille - 4, taille/10 - 4, Color.RED);

        barre.setX(getPosition().getPosX());
        barre.setY(getPosition().getPosY() + taille/2.2);

        vie.setY(barre.getY() + 2);
        vie.setX(barre.getX() + 2);

        double percentage = (this.getHealth() / maxHealth);
        double width = (percentage * (taille - 4));
        vie.widthProperty().setValue(width);

        Group group = new Group();
        group.getChildren().add(barre);
        group.getChildren().add(vie);

        return group;
    }

    /**
     * Fontion qui permet d'obtenin un nom aléatoire parmi ceux dans la liste des noms disponibles
     * @return un nom aléatoire pour la personne
     */
    public static String getRandomName() {
        if (listName.isEmpty()) {
            ajouteNom();
        }
        return listName.get(new Random().nextInt(listName.size()));
    }

    /**
     * Méthode permettant d'ajouter dans la liste des nom les noms écris dans "noms.txt"
     */
    private static void ajouteNom() {
        Scanner scan = new Scanner(Personne.class.getResourceAsStream("/Texts/noms.txt"));
        int ligne = 1;
        while (scan.hasNextLine()) {
            String nom = scan.nextLine();
            listName.add(nom);
            ligne++;
        }
        scan.close();
    }

    /**
     * Getters et setters divers
     */
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

    public int getRange() {
        return range;
    }

    public int getSpeed() {
        return speed;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void getAlteration() {
        if (this.position.getAlteration() != null) {
            this.position.getAlteration().appliquerEffet(this);
        }
    }

    /**
     * Fonction qui permet d'obtenir l'équipe adverse à this
     * @return
     */
    public Equipe getOtherTeam() {
        if (e1.equals(this.team)) {
            return e2;
        } else {
            return e1;
        }
    }

    public ImageView getImageperson() {
        return imageperson;
    }

    public void setImageperson(ImageView imageperson) {
        this.imageperson = imageperson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getName() + " : " + this.getHealth() + "\n";
    }
}