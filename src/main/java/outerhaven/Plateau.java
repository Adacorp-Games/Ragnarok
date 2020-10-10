package outerhaven;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Plateau {
    int aire;
    ArrayList<Personne> personnages;
    ArrayList<Personne> morts;
    private static Stage primary;

    public Plateau(int aire ,Stage primary) {
        this.aire = aire;
        this.primary=primary;
    }

    public void lancerScenePlateau() {

        Group group = new Group();
        Scene scene = new Scene(group);

        Double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
        Double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();

        Double taille = 1000/Math.sqrt(aire);

        boolean decalage = false;
        int i = 0;
        int ligne = 0;

        while (i < aire) {
            if (!decalage) {
                Double posY = largeurMax / 2 - (taille * Math.sqrt(aire)/ 2) + ligne * taille - taille*ligne/4;
                decalage = true;
                for (int j = 0; j < Math.sqrt(aire); j++) {
                    Double posX = longeurMax/2 - (taille*(Math.sqrt(aire))/2)+ j*taille;
                    Case hexago = new Case(i, false);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    i++;
                }
                ligne++;
            }
            else {
                Double posY = largeurMax / 2 - (taille * Math.sqrt(aire)/ 2) + ligne * taille - taille*ligne/4;
                decalage = false;
                for (int j = 0; j < Math.sqrt(aire)+1 ; j++) {
                    Double posX =longeurMax/2 - (taille*(Math.sqrt(aire))/2)+ j*taille - taille/2 ;
                    Case hexago = new Case(i, false);
                    group.getChildren().add(hexago.afficherCase(posX,posY,taille));
                    i++;
                }
                ligne++;
            }
        }

        // Tests : Barre de vie
        /*Guerrier alex = new Guerrier();
        Archer medhy = new Archer();
        medhy.subirDegats(alex);
        alex.subirDegats(medhy);
        System.out.println("Vie alex : " + alex.getHealth());
        System.out.println("Vie medhy : " + medhy.getHealth());
        Group sante = medhy.afficherSante();
        sante.setTranslateY(50);
        group.getChildren().add(alex.afficherSante());
        group.getChildren().add(sante);*/

        primary.setScene(scene);
        primary.show();
    }
}
