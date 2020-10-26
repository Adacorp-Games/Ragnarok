package outerhaven;

import javafx.scene.Group;
import javafx.scene.Scene;
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

    public Plateau(int aire ,Stage primary) {
        this.aire = aire;
        Plateau.primary = primary;
    }

    public void lancerScenePlateau() {

        Group group = new Group();
        Scene scene = new Scene(group);

        double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
        double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();

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
        BarrePersonnage barre = new BarrePersonnage(longeurMax);
        group.getChildren().add(barre.returnBarre());


        // Tests : Barre de vie
        Equipe team1 = new Equipe();
        Equipe team2 = new Equipe();
        Guerrier alex = new Guerrier(team1, listeCase.get(5));
        Archer medhy = new Archer(team2, listeCase.get(10));
        //System.out.println(personnages.size());

        medhy.subirDegats(alex);
        alex.subirDegats(medhy);
        /*System.out.println("Vie alex : " + alex.getHealth());
        System.out.println("Vie medhy : " + medhy.getHealth());*/

        //alex.déplacer(listeCase.get(6));

        for (Personne p : personnages) {
            group.getChildren().add(p.affichagePersonnage());
        }

        primary.setScene(scene);
        primary.show();
    }
}
