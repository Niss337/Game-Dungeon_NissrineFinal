import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite implements KeyListener {
    private boolean isWalking;
    private double speed;
    private double oldSpeed;
    private int spriteSheetNumberOfColumn;
    private double timeBetweenFrame;
    private Direction direction;
    private boolean isHero;


    public DynamicSprite(double x, double y, double width, double height, Image image, String name, boolean isHero) {
        super(x, y, width, height, image, name);
        this.isWalking = false;
        this.speed = 6;
        this.oldSpeed = 6;
        this.spriteSheetNumberOfColumn = 10;
        this.timeBetweenFrame = 250;
        this.direction = Direction.EAST;
        this.isHero = isHero;
    }


    public DynamicSprite(double x, double y, double width, double height, Image image, String name, boolean isWalking, double speed, int spriteSheetNumberOfColumn, double timeBetweenFrame, Direction direction, boolean isHero) {
        super(x, y, width, height, image, name);
        this.isWalking = isWalking;
        this.oldSpeed = speed;
        this.speed = speed;
        this.spriteSheetNumberOfColumn = spriteSheetNumberOfColumn;
        this.timeBetweenFrame = timeBetweenFrame;
        this.direction = direction;
        this.isHero = isHero;
    }

    @Override
    public void draw(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;


        int dynamicSpriteImage = 0;
        if (isWalking) {
            dynamicSpriteImage = (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn);
        }


        graphics.drawImage(
                image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (dynamicSpriteImage * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((dynamicSpriteImage + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null
        );
    }


    public void moveIfPossible(ArrayList<Sprite> environment, ArrayList<DynamicSprite> dynamicSprites) {
        if (isMovingPossible(environment, dynamicSprites)) {
            move();
        }
    }


    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }


    private boolean isMovingPossible(ArrayList<Sprite> environment, ArrayList<DynamicSprite> dynamicSprites) {
        Rectangle2D.Double anticipatedMove = new Rectangle2D.Double();
        switch (direction) {
            case NORTH:
                anticipatedMove.setRect(super.getHitBox().getX(), super.getHitBox().getY() - speed, super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case SOUTH:
                anticipatedMove.setRect(super.getHitBox().getX(), super.getHitBox().getY() + speed, super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case EAST:
                anticipatedMove.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY(), super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
            case WEST:
                anticipatedMove.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY(), super.getHitBox().getWidth(), super.getHitBox().getHeight());
                break;
        }

        //collisions
        for (Sprite sprite : environment) {
            if (sprite instanceof SolidSprite) {
                if (((SolidSprite) sprite).intersect(anticipatedMove)) {

                    if (sprite instanceof LevelSprite && ((LevelSprite) sprite).isFinalLevel()) {
                        Main.gagner();
                        return false;
                    }


                    isWalking = false;
                    return false;
                }
            }
        }

        isWalking = true;
        return true;
    }


    private void move() {
        switch (direction) {
            case NORTH -> this.y -= speed;
            case SOUTH -> this.y += speed;
            case EAST -> this.x += speed;
            case WEST -> this.x -= speed;
        }
    }


    public void speedUp() {
        this.speed = oldSpeed * 2.5; // Doubler la vitesse
    }

    public void speedDown() {
        this.speed = oldSpeed; // Rétablir la vitesse normale
    }

    public void setWalking(boolean isWalking) {
        this.isWalking = isWalking;
    }
    public double getSpeed() {
        return speed;
    }

    public boolean isWalking() {
        return isWalking;
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> setDirection(Direction.NORTH);
            case KeyEvent.VK_DOWN -> setDirection(Direction.SOUTH);
            case KeyEvent.VK_LEFT -> setDirection(Direction.WEST);
            case KeyEvent.VK_RIGHT -> setDirection(Direction.EAST);
            case KeyEvent.VK_SHIFT -> speedUp();
        }
        isWalking = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT -> speedDown();
        }
        isWalking = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
