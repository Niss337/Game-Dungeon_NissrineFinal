import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends Sprite {

    private boolean collected = false;

    public Coin(double x, double y, BufferedImage image) {
        super(x, y, 32, 32, image, "Coin");
    }

    // Vérifier si la pièce est collectée
    public boolean isCollected(DynamicSprite hero) {
        if (this.getBounds().intersects(hero.getBounds())) {
            collected = true;  // Marquer la pièce comme collectée
            return true;  // Retourner vrai si collectée
        }
        return false;  // Sinon, retourner faux
    }

    @Override
    public void draw(Graphics g) {
        if (!collected) {  // Si la pièce n'est pas collectée, dessiner la pièce
            super.draw(g);
        }
    }

    public boolean isCollected() {
        return collected;
    }
}
