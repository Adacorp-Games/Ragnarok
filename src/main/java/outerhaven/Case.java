package outerhaven;

import javafx.scene.Group;
import javafx.scene.effect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import outerhaven.Personnages.Personne;
import static outerhaven.Plateau.*;

/**
 * Une case represente un hexagone sur le plateau,
 * elle peut contenir un personnage et etre alterer,
 * cette classe contient aussi tout le systeme de detection permettant ainsi les combats
 */
public class Case {
    /**
     * Pos X et Pos Y contiennent les coordonées de la case en px sur l'ecran de l'utilisteur
     * coordonnee contient les coordonnée X -> (0) et Y -> (1) de la case
     */
    private double posX;
    private double posY;
    private final int[] coordonnee = new int[2];
    /**
     * caseVoisines contient les cases cases partagant des cotés avec la this (6 max)
     */
    private ArrayList<Case> caseVoisines = new ArrayList<>();
    /**
     *C'est ce que contient la case, nous avons choisi d'untuliser une liste pour eviter certaine erreur
     */
    private final ArrayList<Personne> contenu = new ArrayList<>();
    /**
     * group contenant l'affichage de la case
     */
    private Group affichagecontenu;
    /**
     *Contenant l'alteration de la case : null si la case n'a rien
     */
    private Alteration alteration;
    /**
     * contient l'image d'une case --> forme hexagonale
     */
    private ImageView hexagone;
    private static final Image hexagone_img1 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon.png"));
    private static final Image hexagone_img2 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon2.png"));

    public Case(int x, int y) {
        coordonnee[0] = x;
        coordonnee[1] = y;
    }

    /**
     * Genere l'aficche d'une case ainsi que ses interactions
     * @param X
     * @param Y
     * @param taille
     * @return
     */
    public ImageView afficherCase(double X, double Y, double taille) {
        if (!estOccupe()) {
            hexagone = new ImageView(hexagone_img1);
            hexagone.setFitHeight(taille);
            hexagone.setFitWidth(taille);
            hexagone.setX(X);
            hexagone.setY(Y + 2000);
            this.posX = X;
            this.posY = Y + taille/5;
            //lorsqu'une souris sort de la case
            hexagone.setOnMouseEntered((mouseEvent) -> hexagone.setImage(hexagone_img2));
            //lorsqu'une souris sort de la case (en focntion de l'alteration)
            hexagone.setOnMouseExited((mouseEvent) -> {
                if (this.getAlteration() != null) {
                    hexagone.setImage(this.getAlteration().getImage());
                } else {
                    hexagone.setImage(hexagone_img1);
                }
            });
            //lorsque l'on clique sur la souris
            hexagone.setOnMousePressed((mouseEvent)-> interactionHex());
            arriveCase(hexagone);
            return hexagone;
        } else {
            return null;
        }
    }

    /**
     * Methode permetant à une classe de se vider, ne plus avoir de contenu
     */
    public void seVider() {
        if (contenu.size() > 0) {
            if (!statusPartie) {
                personnages.remove(contenu.get(0));
                contenu.get(0).getTeam().getTeam().remove(contenu.get(0));
            }
            contenu.get(0).supprimerSanteEtNom();
            contenu.remove(0);
            Plateau.group.getChildren().remove(affichagecontenu);
            hexagone.setEffect(null);
        }
    }

    /**
     * Ajoute un personnage dans une case si il veut s'y deplacer
     * @param personne
     */
    public void rentrePersonnage(Personne personne) {
        if (!estOccupe()) {
            contenu.add(personne);
            affichagecontenu = contenu.get(0).affichagePersonnage();
            contenu.get(0).afficherSanteEtNom();
            Plateau.group.getChildren().add(affichagecontenu);
            InnerShadow ombre = new InnerShadow();
            ombre.colorProperty().setValue(contenu.get(0).getTeam().getCouleur());
            hexagone.setEffect(ombre);
        }
    }

