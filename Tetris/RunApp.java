package game.tetris;

public class RunApp {
	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8"); // Força a codificação UTF-8
		new GameWindow();
	}
}