package outerhaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import outerhaven.Personnages.Personne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Plateau {
    private int aire;
    public static ArrayList<Personne> personnages = new ArrayList<>();
    public static ArrayList<Personne> morts = new ArrayList<>();
    public static ArrayList<Personne> invocationAttente = new ArrayList<>();
    public static ArrayList<Case> listeCase = new ArrayList<>();
    public static ArrayList<Case> listeCaseAltérées = new ArrayList<>();
    private static Stage primary;
    public static double taille;
    public static int argentPartie = 0;
    public static Personne personneSelectionné;
    public static Equipe equipeSelectionné;
    public static Group group = new Group();
    public static Scene scene = new Scene(group);
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    public static boolean statusPartie = false;
    private static boolean stoptimeline = true;
    public static BarrePersonnage barre = new BarrePersonnage();
    public static Equipe e1 = new Equipe(Color.RED);
    public static Equipe e2 = new Equipe(Color.BLUE);
    public static Case[][] tableauCase;
    private static int temps = 500;
    public static int nbTour = 0;

    public Plateau(Stage primary) {
        Plateau.primary = primary;
    }

    public void lancerPartie() {
        interfaceDebut();
        primary.setScene(scene);
        primary.show();
    }

    public void lancerScenePlateau() {
        taille = 1000/Math.sqrt(aire);
        tableauCase = new Case[(int) Math.sqrt(aire) + 1][(int) Math.sqrt(aire) + 2];
        boolean decalage = false;
        int i = 0;
        int ligne = 0;

        while (i < aire) {
            if (!decalage) {
                double posY = largeurMax/2 - (taille * Math.sqrt(aire)/2) + ligne * taille - taille * ligne/4;
                decalage = true;
                for (int j = 0; j < Math.sqrt(aire); j++) {
                    double posX = longeurMax/2 - (taille * (Math.sqrt(aire)) / 2) + j * taille;
                    Case hexago = new Case(ligne, j - (ligne/2));
                    tableauCase[ligne][j] = hexago;
                    group.getChildren().add(hexago.afficherCase(posX, posY, taille));
                    listeCase.add(hexago);
                    i++;
                }
                ligne++;
            } else {
                double posY = largeurMax/2 - (taille * Math.sqrt(aire)/2) + ligne * taille - taille * ligne/4;
                decalage = false;
                for (int j = 0; j < Math.sqrt(aire)+1 ; j++) {
                    double posX = longeurMax/2 - (taille * (Math.sqrt(aire)) / 2) + j * taille - taille/2;
                    Case hexago = new Case(ligne, j - ((ligne)/2 + 1));
                    tableauCase[ligne][j] = hexago;
                    group.getChildren().add(hexago.afficherCase(posX, posY, taille));
                    listeCase.add(hexago);
                    i++;
                }
                ligne++;
            }
        }

        // Initialisation des cases voisines
        initVoisins();

        // Creation et incorporation d'une slide barre + boutton
        ajouteLeMenu();

        // Afficher les infos des équipes (pas au point encore)
        //group.getChildren().add(afficherInfosEquipes());

        // Tests argent
        /*System.out.println(argentPartie);
        System.out.println(aire);*/

        if (argentPartie > 0) {
            getE1().setArgent(argentPartie);
            getE2().setArgent(argentPartie);
            group.getChildren().add(barre.getArgentGroup());
        }

        /*System.out.println(getE1().getArgent());
        System.out.println(getE2().getArgent());*/

        group.getChildren().add(boutonPausePlay());
        group.getChildren().add(barre.returnBarre());
        primary.setScene(scene);
    }

    private void interfaceDebut() {
        Button start = new Button("START");
        start.setLayoutX((longeurMax-700)/2);
        start.setLayoutY((largeurMax-200)/2);
        start.setMinSize(700,200);
        start.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold;-fx-font-size: 60");
        start.setOnMouseEntered(mouseEvent -> {
            start.setEffect(new Effets().putInnerShadow(Color.ORANGE));
        });
        start.setOnMouseExited(mouseEvent -> {
            start.setEffect(null);
        });

        Text infoNB = new Text("Entrez le nombre de cases du plateau :");
        infoNB.setLayoutX((longeurMax-700)/2);
        infoNB.setLayoutY((largeurMax-300)/2-20);

        Text infoArgent = new Text("Entrez la limite d'argent pour chaque équipe : (vide = pas de limite)");
        infoArgent.setLayoutX((longeurMax-700)/2);
        infoArgent.setLayoutY((largeurMax-450)/2-20);

        TextField nbCase = new TextField();
        nbCase.setLayoutX((longeurMax-700)/2);
        nbCase.setLayoutY((largeurMax-280)/2-20);
        nbCase.setMinSize(100,50);
        nbCase.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");

        TextField nbArgent = new TextField();
        nbArgent.setLayoutX((longeurMax-700)/2);
        nbArgent.setLayoutY((largeurMax-430)/2-20);
        nbArgent.setMinSize(100,50);
        nbArgent.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");

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

        group.getChildren().add(infoNB);
        group.getChildren().add(nbCase);
        group.getChildren().add(infoArgent);
        group.getChildren().add(nbArgent);
        group.getChildren().add(start);
        group.getChildren().add(quitter);
    }

    public static int getIntFromTextField(TextField textField) {
        String text = textField.getText();
        return Integer.parseInt(text);
    }

    public static void incorporeEquipe(Equipe equipe) {
        equipeSelectionné = equipe;
    }

    private Button boutonExit() {
        Button exit = new Bouton().creerBouton("Quitter");
        exit.setLayoutX(10);
        exit.setLayoutY(190);
        exit.setOnMouseClicked(mouseEvent -> primary.close());
        return exit;
    }

    private Button boutonReset() {
        Button reset = new Bouton().creerBouton("Nouvelle partie");
        reset.setLayoutX(10);
        reset.setLayoutY(130);
        reset.setOnMouseClicked(mouseEvent -> {
            group.getChildren().clear();
            personnages.clear();
            morts.clear();
            listeCase.clear();
            invocationAttente.clear();
            getE1().getTeam().clear();
            getE2().getTeam().clear();
            setStatusPartie(false);
            scene.setFill(Color.WHITE);
            this.lancerPartie();
            aire = 0;
            argentPartie = 0;
            Plateau.scene.setCursor(Cursor.DEFAULT);
            Plateau.personneSelectionné = null;
        });
        return reset;
    }

    private Button boutonReStart() {
        Button reStrat = new Bouton().creerBouton("Restart");
        reStrat.setLayoutX(10);
        reStrat.setLayoutY(70);
        reStrat.setOnMouseClicked(mouseEvent -> {
            group.getChildren().remove(0,group.getChildren().size());
            personnages.clear();
            morts.clear();
            listeCase.clear();
            invocationAttente.clear();
            getE1().getTeam().clear();
            getE2().getTeam().clear();
            setStatusPartie(false);
            scene.setFill(Color.WHITE);
            lancerScenePlateau();
            Plateau.scene.setCursor(Cursor.DEFAULT);
            Plateau.personneSelectionné = null;
        });
        return reStrat;
    }

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
                    Plateau.personneSelectionné = null;
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
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> {
                    Plateau.group.getChildren().remove(attention);
                }));
                timeline.play();
            } else {
                Text attention = new Text("Veuillez remplir les hexagones avec des personnages");
                attention.setY(650);
                attention.setX(20);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                Plateau.group.getChildren().add(attention);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> {
                    Plateau.group.getChildren().remove(attention);
                }));
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

    private Button afficheBarVie() {
        Button barVie = new Bouton().creerBouton("Afficher barres de vie");
        barVie.setLayoutX(longeurMax - 150);
        barVie.setLayoutY(10);
        barVie.setOnMouseClicked(mouseEvent -> {
            if (!personnages.isEmpty()){
                Personne.barreVisible = !Personne.barreVisible;
                for (int i = 0; i < personnages.size(); i++) {
                    personnages.get(i).afficherSanteEtNom();
                }
            }
        });
        group.getChildren().add(barVie);
        return barVie;
    }

    public void tour() {
        while (!e1.getTeam().isEmpty() && !e2.getTeam().isEmpty() && statusPartie && stoptimeline) {
            nbTour++;
            System.out.println("Tour : " + nbTour);
            //System.out.println("Nombre de cases altérées : " + listeCaseAltérées.size());

            // Gestion des invocations
            personnages.addAll(invocationAttente);
            invocationAttente.clear();

            if (!listeCaseAltérées.isEmpty()) {
                for (Case c : listeCaseAltérées) {
                    if (c.getAlteration().getTimer() + c.getAlteration().getDurée() <= nbTour) {
                        c.setAlteration(null);
                    }
                }
                for (Case c : listeCaseAltérées) {
                    if (c.getAlteration() == null) {
                        listeCaseAltérées.remove(c);
                    }
                }
            }

            if (Personne.barreVisible && !personnages.isEmpty()) {
                for (int i = 0; i < personnages.size(); i++) {
                    personnages.get(i).afficherSanteEtNom();
                }
            }
            Collections.shuffle(personnages);
            for (Personne p : personnages) {
                p.getAlteration();
                if (p.getHealth() <= 0) {
                    morts.add(p);
                }
                if (!morts.contains(p)) {
                    p.action();
                }
            }

            // Gestion des morts
            for (Personne p : morts) {
                if (argentPartie > 0) {
                    p.getOtherTeam().setArgent(p.getOtherTeam().getArgent() + 50);
                }
                p.selfDelete();
            }
            System.out.println("Nombre de morts durant ce tour : " + morts.size());
            System.out.println("Equipe 1 : " + e1.getTeam().size() + " | Equipe 2 : " + e2.getTeam().size());
            System.out.println("Nombre de personnages sur le plateau : " + personnages.size());
            morts.clear();

            // Mise en pause de la partie & conditions de fin de partie
            stoptimeline = false;
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(temps), ev -> {
                stoptimeline = true;
                tour();
            }));
            timeline.play();
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
                //System.out.println("Equipe 1 : " + e1.getArgent() + "€ | Equipe 2 : " + e2.getArgent() + "€");
            }
        }
    }

    public void initVoisins() {
        for (Case c : listeCase) {
            c.trouverVoisin();
        }
    }

    /*public static Group afficherInfosEquipes() {
        Group description = new Group();
        Rectangle barre = new Rectangle(200 , getE1().getTeam().size()*4 + getE2().getTeam().size()*4 + 200, Color.LIGHTGRAY);
        barre.setX(10);
        barre.setY(100);
        barre.setStroke(Color.BLACK);
        barre.setStrokeWidth(2);

        Text title = new Text("Informations sur les équipes");
        title.setX(barre.getX() + 10);
        title.setY(barre.getY() + 10);
        //title.setStyle("-fx-font-style: bold");

        Text descrip = new Text(getE1().toString() + "\n" + getE2().toString());
        descrip.setX(title.getX());
        descrip.setY(title.getY() + 20);

        description.getChildren().add(barre);
        description.getChildren().add(title);
        description.getChildren().add(descrip);
        return description;
    }*/

    // Getter et setter
    public static boolean getStatusPartie() {
        return statusPartie;
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
