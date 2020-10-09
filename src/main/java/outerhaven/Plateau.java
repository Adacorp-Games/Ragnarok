package outerhaven;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;
import java.awt.*;
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
    public void lancerScenePlateu(){
        ImageView hexagone_img = new ImageView(new Image(Plateau.class.getResourceAsStream("./Image/Hexagone.png")));
        hexagone_img.setSize(1,2);

        HBox hexagone = new HBox();

        Group group = new Group();
        //group.getChildren().add(hexagone);

        Scene scene = new Scene(group,1280 , 720, Color.WHITE);
        primary.setScene(scene);
        primary.show();

    }
}
