package outerhaven;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import outerhaven.Entites.Entite;
import outerhaven.Entites.Personnages.Personne;
import outerhaven.Interface.Effets;
import outerhaven.Mecaniques.Alterations.Alteration;

import java.util.*;

import static outerhaven.Interface.BarrePersonnage.listeEquipe1;
import static outerhaven.Interface.BarrePersonnage.listeEquipe2;
import static outerhaven.Plateau.*;

/**
 * Une case représente un hexagone sur le plateau,
 * elle peut contenir un personnage et être altérée,
 * cette classe contient aussi tout le système de détection permettant ainsi les combats.
 */

public class Case {
    public static final Image hexagone_img1 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon.png"));
    public static final Image hexagone_img2 = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagon2.png"));
    public static final Image hexagone_imgBlock = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagonBlock.png"));
    public static final Image hexagone_imgPoison = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagonToxic.png"));
    public static final Image hexagone_imgHeal = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagonHeal.png"));
    public static final Image hexagone_imgManaVore = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagonManaVore.png"));
    public static final Image hexagone_imgFreeze = new Image(Case.class.getResourceAsStream("/Images/Cases/hexagonFreeze.png"));
    /**
     * Liste/tableau des cases (altérées ou non) dans le plateau.
     */
    public static ArrayList<Case> listeCase = new ArrayList<>();
    public static ArrayList<Case> listeCaseAlterees = new ArrayList<>();
    public static Case[][] tableauCase;
    private static boolean bloqué;
    private final int[] coordonnee = new int[2];
    /**
     * C'est ce que contient la case, nous avons choisi d'utiliser une liste pour éviter certaine erreur.
     */
    private final ArrayList<Personne> contenu = new ArrayList<>();
    /**
     * Pos X et Pos Y contiennent les coordonnées de la case en px sur l'écran de l'utilisateur.
     * coordonnée contient les coordonnée X -> (0) et Y -> (1) de la case.
     */
    private double posX;
    private double posY;
    /**
     * caseVoisines contient les cases cases partageant des cotés avec la this (6 max).
     */
    private ArrayList<Case> caseVoisines = new ArrayList<>();
    /**
     * Groupe contenant l'affichage de la case.
     */
    private Group affichageContenu;
    /**
     * Contenant l'altération de la case : null si la case n'a rien.
     */
    private Alteration alteration;
    /**
     * Contient l'image d'une case -> forme hexagonale.
     */
    private ImageView hexagone;

    public Case(int x, int y) {
        coordonnee[0] = x;
        coordonnee[1] = y;
    }

    /**
     * Génère l'affichage d'une case ainsi que ses interactions.
     *
     * @param X      : position en X de l'image
     * @param Y      : position en Y de l'image
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
            this.posY = Y + taille / 5;
            // Lorsqu'une souris sort de la case
            hexagone.setOnMouseEntered((mouseEvent) -> {
                if (hexagone.getImage() != hexagone_imgBlock) {
                    hexagone.setImage(hexagone_img2);
                }
            });
            // Lorsqu'une souris sort de la case (en fonction de l'altération)
            hexagone.setOnMouseExited((mouseEvent) -> {
                if (this.getAlteration() != null && hexagone.getImage() != hexagone_imgBlock) {
                    hexagone.setImage(this.getAlteration().getImage());
                } else {
                    if (hexagone.getImage() != hexagone_imgBlock) {
                        hexagone.setImage(hexagone_img1);
                    }
                }
            });
            // Lorsque l'on clique sur la souris
            hexagone.setOnMousePressed((mouseEvent) -> interactionHex());
            arriveCase(hexagone);
            return hexagone;
        } else {
            return null;
        }
    }

    /**
     * Methode permettant à une case (this) de se vider, ne plus avoir de contenu.
     */
    public void seVider() {
        if (contenu.size() > 0) {
            if (!statusPartie) {
                Personne.personnages.remove(contenu.get(0));
                contenu.get(0).getTeam().getTeam().remove(contenu.get(0));
            }
            contenu.get(0).supprimerSanteEtNom();
            contenu.remove(0);
            group.getChildren().remove(affichageContenu);
        }
    }

    /**
     * Cette methode permet de vider le contenu affiché d'une cache pour permettre un futur déplacement.
     */
    public void seViderPourAnimation() {
        contenu.get(0).supprimerSanteEtNom();
        contenu.remove(0);
        group.getChildren().remove(affichageContenu);
    }

