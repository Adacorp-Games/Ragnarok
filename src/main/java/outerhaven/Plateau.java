package outerhaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import outerhaven.Interface.BarrePersonnage;
import outerhaven.Interface.Bouton;
import outerhaven.Interface.Effets;
import outerhaven.Personnages.PersonnagesMagiques.Archimage;
import outerhaven.Personnages.Personne;
import java.util.ArrayList;
import java.util.Collections;

/**
 * La classe Plateau va principalement generer le plateau et son interface, ainsi que les parametres necessaire au fonctionnement du jeu et au interaction
 */

public class Plateau {
    /**
     * Information javaFX utile pour le fonctionnement du jeu
     */
    private static Stage primary;
    public static Group group = new Group();
    public static Scene scene = new Scene(group);
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    /**
     * Aire du plateau && taille en px d'une case
     */
    private int aire;
    public static double taille;
    /**
     * Liste de personnes presentes sur le plateau
     */
    public static ArrayList<Personne> personnages = new ArrayList<>();
    /**
     * Liste de personnes mortes durant un combat
     */
    public static ArrayList<Personne> morts = new ArrayList<>();
    /**
     * Liste d'invocation en attente pour les futurs innuté à ajouté pour le prochain tour
     */
    public static ArrayList<Personne> invocationAttente = new ArrayList<>();
    /**
     * Liste/tableau des cases (alterées ou non) dans le plateau
     */
    public static ArrayList<Case> listeCase = new ArrayList<>();
    public static ArrayList<Case> listeCaseAlterees = new ArrayList<>();
    public static Case[][] tableauCase;
    /**
     * Les différentes équipes dans le jeu
     */
    public static Equipe e1 = new Equipe(Color.RED);
    public static Equipe e2 = new Equipe(Color.BLUE);
    /**
     * Informations concernants une partie en cours
     */
    public static int argentPartie = 0;
    public static int temps = 500;
    public static int nbTour = 0;
    /**
     * Status de certains parametre utiles pour le fonctionnement du jeu
     */
    public static Personne personneSelectionne;
    public static Equipe equipeSelectionne;
    public static boolean statusPartie = false;
    public static boolean activerAnimation = false;
    /**
     * Interface utile au plateau à mettre à jour durant une partie
     */
    public static BarrePersonnage barre = new BarrePersonnage();
    private static Group nbPersonne = new Group();

    public Plateau(Stage primary) {
        // Le contructeur n'as besoin que de la fenetre du main pour se lancer
        Plateau.primary = primary;
    }

    public void lancerPartie() {
        // Lance une partie en ajoutant la scene et le groupe dans la fentre principale
        interfaceDebut();
        primary.setScene(scene);
        primary.show();
    }

