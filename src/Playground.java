import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Playground {
    private static ArrayList<Sprite> environment;

    public Playground(String level) {
        environment = new ArrayList<>();
        try {

            Image grass = ImageIO.read(new File(Main.getImagePath() + "grass.png"));
            Image rock = ImageIO.read(new File(Main.getImagePath() + "rock.png"));
            Image tree = ImageIO.read(new File(Main.getImagePath() + "tree.png"));
            Image doorfinal = ImageIO.read(new File(Main.getImagePath() + "doorfinal.png"));
            Image trap = ImageIO.read(new File(Main.getImagePath() + "trap.png"));  // Image du piège
            Image coinImage = ImageIO.read(new File(Main.getImagePath() + "coin.png"));  // Image de la pièce


            BufferedReader bufferedReader = new BufferedReader(new FileReader(level));
            int row = 0;
            int column = 0;
            String line = bufferedReader.readLine();
            while (line != null) {
                column = 0;
                for (byte character : line.getBytes(StandardCharsets.UTF_8)) {
                    switch (character) {
                        case 'T': // Arbre
                            environment.add(new SolidSprite(
                                    column * tree.getWidth(null),
                                    row * tree.getHeight(null),
                                    tree.getWidth(null),
                                    tree.getHeight(null),
                                    tree,
                                    "tree"
                            ));
                            break;
                        case ' ' : // Herbe
                            environment.add(new Sprite(
                                    column * grass.getWidth(null),
                                    row * grass.getHeight(null),
                                    grass.getWidth(null),
                                    grass.getHeight(null),
                                    grass,
                                    "grass"
                            ));
                            break;
                        case 'R': // Roche
                            environment.add(new SolidSprite(
                                    column * rock.getWidth(null),
                                    row * rock.getHeight(null),
                                    rock.getWidth(null),
                                    rock.getHeight(null),
                                    rock,
                                    "rock"
                            ));
                            break;
                        case '.': // Ancienne porte remplacée par un sprite générique
                        case 'E': // Point de sortie remplacé par un sprite générique
                            environment.add(new LevelSprite(
                                    column * grass.getWidth(null),
                                    row * grass.getHeight(null),
                                    grass.getWidth(null),
                                    grass.getHeight(null),
                                    grass,
                                    "exit"
                            ));
                            break;
                        case 'D': // Porte finale
                            environment.add(new LevelSprite(
                                    column * doorfinal.getWidth(null),
                                    row * doorfinal.getHeight(null),
                                    doorfinal.getWidth(null),
                                    doorfinal.getHeight(null),
                                    doorfinal,
                                    "doorfinal"
                            ));
                            break;
                        case 'O': // Piège
                            environment.add(new SolidSprite(
                                    column * trap.getWidth(null),
                                    row * trap.getHeight(null),
                                    trap.getWidth(null),
                                    trap.getHeight(null),
                                    trap,
                                    "trap"
                            ));
                            break;
                    }
                    column++;
                }
                row++;
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public LevelSprite getExitPoint() {
        for (Sprite sprite : environment) {
            if (sprite instanceof LevelSprite && "exit".equals(sprite.getName())) {
                return (LevelSprite) sprite;
            }
        }
        return null;
    }


    public ArrayList<Sprite> getSolidSpriteList() {
        ArrayList<Sprite> solidSpriteList = new ArrayList<>();
        for (Sprite sprite : environment) {
            if (sprite instanceof SolidSprite) {
                solidSpriteList.add(sprite);
            }
        }
        return solidSpriteList;
    }


    public LevelSprite getWinPoint() {
        for (Sprite sprite : environment) {
            if (sprite instanceof LevelSprite && "doorfinal".equals(sprite.getName())) {
                return (LevelSprite) sprite;
            }
        }
        return null;
    }


    public ArrayList<Displayable> getSpriteList() {
        return new ArrayList<>(environment);
    }


    public boolean isValidCoinPosition(int x, int y) {

        for (Sprite sprite : getSolidSpriteList()) {
            if (sprite.getBounds().intersects(new Rectangle(x, y, 48, 48))) {
                return false;
            }
        }
        return true;
    }
}
