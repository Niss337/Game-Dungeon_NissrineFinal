import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private final static String imagePath = "./img/";
    private final static String dataPath = "./data/";
    private static int levelNumber = 1;
    private static int score = 0;
    private static JFrame displayZoneFrame;

    private static RenderEngine renderEngine;
    private static PhysicsEngine physicsEngine;

    private static Timer renderTimer;
    private static Timer gameTimer;
    private static Timer physicTimer;

    private static List<Coin> coins = new ArrayList<>();

    public Main() {
        displayZoneFrame = new JFrame();
        displayZoneFrame.setTitle("Dungeon");
        displayZoneFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadNextLevel();

        displayZoneFrame.setVisible(true);
        displayZoneFrame.setSize(400, 600);
    }

    public static void increaseLevel() {
        levelNumber++;
    }

    public static int getLevelNumber() {
        return levelNumber;
    }

    public static String getImagePath() {
        return imagePath;
    }

    public static void loadNextLevel() {
        try {
            Playground newLevel = new Playground(dataPath + "level" + levelNumber + ".txt");


            File heroImageFile = new File(imagePath + "heroTileSheetLowRes.png");
            DynamicSprite hero = new DynamicSprite(60, 300, 48, 50, ImageIO.read(heroImageFile), "Hero", true);


            LevelSprite exitPoint = newLevel.getExitPoint();
            LevelSprite winPoint = newLevel.getWinPoint();

            if (exitPoint == null) {
                throw new IllegalStateException("Exit point 'E' is missing in the level data.");
            }


            File coinImageFile = new File(imagePath + "coin.png");
            BufferedImage coinImage = ImageIO.read(coinImageFile);
            coins.clear();
            coins.add(new Coin(100, 100, coinImage));
            coins.add(new Coin(200, 150, coinImage));
            coins.add(new Coin(300, 200, coinImage));
            coins.add(new Coin(200, 300, coinImage));

            renderEngine = new RenderEngine();
            physicsEngine = new PhysicsEngine();

            stopTimers();

            //  vérification des pièces collectées et des points "E" et "D"
            gameTimer = new Timer(50, (time) -> {

                checkCoinCollection(hero);

                // il a collecté tous les pieces
                if (winPoint != null && coins.isEmpty() && winPoint.getBounds().intersects(hero.getBounds())) {
                    gagner(); // Afficher le message de victoire
                }
                // sortie E
                else if (exitPoint.getBounds().intersects(hero.getBounds())) {
                    increaseLevel();
                    loadNextLevel();
                }
            });


            renderTimer = new Timer(50, (time) -> renderEngine.update());
            physicTimer = new Timer(50, (time) -> physicsEngine.update());

            renderTimer.start();
            gameTimer.start();
            physicTimer.start();

            physicsEngine.setEnvironment(newLevel.getSolidSpriteList());
            renderEngine.addToRenderList(newLevel.getSpriteList());
            renderEngine.addToRenderList(hero);
            renderEngine.addToRenderList(exitPoint);

            if (winPoint != null) {
                renderEngine.addToRenderList(winPoint);
            }

            physicsEngine.addToMovingSpriteList(hero);

            for (Coin coin : coins) {
                renderEngine.addToRenderList(coin);
            }

            renderEngine.setScore(score);
            renderEngine.setLevelNumber(levelNumber);

            displayZoneFrame.getContentPane().removeAll();
            displayZoneFrame.getContentPane().add(renderEngine);
            displayZoneFrame.addKeyListener((KeyListener) hero);
            displayZoneFrame.revalidate();
            displayZoneFrame.repaint();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void checkCoinCollection(DynamicSprite hero) {
        for (Coin coin : new ArrayList<>(coins)) {
            if (coin.isCollected(hero)) {
                incrementScore();
                coins.remove(coin);
            }
        }
    }

    public static void incrementScore() {
        score++;
        renderEngine.setScore(score);
    }

    private static void stopTimers() {
        if (renderTimer != null) {
            renderTimer.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (physicTimer != null) {
            physicTimer.stop();
        }
    }

    public static void gagner() {
        stopTimers();

        int choice = JOptionPane.showOptionDialog(null, "Congratulations, you won the maze!",
                "Congratulations", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new Object[]{"Exit", "Retry"}, "Retry");

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {

            levelNumber = 1;
            score = 0;
            loadNextLevel();
        }
    }


    public static int getScore() {
        return score;
    }

    public static void setScore(int newScore) {
        score = newScore;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Acceuil(() -> {
                try {
                    new Main();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