    /**
     * lancerScenePlateau() genere le plateau d'hexagone en fonction de :
     * - l'aire
     * - la taille de l'ecran de l'utilisateur
     * La methode auras aussi pour but d'incorporer les Cases creent dans listeCase et tableauCase en leur donnant des coordoné x et y
     * Elle ajoute aussi des interface en plus comme le menus, ses boutons d'interactions, l'argent du joueur et le nb de personnage dans chaque equipes
     */
    public void lancerScenePlateau() {
        // Ajuste la taille d'une case et le taille du tableau case
        taille = 1000/Math.sqrt(aire);
        tableauCase = new Case[(int) Math.sqrt(aire) + 1][(int) Math.sqrt(aire) + 2];
        // Les hexagones se chevauchent par ligne, le but de se boolean est de decaler chaque ligne pour permettre ce chevauchement
        boolean decalage = false;
        int i = 0;
        int ligne = 0;

        while (i < aire) {
            // On entre dans une ligne
            if (!decalage) {
                double posY = largeurMax/2 - (taille * Math.sqrt(aire)/2) + ligne * taille - taille * ligne/4;
                for (int j = 0; j < Math.sqrt(aire); j++) {
                    // On definie les cases d'une ligne
                    double posX = longeurMax/2 - (taille * (Math.sqrt(aire)) / 2) + j * taille;
                    Case hexago = new Case(ligne, j - (ligne/2));
                    // Ajout de la case dans une liste, tableau et groupe (pour qu'elle s'affiche)
                    tableauCase[ligne][j] = hexago;
                    listeCase.add(hexago);
                    group.getChildren().add(hexago.afficherCase(posX, posY, taille));
                    i++;
                }
                decalage = true;
                ligne++;
            } else {
                double posY = largeurMax/2 - (taille * Math.sqrt(aire)/2) + ligne * taille - taille * ligne/4;
                for (int j = 0; j < Math.sqrt(aire)+1 ; j++) {
                    // On definie les cases d'une ligne
                    double posX = longeurMax/2 - (taille * (Math.sqrt(aire)) / 2) + j * taille - taille/2;
                    Case hexago = new Case(ligne, j - ((ligne)/2 + 1));
                    // Ajout de la case dans une liste, tableau et groupe (pour qu'elle s'affiche)
                    tableauCase[ligne][j] = hexago;
                    listeCase.add(hexago);
                    group.getChildren().add(hexago.afficherCase(posX, posY, taille));
                    i++;
                }
                decalage = false;
                ligne++;
            }
        }
        // Initialisation des cases voisines
        initVoisins();
        // Creation et incorporation d'une slide barre + boutton
        ajouteLeMenu();
        // Creation et incorporation d'information sur les équipes
        afficherNbPersonne();
        // On affiche l'argent si elle n'est pas infini (rien d'écrit)
        if (argentPartie > 0) {
            getE1().setArgent(argentPartie);
            getE2().setArgent(argentPartie);
            group.getChildren().add(barre.getArgentGroup());
        }
        // On ajoute toutes les interfaces
        group.getChildren().add(nbPersonne);
        group.getChildren().add(boutonPausePlay());
        group.getChildren().add(barre.returnBarre());
        primary.setScene(scene);
    }

    /**
     * Méthode permettant à chaque case de connaitre ses cases voisines
     */
    public void initVoisins() {
        for (Case c : listeCase) {
            c.trouverVoisin();
        }
    }

    /**
     * Méthode permettant d'afficher le nombre de personnes contenus dans chaque équipe
     */
    public static void afficherNbPersonne() {
        Rectangle barre = new Rectangle(200 , 60, Color.LIGHTGRAY);
        barre.setX(10);
        barre.setY(400);
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);

        Text title = new Text("Nombre de personne par équipe");
        title.setX(barre.getX() + 23);
        title.setY(barre.getY() + 15);

        Text equipes = new Text( "Equipe 1 : " + getE1().getNbPersonne() + "\n" + "Equipe 2 : " + getE2().getNbPersonne());
        equipes.setX(title.getX());
        equipes.setY(title.getY() + 20);

