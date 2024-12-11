package game.tetris;

import javax.swing.*;
import java.awt.*;

public class GameFinale extends JFrame {

    private static final long serialVersionUID = 1L;

    public GameFinale(GameWindow parentWindow) {
        // Configurações da janela de "Fim de Jogo"
        setTitle("Game Over");
        setSize(300, 130); // Ajuste no tamanho da janela para comportar o espaçamento
        setLocationRelativeTo(null); // Centraliza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuração do painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10)); // Espaçamento entre os elementos

        // Mensagem de fim de jogo
        JLabel message = new JLabel("Fim de jogo. Deseja tentar novamente?", JLabel.CENTER);
        panel.add(message, BorderLayout.NORTH); // Adiciona a mensagem no topo

        // Painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Espaçamento entre os botões

        JButton playAgainButton = new JButton("Jogar Novamente");
        JButton exitButton = new JButton("Sair");

        // Ação do botão "Jogar Novamente"
        playAgainButton.addActionListener(e -> {
            dispose(); // Fecha a janela de fim de jogo
            parentWindow.dispose(); // Fecha a janela do jogo antigo
            new GameWindow(); // Reinicia o jogo criando uma nova instância de GameWindow
        });

        // Ação do botão "Sair"
        exitButton.addActionListener(e -> System.exit(0)); // Encerra a aplicação

        // Adiciona os botões ao painel de botões
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        // Adiciona os componentes ao painel principal
        panel.add(message, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Adiciona o painel principal à janela
        this.add(panel);
        setVisible(true); // Exibe a janela
    }
}
