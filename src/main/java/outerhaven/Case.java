package outerhaven;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import outerhaven.Personnages.Personne;

public class Case {
    int id;
    boolean estOccupe;
    double posX;
    double posY;
    private Color couleur;
    ArrayList<Case> caseVoisines;
    ArrayList<Personne> contenu;

    public static Image hexagone_img1 = new Image(
            "https://cdn.discordapp.com/attachments/764528562429624391/764556130671132672/hexagon.png");
    public static Image hexagone_img2 = new Image(
            "https://cdn.discordapp.com/attachments/764528562429624391/764556132613488680/hexagon2.png");

    public Case(int id, boolean status) {
        this.id = id;
        this.estOccupe = status;
        contenu = new ArrayList<Personne>();
        caseVoisines = new ArrayList<Case>();
        // this.posX = posX;
        // this.posY = posY;
    }

    public ImageView afficherCase(double X, double Y, double taille) {
        if (!estOccupe) {
            // Image hexagone_img1 = new
            // Image(Case.class.getResourceAsStream("Images/hexagon.png"));
            ImageView hexagone = new ImageView(hexagone_img1);
            hexagone.setFitHeight(taille);
            hexagone.setFitWidth(taille);
            hexagone.setX(X);
            hexagone.setY(Y + 2000);
            this.posX = X;
            this.posY = Y + 20;
            hexagone.setOnMouseEntered((mouseEvent) -> {
                hexagone.setImage(hexagone_img2);
            });
            hexagone.setOnMouseExited((mouseEvent) -> {
                hexagone.setImage(hexagone_img1);
            });
            // hexagone.setEffect();
            arriveCase(hexagone);
            return hexagone;
        } else {
            return null;
        }
    }

    private void arriveCase(ImageView image) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), ev -> {
            mouvementY(5, image);
        }));
        timeline.setCycleCount(400);
        timeline.play();
    }

    private void mouvementY(int pixel, ImageView image) {
        image.setY(image.getY() - pixel);
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return estOccupe;
    }

    public void setStatus(boolean status) {
        this.estOccupe = status;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public ArrayList<Case> getCaseVoisines() {
        return caseVoisines;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
