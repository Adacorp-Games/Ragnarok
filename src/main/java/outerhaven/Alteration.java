package outerhaven;

import javafx.scene.image.Image;
import outerhaven.Personnages.PersonnagesMagiques.Alchimiste;
import outerhaven.Personnages.PersonnagesMagiques.PersonneMagique;
import outerhaven.Personnages.Personne;

public class Alteration {
    private Image image;
    private String effet;
    private int puissance;
    private int durée;
    private int timer = Plateau.nbTour;

    public Alteration(String effet, int puissance, int durée) {
        this.effet = effet;
        this.puissance = puissance;
        this.durée = durée;
        if (effet == "poison") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonToxic.png"));
        } else if (effet == "manaVore") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonManaVore.png"));
        } else if (effet == "heal") {
            this.image = new Image(Alteration.class.getResourceAsStream("/Images/Cases/hexagonHeal.png"));
        }
    }

    public void appliquerEffet(Personne p) {
        if (this.effet == "poison" && p.getClass() != Alchimiste.class) {
            p.setHealth(p.getHealth() - this.puissance * 5);
        } else if (this.effet == "heal" && p.getHealth() <= p.getMaxHealth() - this.puissance * 5) {
            p.setHealth(p.getHealth() + this.puissance * 5);
        } else if (this.effet == "heal" && p.getHealth() > p.getMaxHealth() - this.puissance * 5) {
            p.setHealth(p.getMaxHealth());
        }
    }

    public void appliquerEffet(PersonneMagique p) {
        if (this.effet == "manaVore") {
            if (p.getMana() >= 35) {
                p.setMana(p.getMana() - 35);
            } else if (p.getMana() < 35) {
                p.setMana(0);
            }
        }
    }

    // Getter et setter

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getEffet() {
        return effet;
    }

    public void setEffet(String effet) {
        this.effet = effet;
    }

    public int getPuissance() {
        return puissance;
    }

    public void setPuissance(int puissance) {
        this.puissance = puissance;
    }

    public int getDurée() {
        return durée;
    }

    public void setDurée(int durée) {
        this.durée = durée;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }
}
