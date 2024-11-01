package game.tetris;

import java.awt.GridLayout;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

	// é um número sem sentido para suprimir um alerta desnecessário.
	private static final long serialVersionUID = 8737819995837542594L;

	public GameWindow() {
		setTitle("Tetris :D");
		setSize(400, 814);
		setResizable(false);

		setLayout(new GridLayout(1, 2));

		// você pode ajustar a resolução do temporizador aqui, mas este é o valor ideal para este jogo.
		GameBoardPanel gameBoard = new GameBoardPanel(this, 400);
		add(gameBoard);
		gameBoard.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}