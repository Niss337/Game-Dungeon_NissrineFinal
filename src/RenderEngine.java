import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RenderEngine extends JPanel implements Engine {

    private ArrayList<Displayable> renderList;
    private int score;
    private int levelNumber;

    public RenderEngine() {
        renderList = new ArrayList<>();
        this.score = 0;
        this.levelNumber = 1;
    }

    public void setScore(int score) {
        this.score = score;
        this.repaint(); //
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void addToRenderList(Displayable displayable) {
        if (!renderList.contains(displayable))
            renderList.add(displayable);
    }

    public void addToRenderList(ArrayList<Displayable> displayables) {
        if (!renderList.containsAll(displayables))
            renderList.addAll(displayables);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);


        for (Displayable displayable : renderList) {
            displayable.draw(graphics);
        }


        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("SCORE: " + score, 10, 30);
        graphics.drawString("LEVEL: " + levelNumber, 10, 60);
    }

    @Override
    public void update() {
        this.repaint();
    }
}
