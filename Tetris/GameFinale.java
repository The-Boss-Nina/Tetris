package game.tetris;

import javax.swing.*;
import java.awt.*;

public class GameFinale extends JFrame {

    private static final long serialVersionUID = 1L;

    public GameFinale(GameWindow parentWindow) {
        setTitle("Game Over");
        setSize(300, 130); 
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); 

        JLabel message = new JLabel("Fim de jogo. Deseja tentar novamente?", JLabel.CENTER);
        panel.add(message, BorderLayout.NORTH); 

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10)); 

        JButton playAgainButton = new JButton("Jogar Novamente");
        JButton exitButton = new JButton("Sair");

        playAgainButton.addActionListener(e -> {
            dispose(); 
            parentWindow.dispose(); 
            new GameWindow(); 
        });

        exitButton.addActionListener(e -> System.exit(0)); 

        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        panel.add(message, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        this.add(panel);
        setVisible(true); 
    }
}
