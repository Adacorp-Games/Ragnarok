package outerhaven;

import javafx.scene.Group;
import javafx.scene.effect.*;

import java.util.ArrayList;
import java.util.Arrays;

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
            personnages.remove(contenu.get(0));
            contenu.get(0).supprimerSanteEtNom();
            contenu.get(0).getTeam().getTeam().remove(contenu.get(0));
            contenu.remove(0);
            Plateau.group.getChildren().remove(affichagecontenu);
            hexagone.setEffect(null);
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

    @Deprecated
    public ArrayList<Case> pathToPerso(Personne p, ArrayList<Case> parcours) {
        parcours.add(this);
        if (contenu.contains(p)) {
            return parcours;
        }
        ArrayList<Case> aParcourir = new ArrayList<Case>();
        for (Case case1 : voisinsLibres(true)) {
            if (!parcours.contains(case1)) {
                aParcourir.add(case1);
            }
        }
        ArrayList<ArrayList<Case>> cheminsEnfants = new ArrayList<ArrayList<Case>>();
        for (Case case1 : aParcourir) {
            cheminsEnfants.add(case1.pathToPerso(p, parcours));
        }
        ArrayList<Case> shorterPath = new ArrayList<Case>();
        for (ArrayList<Case> path : cheminsEnfants) {
            if (shorterPath.size() == 0) {
                shorterPath = path;
            } else {
                if (path.size() < shorterPath.size()) {
                    shorterPath = path;
                }
            }
        }
        return shorterPath;
    }

    public ArrayList<Case> pathToPersoAux(Equipe equipe, ArrayList<Case> parcours, int depth) {
        if (depth == 0) {
            //si this contient le perso de l'equipe voulue retourne le chemin jusqu'a lui
            if (contenu.get(0).getTeam() == equipe) {
                parcours.add(this);
                return parcours;
            }
        }
        ArrayList<ArrayList<Case>> parcoursEnfants = new ArrayList<ArrayList<Case>>();
        parcours.add(this);
        for (Case voisin : voisinsLibres(depth != 1)) {
            ArrayList<Case> parcoursVoisin = voisin.pathToPersoAux(equipe, parcours, depth-1);
            if (parcoursVoisin.size() == depth) {
                parcoursEnfants.add(parcoursVoisin);
            }
        }
        if(parcoursEnfants.isEmpty()){
            return parcours;
        }
        return parcoursEnfants.get(0);
    }

    // Necessite que le plateau contienne au moins un personnage de l'equipe visee en dehors de this
    public ArrayList<Case> pathToPerso(Equipe e) {
        ArrayList<Case> parcours = new ArrayList<Case>();
        int depth = 1;
        while (parcours.isEmpty()) {
            parcours = pathToPersoAux(e, new ArrayList<Case>(), depth);
            depth++;
        }
        return parcours;
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