import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameEngine implements KeyListener, Engine {

    private DynamicSprite hero;
    private boolean isMoving;

    public GameEngine(DynamicSprite hero) {
        this.hero = hero;
        this.isMoving = true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (isMoving) hero.setDirection(Direction.NORTH);
                break;
            case KeyEvent.VK_DOWN:
                if (isMoving) hero.setDirection(Direction.SOUTH);
                break;
            case KeyEvent.VK_LEFT:
                if (isMoving) hero.setDirection(Direction.WEST);
                break;
            case KeyEvent.VK_RIGHT:
                if (isMoving) hero.setDirection(Direction.EAST);
                break;
            case KeyEvent.VK_SHIFT:
                if (isMoving) hero.speedUp();
                break;
            case KeyEvent.VK_SPACE: //
                isMoving = !isMoving; // Bascule
                if (!isMoving) {
                    hero.setDirection(Direction.EAST);
                    hero.setWalking(false);
                } else {
                    hero.setWalking(true);
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            hero.speedDown();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void update() {

    }
}