    /**
     * Gere l'interaction d'une case lorsque l'on clique dessus
     */
    public void interactionHex() {
        if (!statusPartie) {
            //Verifie si il y a bien un personnage selectionné dans une equipe et que la case vide est vide
            if (personneSelectionne != null && equipeSelectionne != null && contenu.isEmpty()) {
                //Verifie si l'utilisateur à assez d'argent
                if ((equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) || argentPartie == 0) {
                    //met à jour l'affichage,la liste des personnages sur le plateau et dans les equipes
                    contenu.add(personneSelectionne.personneNouvelle(equipeSelectionne,this));
                    affichagecontenu = contenu.get(0).affichagePersonnage();
                    contenu.get(0).afficherSanteEtNom();
                    group.getChildren().add(affichagecontenu);
                    InnerShadow ombre = new InnerShadow();
                    ombre.colorProperty().setValue(equipeSelectionne.getCouleur());
                    hexagone.setEffect(ombre);
                    if (equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) {
                        equipeSelectionne.setArgent(equipeSelectionne.getArgent() - personneSelectionne.getCost());
                    }
                    equipeSelectionne.setNbPersonne();
                }
            }
            //Cas où l'utilisateur cherche à vider une case
            else if ((personneSelectionne != null && equipeSelectionne != null) || (personneSelectionne == null && !contenu.isEmpty())) {
                if (argentPartie != 0) {
                    contenu.get(0).getTeam().setArgent(contenu.get(0).getTeam().getArgent() + contenu.get(0).getCost());
                }
                seVider();
                equipeSelectionne.setNbPersonne();
            }
            //Cas ou l'utilisateur ne respect aucune condition
            else {
                Text attention = new Text("Veuillez selectionner une equipe et un personnage");
                attention.setX(posX);
                attention.setY(posY);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                Plateau.group.getChildren().add(attention);
                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1500), ev -> Plateau.group.getChildren().remove(attention)));
                timeline.play();
            }
        }
    }

    /**
     * Anime les cases lors de l'arrivée
     * @param image
     */
    private void arriveCase(ImageView image) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), ev -> mouvementY(image)));
        timeline.setCycleCount(400);
        timeline.play();
    }

    /**
     * Deplace une case en Y
     * @param image
     */
    private void mouvementY(ImageView image) {
        image.setY(image.getY() - 5);
    }

    /**
     * Verifie si une case est vide ou pleine
     * @return
     */
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

    /**
     * Cherche les voisins d'une casse pour les mettrent dans voisinCase
     */
    public void trouverVoisin() {
        //Verifie toutes les case en fonction de leurs cordonnées
        for (Case c : listeCase) {
            if ((c.getCoordonnee()[1] == this.getCoordonnee()[1] - 1
                    || c.getCoordonnee()[1] == this.getCoordonnee()[1] + 1) && c.getCoordonnee()[0] == getCoordonnee()[0]) {
                caseVoisines.add(c);
            }
            if ((c.getCoordonnee()[0] == this.getCoordonnee()[0] - 1
                    || c.getCoordonnee()[0] == this.getCoordonnee()[0] + 1) && c.getCoordonnee()[1] == getCoordonnee()[1]) {
                caseVoisines.add(c);
            }
            if ((c.getCoordonnee()[0] == this.getCoordonnee()[0] - 1 && c.getCoordonnee()[1] == this.getCoordonnee()[1] + 1)
                    || (c.getCoordonnee()[0] == getCoordonnee()[0] + 1 && c.getCoordonnee()[1] == getCoordonnee()[1] - 1)) {
                caseVoisines.add(c);
            }
        }
    }

    /**
     * Permet d'afficher visiuellement les case voisines
     * @param longueur
     * @param status
     */
    public void afficherCaseVoisines(int longueur, boolean status) {
        Image voisin;
        if (status) {
            voisin = hexagone_img2;
        } else {
            if (this.getAlteration() != null) {
                voisin = this.getAlteration().getImage();
            } else {
                voisin = hexagone_img1;
            }
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

    /**
     * cherches tout les cases voisines libres
     * @param lib
     * @return
     */
    public ArrayList<Case> voisinsLibres(boolean lib) {
        ArrayList<Case> libres = new ArrayList<>();
        for (Case case1 : caseVoisines) {
            if (case1.estOccupe()!=lib) {
                libres.add(case1);
            } else {
                libres.add(case1);
            }
        }
        return libres;
    }

    /**
     * Retourne une liste de voisin melangée
     * @return
     */
    public ArrayList<Case> getRandomVoisinLibre() {
        ArrayList<Case> listeVoisinLibres = this.voisinsLibres(true);
        Collections.shuffle(listeVoisinLibres);
        return listeVoisinLibres;
    }

    /**
     * la nombre de liste vide
     * @return
     */
    public int nbVoisinsLibres() {
        return voisinsLibres(true).size();
    }

    /**
     * Trouves le personnage ennemie le plus proche d'une case grace à un calcule de norme vectoriel
     * @param e
     * @return
     */
    public ArrayList<Case> pathToPerso(Equipe e) {
        //la profondeur max possible
        int depth = 10000000;
        Personne Leplusprocche = null;
        for (int i = 0; i < personnages.size(); i++) {
            if (this.contenu.get(0) != personnages.get(i)) {
                int x = (personnages.get(i).getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
                int y = (personnages.get(i).getPosition().getCoordonnee()[1] - getCoordonnee()[1]);
                double normeVectoriel = Math.sqrt(x*x + y*y);
                if (normeVectoriel < depth && personnages.get(i).getTeam() == e) {
                    depth = (int)normeVectoriel;
                    Leplusprocche = personnages.get(i);
                }
            }
        }
        assert Leplusprocche != null;
        return pathToPersoAux(Leplusprocche);
    }

    /**
     * Calule l'intinairaire d'une case à une autre grace à un systeme de deplacement vectoriel (deplacement unitaire vectoriel)
     * @param personne
     * @return
     */
    public ArrayList<Case> pathToPersoAux(Personne personne) {
        ArrayList<Case> chemin = new ArrayList<>();
        chemin.add(this);
        int x = (personne.getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
        int y = (personne.getPosition().donneYpourTab() - this.donneYpourTab());
        //Calcule du deplacment unitaire allant de xIncr à AvancementX de 1 en 1
        double Avancementx =  x/Math.sqrt(x*x+y*y);
        double Avanvementy =  y/Math.sqrt(x*x+y*y);
        double xIncr = 0;
        double yIncr = 0;
        while (chemin.get(chemin.size() - 1).contenu.isEmpty() || chemin.get(chemin.size() - 1).contenu.get(0) != personne) {
            xIncr = xIncr + Avancementx;
            yIncr = yIncr + Avanvementy;
            testCase(chemin, arrondir(xIncr), arrondir(yIncr), personne);
        }
        return chemin;
    }

    /**
     * Va chercher dans tableauCase et la tester pour savoir si elle peut etre ajouté à un trajet
     * @param chemin
     * @param xIncr
     * @param yIncr
     * @param personne
     */
    public void testCase(ArrayList<Case> chemin, int xIncr, int yIncr, Personne personne) {
        try{
        if (tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() + yIncr].getContenu().isEmpty() || tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() +  yIncr].getContenu().get(0) == personne) {
            //si la case est vide
            if(!caseVoisines.contains(tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() + yIncr])){
                for (int i = 0; i < caseVoisines.size() ; i++) {
                    for (int j = 0; j < tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.size(); j++) {
                        if (tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() + yIncr].caseVoisines.get(j) == caseVoisines.get(i) && tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.get(j).getContenu().isEmpty()) {
                            chemin.add(caseVoisines.get(i));
                        }
                    }
                }
            }
            chemin.add(tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() +  yIncr]);
        } else {
            //si la case est utilisé
            for (int i = 0; i < caseVoisines.size() ; i++) {
                for (int j = 0; j < tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.size(); j++) {
                    if (tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() + yIncr].caseVoisines.get(j) == caseVoisines.get(i) && tableauCase[this.getCoordonnee()[0] +  xIncr][this.donneYpourTab() +  yIncr].caseVoisines.get(j).getContenu().isEmpty()) {
                        chemin.add(caseVoisines.get(i));
                    }
                }
            }
        }} catch(Exception e) {
            //l'enchainement de try et du à l'erreur possible lors des bordures du tabelau de case (coté droit et coté gauche)
            try {
                testCase(chemin,xIncr-1, yIncr, personne);
            } catch(Exception e2) {
                testCase(chemin,xIncr+1, yIncr, personne);
            }
        }
    }

    /**
     * Cette methode convertie les coordonnées X Y d'une case (allant de -racine(aire) à +racine(aire)) en coordonné valable pour le tableau (allant de 0 à tableauCase.length)
     * @return
     */
    private int donneYpourTab() {
        //pour comprendre ce codage, il fait aller dans lancerScenePlateau de Plateau.class
        if (coordonnee[0]%2 == 0) {
            return coordonnee[1] + (coordonnee[0]/2) ;
        } else {
            return (coordonnee[1] + (coordonnee[0])/2 + 1);
        }
    }

    /**
     * Permet d'arrondir un nombre au 0.5 près (utile car (int) arrondi à la decimal inferieur
     * @param nombre
     * @return
     */
    private int arrondir(double nombre) {
        if (Math.abs(nombre - (int)nombre) > 0.6) {
            if (nombre < 0) {
                return (int) nombre - 1;
            } else {
                return (int) nombre + 1 ;
            }
        } else {
            return (int)nombre;
        }
    }

    /**
     * Autre pathToPerso et PathToPersoAux à partir d'un algo recurant (fonctionnelle mais bcp moins performant)
     * @return
     */
    /*public ArrayList<Case> pathToPersoAux(Equipe equipe, ArrayList<Case> parcours, int depth, int initialdepth) {
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

    /**
     * cette section contient tout les getteur et setteur de Equipe
     */


    public int[] getCoordonnee() {
        return coordonnee;
    }

    public ImageView getHexagone() {
        return hexagone;
    }

    public double getPosX() {
        return posX;
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

    public void setContenu(Personne p) {
        if (estOccupe()) {
            System.out.println("la case contient deja un obstacle/unite");
            contenu.add(p);
        }
    }

    public ArrayList<Personne> getContenu() {
        return contenu;
    }

    public Group getAffichagecontenu() {
        return affichagecontenu;
    }

    public void setAffichagecontenu(Group affichagecontenu) {
        this.affichagecontenu = affichagecontenu;
    }

    public Alteration getAlteration() {
        return alteration;
    }

    public void setAlteration(Alteration alteration) {
        if (alteration != null) {
            this.alteration = alteration;
            this.hexagone.setImage(alteration.getImage());
            listeCaseAlterees.add(this);
        } else {
            this.alteration = null;
            hexagone.setImage(hexagone_img1);
        }
    }
}