package outerhaven;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Case {
    int id;
    boolean status;
    Case[] caseVoisines = new Case[6];

    public Case(int id, boolean status) {
        this.id = id;
        this.status = false;
    }

    public ImageView afficherCase(double X, double Y , Double taille) {
        if(!status) {
            Image hexagone_img1 = new Image("https://media.discordapp.net/attachments/653028105870639109/764437469944348702/hexagon-png-transparent-images-171300-9880327.png?width=417&height=468");
            //Image hexagone_img1 = new Image(Case.class.getResourceAsStream("Images/hexagon.png"));
            ImageView hexagone = new ImageView(hexagone_img1);
            Image hexagone_img2 = new Image("https://media.discordapp.net/attachments/653028105870639109/764415718308970516/hexa.png?width=407&height=468");
            hexagone.setFitHeight(taille);
            hexagone.setFitWidth(taille);
            hexagone.setX(X);
            hexagone.setY(Y+300);
            hexagone.setOnMouseEntered((mouseEvent) -> {
                hexagone.setImage(hexagone_img2);
            });
            hexagone.setOnMouseExited((mouseEvent) -> {
                hexagone.setImage(hexagone_img1);
            });
            arriveCase(hexagone);
            return hexagone;
        }
        else {
            return null;
        }
    }

    private void arriveCase(ImageView image){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
            Descendre(5,image);
        }));
        timeline.setCycleCount(60);
        timeline.play();
    }

    public void Descendre(int pixel, ImageView image) {
        image.setY(image.getY()-pixel);
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public Case[] getCaseVoisines() {
        return caseVoisines;
    }
}
