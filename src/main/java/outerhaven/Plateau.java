package outerhaven;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import outerhaven.Interface.BarrePersonnage;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
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

    public Plateau(int aire ,Stage primary) {
        this.aire = aire;
        Plateau.primary = primary;
    }

    public void lancerParti() {
       scene = new Scene(group);

        Button start = new Button("START");
        start.setLayoutX((longeurMax-700)/2);
        start.setLayoutY((largeurMax-200)/2);
        start.setMinSize(700,200);
        group.getChildren().add(start);
        start.setOnMouseClicked(mouseEvent -> { lancerScenePlateau();
        group.getChildren().remove(0);}
        );

        primary.setScene(scene);
        primary.show();

    }

    public void incorporeEquipe(){
        equipeSelectionné = new Equipe();
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
                    double posX = longeurMax/2 - (taille*(Math.sqrt(aire))/2)+ j*taille;
                    Case hexago = new Case(i, false);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    listeCase.add(hexago);
                    i++;
                }
                    ligne++;
            }
            else {
                double posY = largeurMax / 2 - (taille * Math.sqrt(aire)/ 2) + ligne * taille - taille*ligne/4;
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


        //Bouton pause et reprendre
        Label labelPlay = new Label("");
        labelPlay.setLayoutY(670);

        Label labelPause = new Label("");
        labelPause.setLayoutY(650);

        Button play = new Button("Play");
        play.setLayoutX(10);
        play.setLayoutY(50);
        play.setMinSize(60,20);
        play.setOnMouseClicked(mouseEvent -> {
            labelPlay.setText("La partie reprend");
        });

        Button pause = new Button("Pause");
        pause.setLayoutX(10);
        pause.setLayoutY(20);
        pause.setMinSize(60,20);
        pause.setOnMouseClicked(mouseEvent ->{
            labelPause.setText("La partie est en pause");
        });
        group.getChildren().add(pause);
        group.getChildren().add(labelPause);
        group.getChildren().add(play);
        group.getChildren().add(labelPlay);

        //Bouton restrat et exit
        Button reStrat = new Button("RESTART");
        reStrat.setLayoutX(10);
        reStrat.setLayoutY(80);
        reStrat.setMinSize(60,20);
        reStrat.setOnMouseClicked(mouseEvent ->{
            group.getChildren().remove(0,group.getChildren().size());
            lancerScenePlateau();
        });

        Button exit = new Button("EXIT");
        exit.setLayoutX(10);
        exit.setLayoutY(110);
        exit.setMinSize(60,20);
        exit.setOnMouseClicked(mouseEvent -> primary.close());

        group.getChildren().add(reStrat);
        group.getChildren().add(exit);

        //Bouton équipe
        Button equipe1 = new Button("Equipe 1");
        equipe1.setLayoutX(10);
        equipe1.setLayoutY(800);
        equipe1.setMinSize(60, 20);
        equipe1.setOnMouseClicked(mouseEvent -> incorporeEquipe());

        Button equipe2 = new Button("Equipe 2");
        equipe2.setLayoutX(80);
        equipe2.setLayoutY(800);
        equipe2.setMinSize(60, 20);
        equipe2.setOnMouseClicked(mouseEvent -> incorporeEquipe());

        group.getChildren().add(equipe1);
        group.getChildren().add(equipe2);

        primary.setScene(scene);
//        primary.show();
    }

    public void test(){
    }
}
