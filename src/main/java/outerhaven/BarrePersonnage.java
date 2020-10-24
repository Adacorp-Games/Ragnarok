package outerhaven;
import javafx.scene.Group;

public class BarrePersonnage{

    private Group group = new Group();

    public BarrePersonnage(double longeurdelabarre) {
        genereBarre(longeurdelabarre);
    }

    public void genereBarre(double longeurdelabarre){
        //creation de la barre dans le this.group
    }

    public Group returnBarre(){
        return group;
    }

}
