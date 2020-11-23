package outerhaven;

import javafx.scene.Group;

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
import outerhaven.Interface.Effets;
import outerhaven.Mecaniques.Alteration;
import outerhaven.Personnages.Personne;
import static outerhaven.Plateau.*;

/**
 * Une case représente un hexagone sur le plateau,
 * elle peut contenir un personnage et être altérée,
 * cette classe contient aussi tout le systeme de détection permettant ainsi les combats
 */

public class Case {
    /**
     * Pos X et Pos Y contiennent les coordonnées de la case en px sur l'ecran de l'utilisteur
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
     * C'est ce que contient la case, nous avons choisi d'untuliser une liste pour eviter certaine erreur
     */
    private final ArrayList<Personne> contenu = new ArrayList<>();
    /**
     * Groupe contenant l'affichage de la case
     */
    private Group affichagecontenu;
    /**
     * Contenant l'alteration de la case : null si la case n'a rien
     */
    private Alteration alteration;
    /**
     * Contient l'image d'une case --> forme hexagonale
     */
    private ImageView hexagone;
    private static final Image hexagone_img1 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon.png"));
    private static final Image hexagone_img2 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon2.png"));

    public Case(int x, int y) {
        coordonnee[0] = x;
        coordonnee[1] = y;
    }
    private static boolean bloqué;