    /**
     * Cette methode est appelée par le brouillard() et permet de rendre visible une case qui a eté caché.
     */
    public void devenirBlanc() {
        if (alteration == null) {
            hexagone.setImage(hexagone_img1);
        } else {
            hexagone.setImage(alteration.getImage());
        }
        if (!group.getChildren().contains(affichageContenu) && !contenu.isEmpty()) {
            rentrePersonnage(contenu.get(0));
        }
    }

    /**
     * Cette methode est appelée par le brouillard() et permet de rendre caché une case qui est visible.
     */
    public void devenirNoir() {
        group.getChildren().remove(affichageContenu);
        hexagone.setImage(hexagone_imgBlock);
        if (!contenu.isEmpty()) {
            contenu.get(0).afficherSanteEtNom();
        }
    }

    /**
     * Cette methode est appelée par le brouillard() et permet verifier si une case est cachée.
     */
    public boolean verifNoir() {
        return hexagone.getImage() == hexagone_imgBlock;
    }

    /**
     * Ajoute un personnage dans une case si il veut s'y déplacer.
     */
    public void rentrePersonnage(Personne personne) {
        if (!estOccupe() || contenu.get(0) == personne) {
            contenu.clear();
            contenu.add(personne);
            affichageContenu = contenu.get(0).affichagePersonnage();
            contenu.get(0).afficherSanteEtNom();
            group.getChildren().add(listeCase.size() + 5, affichageContenu);
        }
    }

