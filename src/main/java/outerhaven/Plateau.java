package outerhaven;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import outerhaven.Interface.BarrePersonnage;
import outerhaven.Interface.Effets;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class Plateau {
    int aire;
    public static ArrayList<Personne> personnages = new ArrayList<>();
    public static ArrayList<Personne> morts = new ArrayList<>();
    public static ArrayList<Case> listeCase = new ArrayList<>();
    public static ArrayList<Equipe> listeEquipe = new ArrayList<>();
    private static Stage primary;
    public static Double taille;
    public static Personne personneSelectionné;
    public static Equipe equipeSelectionné;
    public static Group group = new Group();
    public static Scene scene = new Scene(group);;
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    public static boolean statusPartie = false;
    public static BarrePersonnage barre = new BarrePersonnage();
    public static Equipe e1 = new Equipe(Color.RED);
    public static Equipe e2 = new Equipe(Color.BLUE);
    // Attribut de test

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
        boolean decalage = false;
        int i = 0;
        int ligne = 0;

        while (i < aire) {
            if (!decalage) {
                double posY = largeurMax / 2 - (taille * Math.sqrt(aire)/ 2) + ligne * taille - taille*ligne/4;
                decalage = true;
                for (int j = 0; j < Math.sqrt(aire); j++) {
                    double posX = longeurMax/2 - (taille * (Math.sqrt(aire))/2) + j*taille;
                    Case hexago = new Case(i);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    listeCase.add(hexago);
                    i++;
                }
                    ligne++;
            }
            else {
                double posY = largeurMax / 2 - (taille * Math.sqrt(aire)/2) + ligne * taille - taille * ligne/4;
                decalage = false;
                for (int j = 0; j < Math.sqrt(aire)+1 ; j++) {
                    double posX =longeurMax/2 - (taille*(Math.sqrt(aire))/2)+ j*taille - taille/2 ;
                    Case hexago = new Case(i);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    listeCase.add(hexago);
                    i++;
                }
                ligne++;
            }
        }

        // Creation et incorporation d'une slide barre + boutton
        ajouteLeMenu();
        //group.getChildren().add(boutonEquipe());
        group.getChildren().add(barre.returnBarre());
        primary.setScene(scene);
    }

    private void interfaceDebut() {
        Button start = new Button("START");
        start.setLayoutX((longeurMax-700)/2);
        start.setLayoutY((largeurMax-200)/2);
        start.setMinSize(700,200);
        start.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold;-fx-font-size: 50");
        start.setOnMouseEntered(mouseEvent -> {
            start.setEffect(new Effets().putInnerShadow(Color.ORANGE));
        });
        start.setOnMouseExited(mouseEvent -> {
            start.setEffect(null);
        });

        Text infoNB = new Text("Entrez le nombre de cases du plateau :");
        infoNB.setLayoutX((longeurMax-700)/2);
        infoNB.setLayoutY((largeurMax-300)/2);

        TextField nbCase = new TextField();
        nbCase.setLayoutX((longeurMax-700)/2);
        nbCase.setLayoutY((largeurMax-280)/2);
        nbCase.setMinSize(100,20);
        nbCase.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        start.setOnMouseClicked(mouseEvent -> {
            aire = getIntFromTextField(nbCase);
            if (aire > 0) {
                group.getChildren().clear();
                lancerScenePlateau();
            }
        });
        nbCase.setOnKeyReleased(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                aire = getIntFromTextField(nbCase);
                if (aire > 0) {
                    group.getChildren().clear();
                    lancerScenePlateau();
                }
            }
        });

        Button quitter = boutonExit();
        quitter.setLayoutY(10);

        listeEquipe.add(e1);
        listeEquipe.add(e2);
        group.getChildren().add(infoNB);
        group.getChildren().add(nbCase);
        group.getChildren().add(start);
        group.getChildren().add(quitter);
    }

    public static int getIntFromTextField(TextField textField) {
        String text = textField.getText();
        return Integer.parseInt(text);
    }

    public static void incorporeEquipe(Equipe e1) {
        equipeSelectionné = e1;
    }

    private Button boutonExit() {
        Button exit = new Button("Quitter");
        exit.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        exit.setLayoutX(10);
        exit.setLayoutY(125);
        exit.setMinSize(60,20);
        exit.setOnMouseClicked(mouseEvent -> primary.close());
        return exit;
    }

    private Button boutonReset() {
        Button reset = new Button("Nouvelle grille");
        reset.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        reset.setLayoutX(10);
        reset.setLayoutY(90);
        reset.setMinSize(60,20);
        reset.setOnMouseClicked(mouseEvent -> {
            group.getChildren().clear();
            this.lancerPartie();
        });
        return reset;
    }

    private Button boutonReStart() {
        Button reStrat = new Button("Restart");
        reStrat.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        reStrat.setLayoutX(10);
        reStrat.setLayoutY(55);
        reStrat.setMinSize(60,20);
        reStrat.setOnMouseClicked(mouseEvent -> {
            group.getChildren().remove(0,group.getChildren().size());
            lancerScenePlateau();
        });
        return reStrat;
    }

    private void boutonPausePlay() {
        Label labelPlay = new Label("");
        labelPlay.setLayoutY(670);
        Label labelPause = new Label("");
        labelPause.setText("La partie est en pause");
        labelPause.setLayoutY(650);
        Button pause = new Button("Pause");
        Button play = new Button("Jouer");
        play.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        play.setLayoutX(75);
        play.setLayoutY(20);
        play.setMinSize(60,20);
        play.setOnMouseClicked(mouseEvent -> {
            //labelPlay.setText("La partie reprend");
            if (e1.getTeam().size() > 0 && e2.getTeam().size() > 0){
                group.getChildren().remove(labelPause);
                group.getChildren().remove(play);
                group.getChildren().add(pause);
                //System.out.println(personnages.size());
                setStatusPartie(true);
                if (group.getChildren().contains(barre.returnBarre())) {
                    group.getChildren().remove(barre.returnBarre());
                    //boutonEquipe().getChildren().clear();
                    scene.setFill(Color.DARKGRAY);
                }
                tour();
            }
        });

        pause.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        pause.setLayoutX(75);
        pause.setLayoutY(20);
        pause.setMinSize(60,20);
        pause.setOnMouseClicked(mouseEvent -> {
            group.getChildren().add(labelPause);
            group.getChildren().remove(pause);
            group.getChildren().add(play);
            setStatusPartie(false);
            if (!group.getChildren().contains(barre.returnBarre())) {
                group.getChildren().add(barre.returnBarre());
                //group.getChildren().add(boutonEquipe());
                scene.setFill(Color.WHITE);
            }
        });
        group.getChildren().add(play);
        group.getChildren().add(labelPlay);
    }

    private void ajouteLeMenu() {
        Button menu = new Button("Menu");
        menu.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        menu.setLayoutX(10);
        menu.setLayoutY(20);
        menu.setMinSize(60,20);
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
        boutonPausePlay();
        afficheBarVie();
        group.getChildren().add(menu);
    }


    private Button afficheBarVie(){
        Button barVie = new Button("barre de vie");
        barVie.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        barVie.setLayoutX(1700);
        barVie.setLayoutY(20);
        barVie.setMinSize(60, 20);
        barVie.setOnMouseClicked(mouseEvent -> {
            if (!personnages.isEmpty()){
                for (int i = 0; i < personnages.size(); i++) {
                    personnages.get(i).afficherSanteetnom();
                }
            }
        });

        group.getChildren().add(barVie);

        return barVie;
    }

    public void tour() {
        while (e1.getTeam().size() != 0 || e2.getTeam().size() != 0 || statusPartie != false) {
            for (Personne p : personnages) {
                p.action();
            }
        }
    }

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
