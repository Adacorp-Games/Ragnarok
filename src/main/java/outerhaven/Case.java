package outerhaven;

import javafx.scene.Group;
import javafx.scene.effect.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import outerhaven.Personnages.Personne;

import static outerhaven.Plateau.*;

public class Case {
    private double posX;
    private double posY;
    private Color couleur;
    private int[] coordonnee = new int[2];
    private ArrayList<Case> caseVoisines;
    private ArrayList<Personne> contenu;
    private Group affichagecontenu;
    private ImageView hexagone;

    public static Image hexagone_img1 = new Image(Case.class.getResourceAsStream("./Images/hexagon.png"));
    public static Image hexagone_img2 = new Image(Case.class.getResourceAsStream("./Images/hexagon2.png"));

    public Case(int x, int y) {
        coordonnee[0] = x;
        coordonnee[1] = y;
        contenu = new ArrayList<Personne>();
        caseVoisines = new ArrayList<Case>();
    }

    public ImageView afficherCase(double X, double Y, double taille) {
        if (!estOccupe()) {
            hexagone = new ImageView(hexagone_img1);
            hexagone.setFitHeight(taille);
            hexagone.setFitWidth(taille);
            hexagone.setX(X);
            hexagone.setY(Y + 2000);
            this.posX = X;
            this.posY = Y + taille/5;
            hexagone.setOnMouseEntered((mouseEvent) -> {
                hexagone.setImage(hexagone_img2);
            });
            hexagone.setOnMouseExited((mouseEvent) -> {
                hexagone.setImage(hexagone_img1);
            });
            hexagone.setOnMousePressed((mouseEvent)-> {
                interactionHex();
            });
            arriveCase(hexagone);
            return hexagone;
        } else {
            return null;
        }
    }

    public void seVider() {
        if (contenu.size() > 0) {
            if(!statusPartie){
                personnages.remove(contenu.get(0));
                contenu.get(0).getTeam().getTeam().remove(contenu.get(0));
            }
            contenu.get(0).supprimerSanteEtNom();
            contenu.remove(0);
            Plateau.group.getChildren().remove(affichagecontenu);
            hexagone.setEffect(null);
        }

    }
    public void rentrePersonnage(Personne personne){
        if(!estOccupe()){
            contenu.add(personne);
            affichagecontenu = contenu.get(0).affichagePersonnage();
            contenu.get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(affichagecontenu);
            InnerShadow ombre = new InnerShadow();
            ombre.colorProperty().setValue(contenu.get(0).getTeam().getCouleur());
            hexagone.setEffect(ombre);
        }
    }
    public void interactionHex() {
        if (statusPartie != true) {
            if (Plateau.personneSelectionné != null && Plateau.equipeSelectionné != null && contenu.isEmpty()) {
                contenu.add(Plateau.personneSelectionné.personneNouvelle(Plateau.equipeSelectionné,this));
                affichagecontenu = contenu.get(0).affichagePersonnage();
                contenu.get(0).afficherSanteEtNom();
                Plateau.group.getChildren().add(affichagecontenu);
                InnerShadow ombre = new InnerShadow();
                ombre.colorProperty().setValue(Plateau.equipeSelectionné.getCouleur());
                hexagone.setEffect(ombre);
                //System.out.println(this.caseVoisines);
            }
            else if ((Plateau.personneSelectionné != null && Plateau.equipeSelectionné != null) || (Plateau.personneSelectionné == null &&  !contenu.isEmpty())) {
                seVider();
            }
            else {
                Text attention = new Text("Veuillez selectionner une equipe et un personnage");
                attention.setX(posX);
                attention.setY(posY);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                Plateau.group.getChildren().add(attention);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> {
                    Plateau.group.getChildren().remove(attention);
                }));
                timeline.play();
            }
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

    public boolean estOccupe() {
        if (contenu.size() == 0) {
            return false;
        } else {
            if (contenu.size() == 1) {
                return true;
            }
            System.out.println("Attention la case contient plus d'un obstacle/unite");
            return true;
        }
    }

    @Override
    public String toString() {
        return "Case {" + Arrays.toString(coordonnee) +
                '}';
    }

    public void trouverVoisin() {
        for (Case c : listeCase) {
            if((c.getCoordonnee()[1] == this.getCoordonnee()[1]-1
                    || c.getCoordonnee()[1] == this.getCoordonnee()[1]+1) && c.getCoordonnee()[0] == getCoordonnee()[0]) {
                caseVoisines.add(c);
            }
            if((c.getCoordonnee()[0]==this.getCoordonnee()[0]-1
                    || c.getCoordonnee()[0]==this.getCoordonnee()[0]+1) && c.getCoordonnee()[1] == getCoordonnee()[1]) {
                caseVoisines.add(c);
            }
            if((c.getCoordonnee()[0] == this.getCoordonnee()[0]-1 && c.getCoordonnee()[1] == this.getCoordonnee()[1]+1)
                    || (c.getCoordonnee()[0] == getCoordonnee()[0]+1 && c.getCoordonnee()[1] == getCoordonnee()[1]-1)) {
                caseVoisines.add(c);
            }
        }
    }

    public void afficherCaseVoisines(int longueur, boolean status) {
        Image voisin;
        if (status) {
            voisin = hexagone_img2;
        } else {
            voisin = hexagone_img1;
        }
        this.getHexagone().setImage(voisin);
        if (longueur == 1) {
            for (Case c : this.getCaseVoisines()) {
                if (!c.estOccupe()) {
                    c.getHexagone().setImage(voisin);
                }
            }
        } else {
            for (Case c : this.getCaseVoisines()) {
                if (!c.estOccupe()) {
                    c.getHexagone().setImage(voisin);
                    c.afficherCaseVoisines(longueur - 1, status);
                }
            }
        }
    }

