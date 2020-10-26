package outerhaven;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private static Stage primary;
    public static Double taille;
    public static Scene scene;
    public static Personne personneSelectionné;
    public static Equipe equipeSelectionné;
    public static Group group = new Group();
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();

    public Plateau(Stage primary) {
        Plateau.primary = primary;
    }

    public static int getIntFromTextField(TextField textField) {
        String text = textField.getText();
        return Integer.parseInt(text);
    }

    public void lancerPartie() {
        scene = new Scene(group);
        //scene.getStylesheets().add("style.css");

        Button start = new Button("START");
        start.setLayoutX((longeurMax-700)/2);
        start.setLayoutY((largeurMax-200)/2);
        start.setMinSize(700,200);
        start.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black;-fx-font-weight: bold;-fx-font-size: 50");

        Text infoNB = new Text("Entrez le nombre de cases du plateau :");
        infoNB.setLayoutX((longeurMax-700)/2);
        infoNB.setLayoutY((largeurMax-300)/2);

        TextField nbCase = new TextField();
        nbCase.setLayoutX((longeurMax-700)/2);
        nbCase.setLayoutY((largeurMax-280)/2);
        nbCase.setMinSize(100,20);
        nbCase.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");

        group.getChildren().add(infoNB);
        group.getChildren().add(nbCase);
        group.getChildren().add(start);

        start.setOnMouseClicked(mouseEvent -> {
            group.getChildren().clear();
            aire = getIntFromTextField(nbCase);
            lancerScenePlateau();
        });

        primary.setScene(scene);
        primary.show();
    }

    public void incorporeEquipe(Equipe e1){
        equipeSelectionné = e1;
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
                    Case hexago = new Case(i, false);
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
                    Case hexago = new Case(i, false);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    listeCase.add(hexago);
                    i++;
                }
                ligne++;
            }
        }

        //creation et incorporation d'une slide barre
        BarrePersonnage barre = new BarrePersonnage();
        group.getChildren().add(barre.returnBarre());

        test();



        // Boutons pause et reprendre
        Label labelPlay = new Label("");
        labelPlay.setLayoutY(670);

        Label labelPause = new Label("");
        labelPause.setLayoutY(650);

        Button play = new Button("Play");
        play.setLayoutX(10);
        play.setLayoutY(80);
        play.setMinSize(60,20);
        play.setOnMouseClicked(mouseEvent -> {
            labelPlay.setText("La partie reprend");
        });

        Button pause = new Button("Pause");
        pause.setLayoutX(10);
        pause.setLayoutY(50);
        pause.setMinSize(60,20);
        pause.setOnMouseClicked(mouseEvent -> {
            labelPause.setText("La partie est en pause");
        });

        // Boutons restrat et exit
        Button reStrat = new Button("RESTART");
        reStrat.setLayoutX(10);
        reStrat.setLayoutY(110);
        reStrat.setMinSize(60,20);
        reStrat.setOnMouseClicked(mouseEvent -> {
            group.getChildren().remove(0,group.getChildren().size());
            lancerScenePlateau();
        });

        Button exit = new Button("EXIT");
        exit.setLayoutX(10);
        exit.setLayoutY(140);
        exit.setMinSize(60,20);
        exit.setOnMouseClicked(mouseEvent -> primary.close());

        // Bouton menu
        Button menu = new Button("Menu");
        menu.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        menu.setLayoutX(10);
        menu.setLayoutY(20);
        menu.setMinSize(60,20);
        menu.setOnMouseClicked(mouseEvent -> {
            if (!group.getChildren().contains(pause)) {
                group.getChildren().add(pause);
                group.getChildren().add(labelPause);
                group.getChildren().add(play);
                group.getChildren().add(labelPlay);
                group.getChildren().add(reStrat);
                group.getChildren().add(exit);
            } else {
                group.getChildren().remove(pause);
                group.getChildren().remove(labelPause);
                group.getChildren().remove(play);
                group.getChildren().remove(labelPlay);
                group.getChildren().remove(reStrat);
                group.getChildren().remove(exit);
            }
        });
        group.getChildren().add(menu);

        // Boutons équipes
        Button equipe1 = new Button("Equipe 1");
        equipe1.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        Equipe e1 = new Equipe(Color.RED);
        equipe1.setLayoutX(10);
        equipe1.setLayoutY(800);
        equipe1.setMinSize(60, 20);

        Button equipe2 = new Button("Equipe 2");
        equipe2.setStyle("-fx-background-color: lightgrey;-fx-border-style: solid;-fx-border-width: 2px;-fx-border-color: black");
        Equipe e2 = new Equipe(Color.BLUE);
        equipe2.setLayoutX(80);
        equipe2.setLayoutY(800);
        equipe2.setMinSize(60, 20);

        // Actions sur les boutons d'équipes
        equipe1.setOnMouseClicked(mouseEvent -> {
            incorporeEquipe(e1);
            equipe1.setEffect(new Effets().putShadow(e1.getCouleur()));
            equipe2.setEffect(null);
        });
        /*equipe1.setOnMouseEntered(mouseEvent -> {
            equipe1.setEffect(new Effets().putShadow(e1.getCouleur()));
        });
        equipe1.setOnMouseExited(mouseEvent -> {
            equipe1.setEffect(null);
        });*/

        equipe2.setOnMouseClicked(mouseEvent -> {
            incorporeEquipe(e2);
            equipe2.setEffect(new Effets().putShadow(e2.getCouleur()));
            equipe1.setEffect(null);
        });
        /*equipe2.setOnMouseEntered(mouseEvent -> {
            equipe2.setEffect(new Effets().putShadow(e2.getCouleur()));
        });
        equipe2.setOnMouseExited(mouseEvent -> {
            equipe2.setEffect(null);
        });*/

        /*Button equipe3 = new Button("Sans Equipe");
        equipe3.setLayoutX(150);
        equipe3.setLayoutY(800);
        equipe3.setMinSize(60, 20);
        equipe3.setOnMouseClicked(mouseEvent -> incorporeEquipe(null));*/

        group.getChildren().add(equipe1);
        group.getChildren().add(equipe2);
        //group.getChildren().add(equipe3);

        primary.setScene(scene);
        //primary.show();
    }

    // Zone de test
    public void test(){

    }
}
