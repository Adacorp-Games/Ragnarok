package outerhaven.Personnages;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import outerhaven.Case;
import outerhaven.Equipe;
import outerhaven.Interface.Effets;
import outerhaven.Personnages.Invocations.Mort;
import outerhaven.Plateau;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import static outerhaven.Plateau.*;

public abstract class Personne {
    private String name; // Stocké dans un tableau.
    private static ArrayList<String> listName = new ArrayList<>();
    private Group SanteNom = new Group();
    private double health;
    private double maxHealth;
    private double armor;
    private double cost;
    protected int damage;
    protected int range; // Portée d'attaque en nombre de case.
    private int speed; // Nombre de case qu'il parcourt chaque tour.
    private Equipe team;
    public static boolean barreVisible = false;
    private ImageView imageperson = new ImageView(this.getImageFace());
    private Case position;

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

    //Fonction qui permet d'attaquer le personnage mit en parametre s'il est d'une équipe différente

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
    // Fonction qui permet de se déplacer dans une case mis en parametre et vidant la case précédente

    public void déplacer(Case c) {
        Case casePrecedente = this.position;
        this.position = c;
        c.rentrePersonnage(this);
        casePrecedente.seVider();
        if (Personne.barreVisible) {
            this.afficherSanteEtNom();
        }
    }
    // Action basique de toute personne en fonction de la distance avec sa cible la plus proche : attaquer ou se déplacer

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
                    déplacer(pathToEnnemy.get(speed));
                }
                // System.out.println("Vie restante de la cible " + getHealth());
            }
        }
    }
    // Méthode pour ralentir certaines méthodes

    public void waitTEST(long nb) {
        long i = 0;
        for (long j = 0; j < nb; j++) {
            i = j -i + 5;
        }
    }
    // Fonction qui permet d'afficher la barre des personnages selectionables et qui implémente les fonctionnalités liées à la séléction des personnages

    public Group affichagePersonnageBarre(int i) {
        Group group = new Group();

        imageperson.setFitHeight(130);
        imageperson.setFitWidth(100);
        imageperson.setY(Screen.getPrimary().getVisualBounds().getHeight() - 160);
        imageperson.setX(200 + i * (imageperson.getFitWidth() + 50));
        imageperson.setOnMouseEntered((mouseEvent) -> {
            group.getChildren().add(this.afficherInfo(imageperson.getX(), imageperson.getY()));
        });
        imageperson.setOnMouseExited((mouseEvent) -> {
            group.getChildren().remove(1);
        });
        imageperson.setOnMouseClicked((mouseEvent) -> {
            //if (Plateau.personneSelectionné == null) {
                Plateau.personneSelectionné = this;
                if (equipeSelectionné != null) {
                    imageperson.setEffect(new Effets().putInnerShadow(equipeSelectionné.getCouleur()));
                    for (Personne p : barre.getListClasse()) {
                        if (personneSelectionné == p) {
                            p.getImageperson().setEffect(new Effets().putInnerShadow(equipeSelectionné.getCouleur()));
                        } else {
                            p.getImageperson().setEffect(null);
                        }
                    }
                }
            /*} else {
                for (Personne p : barre.getListClasse()) {
                    p.getImageperson().setEffect(null);
                }
                Plateau.personneSelectionné = null;
            }*/
        });
        group.getChildren().add(imageperson);
        return group;
    }
    // Fonction permettant de créer la barre d'informations d'un personnage

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
        title.setStyle("-fx-font-weight: bold");
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

    // Méthodes personnalisées en fonction du personnage
    public abstract Personne personneNouvelle(Equipe team, Case position);
    public abstract Text getinfoTitleText();
    public abstract Text getinfoDescText();
    public abstract Image getImageFace();

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
            if (!statusPartie) {
                if (argentPartie != 0) {
                    position.getContenu().get(0).getTeam().setArgent(position.getContenu().get(0).getTeam().getArgent() + this.getCost());
                }
                selfDelete();
            }
        });

        // Affichage des case voisines en fonction de la portée de déplacement
        person.setOnMouseEntered((mouseEvent) -> {
            position.afficherCaseVoisines(speed, true);
        });

        person.setOnMouseExited((mouseEvent) -> {
            position.afficherCaseVoisines(speed, false);
        });

        //}

        return group;
    }
    // Affichage santé et nom

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

    public void supprimerSanteEtNom() {
        if (group.getChildren().contains(SanteNom)) {
            group.getChildren().remove(SanteNom);
        }
    }
    // Fonction qui permet à un personnage de s'auto détruire

    public void selfDelete() {
        this.position.seVider();
        SanteNom.getChildren().clear();
        this.getTeam().getTeam().remove(this);
        personnages.remove(this);
    }
    // Fonction qui permet de créer la barre de vie d'un personnage

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
    // Gestion des nom aléatoires pour les personnages

    public static String getRandomName() {
        if (listName.isEmpty()) {
            ajouteNom();
        }
        return listName.get(new Random().nextInt(listName.size()));
    }

    private static void ajouteNom() {
        // File noms = new File("/text/noms.txt");
        Scanner scan = new Scanner(Personne.class.getResourceAsStream("/text/noms.txt"));
        int ligne = 1;
        while (scan.hasNextLine()) {
            String nom = scan.nextLine();
            listName.add(nom);
            ligne++;
        }
        scan.close();
    }

    // Getter et setter

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

    @Override
    public String toString() {
        return this.getName() + " : " + this.getHealth() + "\n";
    }
}