    /**
     * Génère l'affichage d'une case ainsi que ses intéractions
     * @param X : position en X de l'image
     * @param Y : position en Y de l'image
     * @param taille : taille de l'image pixels
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
            // Lorsqu'une souris sort de la case
            hexagone.setOnMouseEntered((mouseEvent) -> hexagone.setImage(hexagone_img2));
            // Lorsqu'une souris sort de la case (en fonction de l'altération)
            hexagone.setOnMouseExited((mouseEvent) -> {
                if (this.getAlteration() != null) {
                    hexagone.setImage(this.getAlteration().getImage());
                } else {
                    hexagone.setImage(hexagone_img1);
                }
            });
            // Lorsque l'on clique sur la souris
            hexagone.setOnMousePressed((mouseEvent)-> interactionHex());
            arriveCase(hexagone);
            return hexagone;
        } else {
            return null;
        }
    }

    /**
     * Methode permetant à une case (this) de se vider, ne plus avoir de contenu
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
            //hexagone.setEffect(null);
        }
    }

    public void seViderPourAnimation(){
            contenu.get(0).supprimerSanteEtNom();
            contenu.remove(0);
            Plateau.group.getChildren().remove(affichagecontenu);
            //hexagone.setEffect(null);
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
            /*InnerShadow ombre = new InnerShadow();
            ombre.colorProperty().setValue(contenu.get(0).getTeam().getCouleur());
            hexagone.setEffect(ombre);*/
        }
    }

    /**
     * Gère l'intéraction d'une case lorsque l'on clique dessus
     */
    public void interactionHex() {
        if (!statusPartie) {
            // Vérifie si il y a bien un personnage selectionné dans une equipe et que la case vide est vide
            if (personneSelectionne != null && equipeSelectionne != null && contenu.isEmpty()) {
                // Vérifie si l'utilisateur à assez d'argent
                if ((equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) || argentPartie == 0) {
                    // Met à jour l'affichage, la liste des personnages sur le plateau et dans les équipes
                    contenu.add(personneSelectionne.personneNouvelle(equipeSelectionne,this));
                    affichagecontenu = contenu.get(0).affichagePersonnage();
                    contenu.get(0).afficherSanteEtNom();
                    group.getChildren().add(affichagecontenu);
                    /*InnerShadow ombre = new InnerShadow();
                    ombre.colorProperty().setValue(equipeSelectionne.getCouleur());
                    hexagone.setEffect(ombre);*/
                    if (equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) {
                        equipeSelectionne.setArgent(equipeSelectionne.getArgent() - personneSelectionne.getCost());
                    }
                    equipeSelectionne.setNbPersonne();
                }
            }
            // Cas où l'utilisateur cherche à vider une case
            else if ((personneSelectionne != null && equipeSelectionne != null) || (personneSelectionne == null && !contenu.isEmpty())) {
                if (argentPartie != 0) {
                    contenu.get(0).getTeam().setArgent(contenu.get(0).getTeam().getArgent() + contenu.get(0).getCost());
                }
                seVider();
                equipeSelectionne.setNbPersonne();
            }
            // Cas ou l'utilisateur ne respecte aucune condition
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
     */
    private void arriveCase(ImageView image) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(5), ev -> mouvementY(image)));
        timeline.setCycleCount(400);
        timeline.play();
    }

    /**
     * Déplace une case en Y
     * @param image
     */
    private void mouvementY(ImageView image) {
        image.setY(image.getY() - 5);
    }

    /**
     * Vérifie si une case est vide ou pleine
     * @return false si vide et true si occupée
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
     * Cherche les voisins d'une casse pour les mettrent dans voisinCase en début de partie
     */
    public void trouverVoisin() {
        // Vérifie toutes les cases en fonction de leurs cordonnées
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
     * Permet d'afficher visuellement les case voisines
     * @param longueur : portée d'affichage en case voisine
     * @param status : afficher ou non
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
     * Cherches toutes les cases voisines libres/pleines
     * @param lib : on cherche une case libre ou pleine
     * @return une liste de cases libres/pleines autour de this
     */
    public ArrayList<Case> voisinsLibres(boolean lib) {
        ArrayList<Case> libres = new ArrayList<>();
        for (Case c : caseVoisines) {
            if (c.estOccupe() != lib) {
                libres.add(c);
            }
        }
        return libres;
    }

    /**
     * Retourne une liste de voisin libres melangés
     */
    public ArrayList<Case> getRandomVoisinLibre() {
        ArrayList<Case> listeVoisinLibres = this.voisinsLibres(true);
        Collections.shuffle(listeVoisinLibres);
        return listeVoisinLibres;
    }

    /**
     * Méthode qui retourne le nombre de voisins vides
     */
    public int nbVoisinsLibres() {
        return voisinsLibres(true).size();
    }

    /**
     * Trouves le personnage ennemie le plus proche d'une case grâce à un calcul de norme vectoriel
     * @return une liste de cases étant le chemin vers la cible la plus proche
     */
    public ArrayList<Case> pathToPerso(Equipe e) {
        // La profondeur max possible
        int depth = 10000000;
        Personne laPlusProche = null;
        for (Personne personnage : personnages) {
            if (this.contenu.get(0) != personnage) {
                int x = (personnage.getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
                int y = (personnage.getPosition().getCoordonnee()[1] - getCoordonnee()[1]);
                double normeVectoriel = Math.sqrt(x * x + y * y);
                if (normeVectoriel < depth && personnage.getTeam() == e) {
                    depth = (int) normeVectoriel;
                    laPlusProche = personnage;
                }
            }
        }
        assert laPlusProche != null;
        return pathToPersoAux(laPlusProche);
    }

    /**
     * Calcule l'itinéraire d'une case à une autre grace à un systeme de déplacement vectoriel (déplacement unitaire vectoriel)
     */
    public ArrayList<Case> pathToPersoAux(Personne personne) {
        ArrayList<Case> chemin = new ArrayList<>();
        chemin.add(this);
        int x = (personne.getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
        int y = (personne.getPosition().donneYpourTab() - this.donneYpourTab());
        // Calcule du deplacment unitaire allant de xIncr à AvancementX de 1 en 1
        double Avancementx =  x/Math.sqrt(x*x+y*y);
        double Avanvementy =  y/Math.sqrt(x*x+y*y);
        double xIncr = 0;
        double yIncr = 0;
        bloqué = true;
        while (chemin.get(chemin.size() - 1).contenu.isEmpty() || chemin.get(chemin.size() - 1).contenu.get(0) != personne && bloqué) {
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
            try {
                boolean ajout = false;
                Case c = tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() + yIncr];
                if (c.getContenu().isEmpty() || c.getContenu().get(0) == personne) {
                    // Si la case est vide
                    if (!caseVoisines.contains(c)) {
                        for (int i = 0; i < caseVoisines.size(); i++) {
                            for (int j = 0; j < c.caseVoisines.size(); j++) {
                                if (c.caseVoisines.get(j) == caseVoisines.get(i) && c.caseVoisines.get(j).getContenu().isEmpty() && c.caseVoisines.get(i) != this.getContenu().get(0).getCasePrecedente()) {
                                    chemin.add(caseVoisines.get(i));
                                    ajout = true;
                                }
                            }
                        }
                    }
                    chemin.add(c);
                } else {
                    // Si la case est utilisé
                    for (int i = 0; i < caseVoisines.size(); i++) {
                        for (int j = 0; j < c.caseVoisines.size(); j++) {
                            if (c.caseVoisines.get(j) == caseVoisines.get(i) && c.caseVoisines.get(j).getContenu().isEmpty() && caseVoisines.get(i) != this.getContenu().get(0).getCasePrecedente()) {
                                chemin.add(caseVoisines.get(i));
                                ajout = true;
                            }
                        }
                    }
                    if (!ajout) {
                        for (Case c2 : caseVoisines) {
                            if (c2.getContenu().isEmpty() && c2 != this.getContenu().get(0).getCasePrecedente()) {
                                chemin.add(c2);
                                ajout = true;
                            }

                        }
                    }
                }
                if (!ajout) {
                    bloqué = false;
                }
            } catch (Exception e) {
                // L'enchainement de try et du à l'erreur possible lors des bordures du tabelau de case (coté droit et coté gauche)
                try {
                    testCase(chemin, xIncr - 1, yIncr, personne);
                } catch (Exception e2) {
                    testCase(chemin, xIncr + 1, yIncr, personne);
                }
            }
    }

    /**
     * Cette méthode convertie les coordonnées X Y d'une case (allant de -racine(aire) à +racine(aire)) en coordonné valable pour le tableau (allant de 0 à tableauCase.length)
     * @return
     */
    private int donneYpourTab() {
        // Pour comprendre ce codage, il faut aller dans lancerScenePlateau de Plateau.class
        if (coordonnee[0]%2 == 0) {
            return coordonnee[1] + (coordonnee[0]/2) ;
        } else {
            return (coordonnee[1] + (coordonnee[0])/2 + 1);
        }
    }

    /**
     * Permet d'arrondir un nombre au 0.5 près (utile car (int) arrondi à la decimal inferieur
     */
    private int arrondir(double nombre) {
        if (Math.abs(nombre - (int) nombre) > 0.6) {
            if (nombre < 0) {
                return (int) nombre - 1;
            } else {
                return (int) nombre + 1 ;
            }
        } else {
            return (int) nombre;
        }
    }

    public void ajouterAlter(String effet, int puissance, int duree) {
        if (this.getAlteration() == null) {
            this.setAlteration(new Alteration(effet, puissance, duree));
            this.getHexagone().setEffect(new Effets().putInnerShadow(Color.BLACK));
            Plateau.listeCaseAlterees.add(this);
        } else {
            this.getAlteration().setDuree(duree);
        }
    }

    /**
     * Autre pathToPerso et PathToPersoAux à partir d'un algo récurant (fonctionnelle mais bcp moins performant)
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
     * Cette section contient tout les getters et setters de Case
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

    /**
     * Méthode permettant de modifier l'atération de this
     * @param alteration à mettre sur this
     */
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