        nbPersonne.getChildren().add(barre);
        nbPersonne.getChildren().add(title);
        nbPersonne.getChildren().add(equipes);
    }

    /**
     * Méthode pour update les compteurs de afficherNbPersonne()
     */
    public static void updateNbPersonne() {
        nbPersonne.getChildren().clear();
        afficherNbPersonne();
    }

    /**
     * Interface avant la generation du plateau
     * on ajoute ici les boutons :
     * Start (pour lancer la partie)
     * Quitter (pour arreter le jeu)
     * Une entrée pour l'aire du plateau
     * Une entrée pour l'argent des equipes
     * (avec tout les texte qui vont avec)
     */
    private void interfaceDebut() {
        Button start = new Button("START");
        start.setLayoutX((longeurMax-700)/2);
        start.setLayoutY((largeurMax-200)/2);
        start.setMinSize(700,200);
        start.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold;-fx-font-size: 60");
        start.setOnMouseEntered(mouseEvent -> start.setEffect(new Effets().putInnerShadow(Color.ORANGE)));
        start.setOnMouseExited(mouseEvent -> start.setEffect(null));

        // Ajout de textes
        Text infoNB = new Text("Entrez le nombre de cases du plateau :");
        infoNB.setLayoutX((longeurMax-700)/2);
        infoNB.setLayoutY((largeurMax-300)/2-20);

        Text infoArgent = new Text("Entrez la limite d'argent pour chaque équipe : (vide = pas de limite)");
        infoArgent.setLayoutX((longeurMax-700)/2);
        infoArgent.setLayoutY((largeurMax-450)/2-20);

        // Entrée de l'aire du plateau
        TextField nbCase = new TextField();
        nbCase.setLayoutX((longeurMax-700)/2);
        nbCase.setLayoutY((largeurMax-280)/2-20);
        nbCase.setMinSize(100,50);
        nbCase.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");

        // Entrée de l'argent
        TextField nbArgent = new TextField();
        nbArgent.setLayoutX((longeurMax-700)/2);
        nbArgent.setLayoutY((largeurMax-430)/2-20);
        nbArgent.setMinSize(100,50);
        nbArgent.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");

        // Demande l'utilisation des animations
        Button animationBT = new Button("Animations : NON");
        animationBT.setMinSize(120,50);
        animationBT.setLayoutX((longeurMax - 700) / 2);
        animationBT.setLayoutY((largeurMax + 220) / 2);
        animationBT.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        animationBT.setOnMouseClicked(mouseEvent -> {
            if (!activerAnimation) {
                activerAnimation = true;
                animationBT.setEffect(new Effets().putInnerShadow(Color.BLACK));
                animationBT.setText("Animations : OUI");
            } else {
                activerAnimation = false;
                animationBT.setEffect(null);
                animationBT.setText("Animations : NON");
            }
        });

        nbCase.setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                aire = getIntFromTextField(nbCase);
                if(nbArgent.getText().length() > 0) {
                    argentPartie = getIntFromTextField(nbArgent);
                }
                if (aire > 0) {
                    group.getChildren().clear();
                    lancerScenePlateau();
                }
            }
        });

        nbArgent.setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                aire = getIntFromTextField(nbCase);
                if(nbArgent.getText().length() > 0) {
                    argentPartie = getIntFromTextField(nbArgent);
                }
                if (aire > 0) {
                    group.getChildren().clear();
                    lancerScenePlateau();
                }
            }
        });

        // Configuration du bouton start
        start.setOnMouseClicked(mouseEvent -> {
            aire = getIntFromTextField(nbCase);
            if(nbArgent.getText().length() > 0) {
                argentPartie = getIntFromTextField(nbArgent);
            }
            if (aire > 0) {
                group.getChildren().clear();
                lancerScenePlateau();
            }
        });
        Button quitter = boutonExit();
        quitter.setLayoutY(10);

        //ajout de toute ces interfaces dans le group
        group.getChildren().addAll(animationBT, infoNB, nbCase, infoArgent, nbArgent, start, quitter);
        /*group.getChildren().add(animationCB);
        group.getChildren().add(infoNB);
        group.getChildren().add(nbCase);
        group.getChildren().add(infoArgent);
        group.getChildren().add(nbArgent);
        group.getChildren().add(start);
        group.getChildren().add(quitter);*/
    }

    /**
     * Fonction transformant un String en entier
     * @param textField est le texte qu'on cherche à transformer en entier
     * @return le texte en int s'il contient que des entiers
     */
    public static int getIntFromTextField(TextField textField) {
        String text = textField.getText();
        return Integer.parseInt(text);
    }

    /**
     * equipeSelectionne devient l'équipe en parametre
     */
    public static void incorporeEquipe(Equipe equipe) {
        equipeSelectionne = equipe;
    }

    /**
    * Cette section contiendras les boutons du Plateau, on retrouveras le systeme de tour plus tard
    */

    /**
     * Crée un bouton Exit
     * @return le bouton Exit
     */
    private Button boutonExit() {
        Button exit = new Bouton().creerBouton("Quitter");
        exit.setLayoutX(10);
        exit.setLayoutY(190);
        exit.setOnMouseClicked(mouseEvent -> primary.close());
        return exit;
    }

    /**
     * Crée un bouton Reset:
     * Relance le jeu une nouvelle aire
     * @return le bouton Reset
     */
    private Button boutonReset() {
        Button reset = new Bouton().creerBouton("Nouvelle partie");
        reset.setLayoutX(10);
        reset.setLayoutY(130);
        reset.setOnMouseClicked(mouseEvent -> {
            setStatusPartie(false);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300), ev -> {
                group.getChildren().clear();
                personnages.clear();
                morts.clear();
                listeCase.clear();
                invocationAttente.clear();
                getE1().getTeam().clear();
                getE2().getTeam().clear();
                scene.setFill(Color.WHITE);
                this.lancerPartie();
                aire = 0;
                argentPartie = 0;
            }));
            timeline.play();
            //Plateau.personneSelectionné = null;
        });
        return reset;
    }

    /**
     * Crée un bouton ReStart:
     * Relance le meme plateau
     * @return le bouton ReStart
     */
    private Button boutonReStart() {
        Button reStrat = new Bouton().creerBouton("Restart");
        reStrat.setLayoutX(10);
        reStrat.setLayoutY(70);
        reStrat.setOnMouseClicked(mouseEvent -> {
            setStatusPartie(false);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300), ev -> {
                group.getChildren().remove(0, group.getChildren().size());
                personnages.clear();
                morts.clear();
                listeCase.clear();
                invocationAttente.clear();
                getE1().getTeam().clear();
                getE2().getTeam().clear();
                scene.setFill(Color.WHITE);
                lancerScenePlateau();
            }));
            timeline.play();
            //Plateau.personneSelectionné = null;
        });
        return reStrat;
    }

    /**
     * Crée un bouton PausePlay:
     * gere l'etat et l'avancement du jeu
     * @return un groupe contenant les boutons Play et Pause
     */
    private Group boutonPausePlay() {
        Group boutonGame = new Group();
        Label labelPlay = new Label("");
        labelPlay.setLayoutY(670);
        Label labelPause = new Label("");
        labelPause.setText("La partie est en pause");
        labelPause.setLayoutY(650);
        Button pause = new Bouton().creerBouton("Pause");
        Button play = new Bouton().creerBouton("Play");
        play.setLayoutX(140);
        play.setLayoutY(10);
        play.setOnMouseClicked(mouseEvent -> {
            //labelPlay.setText("La partie reprend");
            if (!e1.getTeam().isEmpty() && !e2.getTeam().isEmpty()) {
                boutonGame.getChildren().remove(labelPause);
                boutonGame.getChildren().remove(play);
                boutonGame.getChildren().add(pause);
                setStatusPartie(true);
                if (group.getChildren().contains(barre.returnBarre())) {
                    group.getChildren().remove(barre.returnBarre());
                    //boutonEquipe().getChildren().clear();
                    scene.setFill(Color.DARKGRAY);
                    Plateau.scene.setCursor(Cursor.DEFAULT);
                    //Plateau.personneSelectionné = null;
                    //Plateau.incorporeEquipe(null);
                }
                tour();
            } else if (!personnages.isEmpty() && (e1.getTeam().isEmpty() || e2.getTeam().isEmpty())) {
                Text attention = new Text("Il n'y qu'une seule equipe sur le terrain");
                attention.setY(650);
                attention.setX(20);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                Plateau.group.getChildren().add(attention);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> Plateau.group.getChildren().remove(attention)));
                timeline.play();
            } else {
                Text attention = new Text("Veuillez remplir les hexagones avec des personnages");
                attention.setY(650);
                attention.setX(20);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                Plateau.group.getChildren().add(attention);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> Plateau.group.getChildren().remove(attention)));
                timeline.play();
            }
        });

        pause.setLayoutX(140);
        pause.setLayoutY(10);
        pause.setOnMouseClicked(mouseEvent -> {
            boutonGame.getChildren().add(labelPause);
            boutonGame.getChildren().remove(pause);
            boutonGame.getChildren().add(play);
            setStatusPartie(false);
            if (!group.getChildren().contains(barre.returnBarre())) {
                group.getChildren().add(barre.returnBarre());
                //group.getChildren().add(boutonEquipe());
                scene.setFill(Color.WHITE);
            }
        });
        boutonGame.getChildren().add(play);
        boutonGame.getChildren().add(labelPlay);
        return boutonGame;
    }

    /**
     * Crée des boutons modifiants la vitesse d'execution d'un tour en millisecondes
     * @return les boutons de vitesse
     */
    private Button vitesseX1() {
        Button vitesseX1 = new Bouton().creerBouton("x1");
        vitesseX1.setLayoutX(270);
        vitesseX1.setLayoutY(70);
        vitesseX1.setEffect(new Effets().putInnerShadow(Color.BLACK));
        return vitesseX1;
    }

    private Button vitesseX2() {
        Button vitesseX2 = new Bouton().creerBouton("x2");
        vitesseX2.setLayoutX(270);
        vitesseX2.setLayoutY(vitesseX1().getLayoutY()+60);
        return vitesseX2;
    }

    private Button vitesseX3() {
        Button vitesseX3 = new Bouton().creerBouton("x3");
        vitesseX3.setLayoutX(270);
        vitesseX3.setLayoutY(vitesseX2().getLayoutY()+60);
        return vitesseX3;
    }

    /**
     * Crée un bouton affichant la vie et le nom des personnages
     */
    private void afficheBarVie() {
        Button barVie = new Bouton().creerBouton("Afficher barres de vie");
        barVie.setLayoutX(longeurMax - 150);
        barVie.setLayoutY(10);
        barVie.setOnMouseClicked(mouseEvent -> {
            if (!personnages.isEmpty()){
                Personne.barreVisible = !Personne.barreVisible;
                for (Personne personnage : personnages) {
                    personnage.afficherSanteEtNom();
                }
            }
        });
        group.getChildren().add(barVie);
    }

    /**
     * Menu contenant tout les boutons précédants et l'ajout au groupe général
     */
    private void ajouteLeMenu() {
        Button menu = new Bouton().creerBouton("Menu");
        menu.setLayoutX(10);
        menu.setLayoutY(10);
        Button exit = boutonExit();
        Button reset = boutonReset();
        Button reStart = boutonReStart();
        menu.setOnMouseClicked(mouseEvent -> {
            if (!group.getChildren().contains(exit)) {
                try {
                    group.getChildren().add(reset);
                    group.getChildren().add(reStart);
                    group.getChildren().add(exit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    group.getChildren().remove(reset);
                    group.getChildren().remove(reStart);
                    group.getChildren().remove(exit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button vitesse = new Bouton().creerBouton("Vitesse");
        vitesse.setLayoutX(270);
        vitesse.setLayoutY(10);
        Button x1 = vitesseX1();
        Button x2 = vitesseX2();
        Button x3 = vitesseX3();
        x1.setOnMouseClicked(mouseEvent-> {
            temps = 500;
            x1.setEffect(new Effets().putInnerShadow(Color.BLACK));
            x2.setEffect(null);
            x3.setEffect(null);
        });
        x2.setOnMouseClicked(mouseEvent-> {
            temps = 250;
            x2.setEffect(new Effets().putInnerShadow(Color.BLACK));
            x1.setEffect(null);
            x3.setEffect(null);
        });
        x3.setOnMouseClicked(mouseEvent-> {
            temps = 166;
            x3.setEffect(new Effets().putInnerShadow(Color.BLACK));
            x1.setEffect(null);
            x2.setEffect(null);
        });
        vitesse.setOnMouseClicked(mouseEvent-> {
            if (!group.getChildren().contains(x1)) {
                try {
                    group.getChildren().add(x1);
                    group.getChildren().add(x2);
                    group.getChildren().add(x3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    group.getChildren().remove(x1);
                    group.getChildren().remove(x2);
                    group.getChildren().remove(x3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        group.getChildren().add(vitesse);
        boutonPausePlay();
        afficheBarVie();
        group.getChildren().add(menu);
    }

    /**
     * Lance un tour : fait combattre les personnages en fonction d'un certain temps d'attente (vitesse) jusqu'à ce qu'il ne reste qu'une équipe ou
     * que l'utilisateur mette pause
     */
    public void tour() {
        if(!e1.getTeam().isEmpty() && !e2.getTeam().isEmpty() && statusPartie) {
            nbTour++;
            System.out.println("Tour : " + nbTour);

            // Gestion des invocations
            personnages.addAll(invocationAttente);
            invocationAttente.clear();

            // Gestion des cases altérées
            if (!listeCaseAlterees.isEmpty()) {
                for (Case c : listeCaseAlterees) {
                    if (c.getAlteration() != null) {
                        c.getAlteration().passeTour();
                    } else {
                        Alteration.AlterSupr.add(c);
                    }
                }
                Alteration.nettoiCaseAlter();
            }

            // Gestion de l'affichage de barres de vie
            if (Personne.barreVisible && !personnages.isEmpty()) {
                for (Personne personnage : personnages) {
                    personnage.afficherSanteEtNom();
                }
            }

            // Gestion de l'ordre d'action des personnages
            Collections.shuffle(personnages);
            // Fait combattre les peronnages non contenu dans mort
            for (Personne personnage : personnages) {
                if (personnage.getHealth() <= 0) {
                    morts.add(personnage);
                }
                if (!morts.contains(personnage)) {
                    personnage.getAlteration();
                    if (personnage.getPosition().getAlteration() != null) {
                        if (personnage.getPosition().getAlteration().getEffet() == "freeze" && personnage.getClass() == Archimage.class) {
                            personnage.action();
                        } else if (personnage.getPosition().getAlteration().getEffet() != "freeze") {
                            personnage.action();
                        }
                    } else {
                        personnage.action();
                    }
                }
            }

            // Gestion des morts
            for (Personne p : morts) {
                if (argentPartie > 0) {
                    p.getOtherTeam().setArgent(p.getOtherTeam().getArgent() + 50);
                }
                p.selfDelete();
                p.getTeam().setNbPersonne();
            }
            System.out.println("Nombre de morts durant ce tour : " + morts.size());
            System.out.println("Equipe 1 : " + e1.getTeam().size() + " | Equipe 2 : " + e2.getTeam().size());
            System.out.println("Nombre de personnages sur le plateau : " + personnages.size());
            morts.clear();

            // Relance le prochain tour (dans un certain temps --> vitesse)
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(temps), ev -> tour()));
            timeline.play();

            // Change l'interface car nous somme en jeu
            if (e1.getTeam().isEmpty() || e2.getTeam().isEmpty()) {
                setStatusPartie(false);
                scene.setFill(Color.WHITE);
                group.getChildren().remove(boutonPausePlay());
                group.getChildren().add(boutonPausePlay());
                group.getChildren().add(barre.returnBarre());
                morts.clear();
                personnages.addAll(invocationAttente);
            }
            if (argentPartie > 0) {
                getE1().setArgent(getE1().getArgent() + 25);
                getE2().setArgent(getE2().getArgent() + 25);
            }

            // Mise à jours du nombre de personne sur le plateau
            afficherNbPersonne();
        }
    }

    /**
     * cette section contient tout les getters et setters de Plateau
     */
    public static boolean getStatusPartie() {
        return statusPartie;
    }

    public static Group getNbPersonne() {
        return nbPersonne;
    }

    public static boolean isActiverAnimation() {
        return activerAnimation;
    }

    public static void setStatusPartie(boolean statusPartie) {
        Plateau.statusPartie = statusPartie;
    }

    public static Equipe getE1() {
        return e1;
    }

    public static Equipe getE2() {
        return e2;
    }
}
