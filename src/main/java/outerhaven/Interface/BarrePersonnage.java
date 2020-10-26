package outerhaven.Interface;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import outerhaven.Personnages.Archer;
import outerhaven.Personnages.Guerrier;
import outerhaven.Personnages.Personne;

import java.util.ArrayList;

public class BarrePersonnage{

    private Group group = new Group();
    double largeurMax = Screen.getPrimary().getVisualBounds().getHeight();
    double longeurMax = Screen.getPrimary().getVisualBounds().getWidth();
    ArrayList<Personne> listClasse = new ArrayList<Personne>();

    public BarrePersonnage() {
        //ajoutez les nouveaux Classe personnages ici                           <-------------------------------------------

        listClasse.add(new Archer());
        listClasse.add(new Guerrier());
        genereBarre();
    }

    public void genereBarre(){
        ImageView imageFond = new ImageView(new Image("https://cdn.discordapp.com/attachments/764528562429624391/770303212279824424/vector-square-frame-in-calligraphic-retro-style.png"));
        imageFond.setFitWidth(longeurMax);
        imageFond.setFitHeight(200);
        imageFond.setY(largeurMax-imageFond.getFitHeight());
        group.getChildren().add(imageFond);
        for (int i = 0; i < listClasse.size() ; i++) {
            group.getChildren().add(listClasse.get(i).affichagePersonnageBarre(i));
        }
    }

    public Group returnBarre(){
        return group;
    }

}
