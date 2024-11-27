import javax.swing.*;
import java.awt.*;

public class Acceuil extends JPanel {
    private Image backgroundImage;

    public Acceuil(Runnable onStart) {

        backgroundImage = Toolkit.getDefaultToolkit().getImage("./img/Acceuil.png");


        JFrame frame = new JFrame("Game");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Start Your Adventure!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH); // Positionné en haut

        // Bouton "Entrer"
        JButton enterButton = new JButton("Start");
        enterButton.setFont(new Font("Arial", Font.BOLD, 24));
        enterButton.setBackground(new Color(139, 69, 19));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2));

        enterButton.addActionListener(e -> {
            onStart.run();
        });

        add(enterButton, BorderLayout.SOUTH);
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}