    /**
     * Gère l'interaction d'une case lorsque l'on clique dessus.
     */
    public void interactionHex() {
        if (!statusPartie) {
            // Vérifie si il y a bien un personnage sélectionné dans une équipe et que la case vide est vide
            if (personneSelectionne != null && equipeSelectionne != null && contenu.isEmpty() && this.hexagone.getImage() != hexagone_imgBlock) {
                // Vérifie si l'utilisateur à assez d'argent
                if ((equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) || argentPartie == 0) {
                    // Met à jour l'affichage, la liste des personnages sur le plateau et dans les équipes
                    contenu.add(personneSelectionne.personneNouvelle(equipeSelectionne, this));
                    affichageContenu = contenu.get(0).affichagePersonnage();
                    contenu.get(0).afficherSanteEtNom();
                    group.getChildren().add(listeCase.size() + 5, affichageContenu);
                    if (equipeSelectionne.getArgent() >= personneSelectionne.getCost() && argentPartie > 0) {
                        equipeSelectionne.setArgent(equipeSelectionne.getArgent() - personneSelectionne.getCost());
                    }
                    if (personneSelectionne.getClass().getName().contains("Prime")) {
                        try {
                            ArrayList<Personne> listeEquipe;
                            if (equipeSelectionne == Equipe.e1) {
                                listeEquipe = listeEquipe1;
                            } else {
                                listeEquipe = listeEquipe2;
                            }
                            for (Personne p : listeEquipe) {
                                if (p.getClass().getName().equals(personneSelectionne.getClass().getName())) {
                                    listeEquipe.remove(p);
                                    personneSelectionne = null;
                                    barre.majBarreEnchere();
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    equipeSelectionne.setNbPersonne();
                }
            }
            // Cas où l'utilisateur cherche à vider une case
            else if ((personneSelectionne != null && equipeSelectionne != null && !activerEnchere) || (personneSelectionne == null && !contenu.isEmpty() && !activerEnchere)) {
                if (argentPartie != 0) {
                    contenu.get(0).getTeam().setArgent(contenu.get(0).getTeam().getArgent() + contenu.get(0).getCost());
                }
                if (contenu.get(0).getClass().getName().contains("Prime")) {
                    listeEquipe1.add(contenu.get(0));
                    barre.majBarreEnchere();
                }
                seVider();
                equipeSelectionne.setNbPersonne();
            }
            // Cas ou l'utilisateur ne respecte aucune condition
            else {
                Text attention = new Text();
                if (activerEnchere && contenu.isEmpty()) {
                    attention.setText("Veuillez sélectionner un personnage");
                } else if (activerEnchere) {
                    attention.setText("Vous ne pouvez pas enlever combattant du plateau");
                } else {
                    attention.setText("Veuillez sélectionner une équipe et un personnage");
                }
                attention.setX(posX);
                attention.setY(posY);
                attention.underlineProperty().setValue(true);
                attention.setFill(Color.RED);
                group.getChildren().add(attention);
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
     */
    private void mouvementY(ImageView image) {
        image.setY(image.getY() - 5);
    }

    /**
     * Vérifie si une case est vide ou pleine
     *
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
        return "Case {" + Arrays.toString(coordonnee) + '}';
    }

    /*
    /**
     * Algo récursif qui a pour but de retourner la taille d'un block de personnages de même équipes
     * @param block qui est une Arraylist contenant toutes les cases du block
     * @param caseVu qui est une Arraylist contenant toutes les cases deja parcouru
     * @return le return n'est récupéré qu'une seule fois : quand le parcours final est finis
     */
    /*
    private ArrayList<Case> tailleBlock(ArrayList<Case> block, ArrayList<Case> caseVu) {
        if (this.contenu.isEmpty()) {
            caseVu.add(this);
        } else {
            if (this.contenu.get(0).getTeam() == block.get(0).getContenu().get(0).getTeam()) {
                block.add(this);
                for (Case c : this.caseVoisines) {
                    if (!block.contains(c) && !caseVu.contains(c)) {
                        c.tailleBlock(block, caseVu);
                    }
                }
            } else {
                caseVu.add(this);
            }
        }
        return block;
    }*/

    /**
     * Permet d'afficher visuellement les case voisines
     *
     * @param longueur : portée d'affichage en case voisine
     * @param status   : afficher ou non
     */
    public void afficherCaseVoisines(int longueur, boolean status) {
        Image voisin;
        if (status) {
            voisin = hexagone_img2;
        } else {
            voisin = hexagone_img1;
        }
        if (this.getHexagone().getImage() != hexagone_imgBlock) {
            this.getHexagone().setImage(voisin);
            if (longueur == 1) {
                for (Case c : this.getCaseVoisines()) {
                    if (!c.estOccupe() && c.hexagone.getImage() != hexagone_imgBlock) {
                        if (c.getAlteration() != null) {
                            c.getHexagone().setImage(c.getAlteration().getImage());
                        } else {
                            c.getHexagone().setImage(voisin);
                        }
                    }
                }
            } else {
                for (Case c : this.getCaseVoisines()) {
                    if (!c.estOccupe() && c.hexagone.getImage() != hexagone_imgBlock) {
                        if (c.getAlteration() != null) {
                            c.getHexagone().setImage(c.getAlteration().getImage());
                        } else {
                            c.getHexagone().setImage(voisin);
                        }
                        c.afficherCaseVoisines(longueur - 1, status);
                    }
                }
            }
        }
    }

    /**
     * Cherches toutes les cases voisines libres/pleines
     *
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
     * Retourne une liste de voisin libres mélangée
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
     * Cherche les voisins d'une case pour les mettre dans voisinCase en début de partie
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
     * Trouves le personnage ennemie le plus proche d'une case grâce à un calcul de norme vectoriel
     *
     * @return une liste de cases étant le chemin vers la cible la plus proche
     */
    public ArrayList<Case> pathToPerso(Equipe e) {
        // La profondeur max possible
        int depth = listeCase.size();
        Personne laPlusProche = null;
        // On regarde avec tout les personnages la norme vectoriel qui les séparent d'une case
        for (Personne personnage : Personne.personnages) {
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
     * Calcule l'itinéraire d'une case à une autre grâce à un système de déplacement vectoriel (déplacement unitaire vectoriel).
     */
    public ArrayList<Case> pathToPersoAux(Personne personne) {
        ArrayList<Case> chemin = new ArrayList<>();
        chemin.add(this);
        // On calcule la distance en X et en Y entre la case initiale et la case finale.
        int x = (personne.getPosition().getCoordonnee()[0] - getCoordonnee()[0]);
        int y = (personne.getPosition().donneYpourTab() - this.donneYpourTab());
        // Calcule du déplacement unitaire pour AvancementX/avancementY.
        final double sqrt = Math.sqrt(x * x + y * y);
        double avancementX = x / sqrt;
        double avancementY = y / sqrt;
        // xIncr et yIncr seront des variables qui seront incrémentées.
        // Elles représenteront étape par étape les déplacements en case pour le chemin final.
        double xIncr = 0;
        double yIncr = 0;
        bloqué = false;
        // On increment à chaque fois xIncr et yIncr pour tracer le chemin jusqu'à personne.
        while (chemin.get(chemin.size() - 1).contenu.isEmpty() || chemin.get(chemin.size() - 1).contenu.get(0) != personne && !bloqué) {
            xIncr = xIncr + avancementX;
            yIncr = yIncr + avancementY;
            // Nous vérifions si une case est valide et peut accueillir une Personne.
            testCase(chemin, arrondir(xIncr), arrondir(yIncr), personne);
        }
        return chemin;
    }

    /**
     * Va chercher dans tableauCase et la tester pour savoir si elle peut être ajouté à un trajet
     *
     * @param chemin   une liste de case qui servira de chemin final
     * @param xIncr    la difference de case en X entre this et la case étudiée
     * @param yIncr    la difference de case en Y entre this et la case étudiée
     * @param personne ennemie à trouver
     */
    public void testCase(ArrayList<Case> chemin, int xIncr, int yIncr, Personne personne) {
        try {
            boolean ajout = false;
            Case c = tableauCase[this.getCoordonnee()[0] + xIncr][this.donneYpourTab() + yIncr];
            if (c.getContenu().isEmpty() || c.getContenu().get(0) == personne) {
                // Si la case est vide
                if (!caseVoisines.contains(c)) {
                    // on vérifie si c est bien une case voisine de la case précédente, si ce n'est pas le cas, on cherche une case voisine commune au deux cases
                    for (int i = 0; i < caseVoisines.size(); i++) {
                        for (int j = 0; j < c.caseVoisines.size(); j++) {
                            if (c.caseVoisines.get(j) == caseVoisines.get(i) && c.caseVoisines.get(j).getContenu().isEmpty()) {
                                chemin.add(caseVoisines.get(i));
                                ajout = true;
                            }
                        }
                    }
                }
                chemin.add(c);
            } else {
                // Si la case est utilisé, on cherche une case voisine commune au deux cases
                for (int i = 0; i < caseVoisines.size(); i++) {
                    for (int j = 0; j < c.caseVoisines.size(); j++) {
                        if (c.caseVoisines.get(j) == caseVoisines.get(i) && c.caseVoisines.get(j).getContenu().isEmpty()) {
                            chemin.add(caseVoisines.get(i));
                            ajout = true;
                        }
                    }
                }
            }
            // Si l'on de c'est deux boucle for sans ajout d'une case, alors l'unité est bloqué et ne peut pas se déplacer
            if (!ajout) {
                bloqué = true;
            }
        } catch (Exception e) {
            // L'enchainement de try et du à l'erreur possible lors des bordures du tableau de case (coté droit et coté gauche)
            try {
                testCase(chemin, xIncr - 1, yIncr, personne);
            } catch (Exception e2) {
                testCase(chemin, xIncr + 1, yIncr, personne);
            }
        }
    }

    /**
     * Cette méthode convertie les coordonnées X et Y d'une case (allant de -racine(aire) à +racine(aire))
     * en coordonnée valable pour le tableau (allant de 0 à tableauCase.length)
     * - coordonnee[0] = X
     * - coordonnee[1] = Y
     *
     * @return un entier qui correspond à la coordonnée de Y.
     */
    private int donneYpourTab() {
        // Le codage se diffère entre deux lignes.
        if (coordonnee[0] % 2 == 0) {
            //quand c'est une ligne paire
            return coordonnee[1] + (coordonnee[0] / 2);
        } else {
            //quand c'est une ligne impaire
            return (coordonnee[1] + (coordonnee[0]) / 2 + 1);
        }
    }

    /**
     * Permet d'arrondir un nombre au 0.5 près (utile car (int) arrondi à la decimal inférieur)
     */
    private int arrondir(double nombre) {
        if (Math.abs(nombre - (int) nombre) > 0.6) {
            if (nombre < 0) {
                return (int) nombre - 1;
            } else {
                return (int) nombre + 1;
            }
        } else {
            return (int) nombre;
        }
    }

    /**
     * Permet d'ajouter une alteration à une case.
     */
    public void ajouterAlter(Alteration a) {
        if (this.getAlteration() == null) { // Si la case ne possède pas d'altération.
            this.setAlteration(a); // Ajoute l'altération de case à this.
            this.getHexagone().setEffect(new Effets().putInnerShadow(Color.BLACK));
            listeCaseAlterees.add(this); // Ajoute à la case altérée à la liste des cases altérée pour décrémenter la durée.
        } else if (this.getAlteration().getClass().equals(a.getClass())) { // Sinon réinitialise sa durée.
            this.getAlteration().setDuree(a.getDuree());
        }
    }

    /**
     * Fonction de pathfinding utilisant une version de l'algorithme de Dijkstra très basique.
     *
     * @return une ArrayList contenant les cases qui compose le chemin le plus court vers l'adversaire le plus proche s'il existe.
     */
    public ArrayList<Case> pathDijkstra() {
        ArrayList<Case> chemin = new ArrayList<>();     // Chemin vers l'adversaire le plus proche.
        LinkedList<Case> file = new LinkedList<>();     // File de traitement des cases (FIFO).
        file.add(this);
        HashMap<Case, Case> depuis = new HashMap<>();   // Collection des cases parcourues et leur case de découverte.
        while (!file.isEmpty()) {
            Case v = file.pollFirst();                  // On Sort la première case de la liste.
            for (Case u : v.getCaseVoisines()) {        // On traite toutes ses cases voisines.
                if (!u.getContenu().isEmpty() && u.getEquipeContenu() != this.getEquipeContenu()) { // Si la case contient un adversaire.
                    depuis.put(u, v); // On met la case visitée dans la Collection "depuis" avec la case depuis laquelle on l'a découverte.
                    // Traçage du chemin inverse car adversaire trouvé (c'est le plus proche).
                    while (u != this && depuis.get(u) != null) { // Tant qu'on est pas sur la case de départ de l'algorithme.
                        chemin.add(u);
                        u = depuis.get(u);          // On remonte le chemin vers son origine : this.
                    }
                    chemin.add(this);
                    Collections.reverse(chemin);    // Inversion du chemin pour l'avoir dans le bon ordre.
                    return chemin;                  // On retourne le chemin vers l'adversaire le plus proche.
                } else if (!u.estOccupe() && !depuis.containsKey(u)) { // Si la case visitée est vide et n'a jamais été visitée.
                    file.addLast(u);    // On l'ajoute à la fin de la file.
                    depuis.put(u, v);   // On la met dans la Collection "depuis" avec la case depuis laquelle on l'a découverte.
                }
            }
        }
        Collections.reverse(chemin);
        return chemin; // Si l'algorithme n'a pas trouvé d'adversaire (car il est inaccessible), il retourne un chemin vide.
    }

    /**
     * Fonction de pathfinding utilisant une version de l'algorithme de Dijkstra très basique.
     *
     * @return une ArrayList contenant les cases qui compose le chemin le plus court vers l'entité "e" en paramètre.
     */
    public ArrayList<Case> pathDijkstra(Entite e) {
        ArrayList<Case> chemin = new ArrayList<>();     // Chemin vers l'adversaire le plus proche.
        LinkedList<Case> file = new LinkedList<>();     // File de traitement des cases (FIFO).
        file.add(this);
        HashMap<Case, Case> depuis = new HashMap<>();   // Collection des cases parcourues et leur case de découverte.
        while (!file.isEmpty()) {
            Case v = file.pollFirst();                  // On Sort la première case de la liste.
            for (Case u : v.getCaseVoisines()) {        // On traite toutes ses cases voisines.
                if (!u.estOccupe() && !depuis.containsKey(u)) { // Si la case visitée est vide et n'a jamais été visitée.
                    file.addLast(u);    // On l'ajoute à la fin de la file.
                    depuis.put(u, v);   // On la met dans la Collection "depuis" avec la case depuis laquelle on l'a découverte.
                } else if (!u.getContenu().isEmpty() && u.getContenu().get(0) == e) {
                    depuis.put(u, v); // On met la case visitée dans la Collection "depuis" avec la case depuis laquelle on l'a découverte.
                    // Traçage du chemin inverse car adversaire trouvé (c'est le plus proche).
                    while (u != this && depuis.get(u) != null) { // Tant qu'on est pas sur la case de départ de l'algorithme.
                        chemin.add(u);
                        u = depuis.get(u);          // On remonte le chemin vers son origine : this.
                    }
                    chemin.add(this);
                    Collections.reverse(chemin);    // Inversion du chemin pour l'avoir dans le bon ordre.
                    return chemin;                  // On retourne le chemin vers l'adversaire le plus proche.
                }
            }
        }
        Collections.reverse(chemin);
        return chemin; // Si l'algorithme n'a pas trouvé d'adversaire (car il est inaccessible), il retourne un chemin vide.
    }

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

    public ArrayList<Personne> getContenu() {
        return contenu;
    }

    public void setContenu(Personne p) {
        if (estOccupe()) {
            System.out.println("la case contient deja un obstacle/unite");
            contenu.add(p);
        }
    }

    public Group getAffichageContenu() {
        return affichageContenu;
    }

    public void setAffichageContenu(Group affichageContenu) {
        this.affichageContenu = affichageContenu;
    }

    public Alteration getAlteration() {
        return alteration;
    }

    /**
     * Méthode permettant de modifier l'altération de this.
     *
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

    public Equipe getEquipeContenu() {
        return this.getContenu().get(0).getTeam();
    }

}