    public ArrayList<Case> voisinsLibres(boolean lib) {
        ArrayList<Case> libres = new ArrayList<>();
        for (Case case1 : caseVoisines) {
            if (lib) {
                if (!case1.estOccupe()) {
                    libres.add(case1);
                }
            } else {
                if (case1.estOccupe()) {
                    libres.add(case1);
                }
            }
        }
        return libres;
    }
    public int nbVoisinsLibres() {
        int i = 0;
        for (Case c : caseVoisines) {
            if (!c.estOccupe()) {
                i++;
            }
        }
        return i;
    }
/*
    public ArrayList<Case> pathToPersoAux(Equipe equipe, ArrayList<Case> parcours, int depth, int initialdepth) {
        if (depth == 0) {
            //si this contient le perso de l'equipe voulue retourne le chemin jusqu'a lui
            if (!contenu.isEmpty() && contenu.get(0).getTeam() == equipe) {
                parcours.add(this);
                return parcours;
            } else {
                return new ArrayList<>();
            }
        } else {
            parcours.add(this);
            for (int i = 0; i < caseVoisines.size() - 1; i++) {
                if (!parcours.contains(caseVoisines.get(i)) && (caseVoisines.get(i).contenu.isEmpty() || caseVoisines.get(i).contenu.get(0).getTeam() == equipe )) {

                    if (caseVoisines.get(i).pathToPersoAux(equipe, new ArrayList<Case>(parcours), depth - 1, initialdepth).size() == initialdepth + 1) {
                        return caseVoisines.get(i).pathToPersoAux(equipe, new ArrayList<Case>(parcours), depth - 1, initialdepth);
                    }
                }
            }
            return new ArrayList<>();
        }
    }

    // Necessite que le plateau contienne au moins un personnage de l'equipe visee en dehors de this

    public ArrayList<Case> pathToPerso(Equipe e) {
        ArrayList<Case> parcours = new ArrayList<Case>();
        int depth = 1;
        while (parcours.isEmpty()) {
            parcours = pathToPersoAux(e, new ArrayList<Case>(), depth, depth);
            depth++;
        }
        return parcours;
    }*/

    public ArrayList<Case> pathToPerso(Equipe e) {
        int depth = 10000000;
        Personne Leplusprocche = null;
        for (int i = 0; i < personnages.size(); i++) {
            if(this.contenu.get(0) != personnages.get(i)){
                int x = (personnages.get(i).getPosition().getCoordonnee()[0]- getCoordonnee()[0]);
                int y = (personnages.get(i).getPosition().getCoordonnee()[1]- getCoordonnee()[1]);
                double norme = Math.sqrt(x*x+y*y);
                if(norme<depth && personnages.get(i).getTeam()==e){
                    depth = (int)norme;
                    Leplusprocche=personnages.get(i);
                }
            }
        }
        assert Leplusprocche != null;
        return pathToPersoAux(Leplusprocche);
    }
    public ArrayList<Case> pathToPersoAux(Personne personne){
        ArrayList<Case> chemin= new ArrayList<>();
        chemin.add(this);
        int x = (personne.getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
        int y = (personne.getPosition().donneYpourTab()- this.donneYpourTab());
        double Avancementx =  x/Math.sqrt(x*x+y*y);
        double Avanvementy =  y/Math.sqrt(x*x+y*y);
        double xIncr=0;
        double yIncr=0;
        while(chemin.get(chemin.size()-1).contenu.isEmpty() || chemin.get(chemin.size()-1).contenu.get(0) != personne) {
            xIncr = xIncr + Avancementx;
            yIncr = yIncr + Avanvementy;
            System.out.println(xIncr +" "+ arrondir(xIncr));
            testCase(chemin, arrondir(xIncr),arrondir(yIncr),personne);
        }
        System.out.println(chemin.toString());
        return chemin;
    }

    public void testCase(ArrayList<Case> chemin, int xIncr,int yIncr, Personne personne){
        //System.out.println(xIncr +" "+ yIncr);
        if(tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() +  yIncr].getContenu().isEmpty() || tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() +  yIncr].getContenu().get(0) == personne) {
            chemin.add(tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() +  yIncr]);
        }
        else {
            for (int i = 0; i < caseVoisines.size() ; i++) {
                for (int j = 0; j < tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.size(); j++) {
                    if(tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() + yIncr].caseVoisines.get(j) == caseVoisines.get(i) && tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.get(j).getContenu().isEmpty()){
                        chemin.add(caseVoisines.get(i));
                    }
                }

            }
        }
    }

    private int donneYpourTab(){
        if(coordonnee[0]%2==0) {
            return coordonnee[1] + (coordonnee[0]/2) ;
        }
        else{
            return (coordonnee[1] + (coordonnee[0])/2+1);
        }
    }

    private int arrondir(double nombre){
        if(Math.abs(nombre-(int)nombre)>0.5){
            if(nombre<0){
                return (int) nombre - 1;
            }
            else{
                return (int) nombre +1 ;
            }
        }
        else{
            return (int)nombre;
        }
    }


    // Getter et setter

    public int[] getCoordonnee() {
        return coordonnee;
    }

    public ImageView getHexagone() {
        return hexagone;
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

    public void setCaseVoisines(ArrayList<Case> voisines) {
        this.caseVoisines = voisines;
    }

    public void addVoisin(Case v) {
        caseVoisines.add(v);
    }

    public void setContenu(Personne p) {
        if (estOccupe()) {
            System.out.println("la case contient deja un obstacle/unite");
            contenu.add(p);
        }
    }

    public ArrayList<Personne> getContenu() {
        return contenu;
    }
}