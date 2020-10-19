package outerhaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Case {
    int id;
    boolean status;
    double posX;
    double posY;
    private Color couleur;
    Case[] caseVoisines = new Case[6];
    public static Image hexagone_img1 = new Image("https://cdn.discordapp.com/attachments/764528562429624391/764556130671132672/hexagon.png");
    public static Image hexagone_img2 = new Image("https://cdn.discordapp.com/attachments/764528562429624391/764556132613488680/hexagon2.png");

    public Case(int id, boolean status) {
        this.id = id;
        this.status = status;
        this.posX = posX;
        this.posY = posY;
    }

    public ImageView afficherCase(double X, double Y , Double taille) {
        if(!status) {
            //Image hexagone_img1 = new Image(Case.class.getResourceAsStream("Images/hexagon.png"));
            ImageView hexagone = new ImageView(hexagone_img1);
            hexagone.setFitHeight(taille);
            hexagone.setFitWidth(taille);
            hexagone.setX(X);
            hexagone.setY(Y+2000);
            this.posX = X;
            this.posY = Y+20;
            hexagone.setOnMouseEntered((mouseEvent) -> {
                hexagone.setImage(hexagone_img2);
            });
            hexagone.setOnMouseExited((mouseEvent) -> {
                hexagone.setImage(hexagone_img1);
            });
            //hexagone.setEffect();
            arriveCase(hexagone);
            return hexagone;
        }
        else {
            return null;
        }
    }

    private void arriveCase(ImageView image){
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), ev -> {
            mouvementY(5,image);
        }));
        timeline.setCycleCount(400);
        timeline.play();
    }

    private void mouvementY(int pixel, ImageView image) {
        image.setY(image.getY()-pixel);
    }

    public int getId() {
        return id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public Case[] getCaseVoisines() {
        return caseVoisines;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
