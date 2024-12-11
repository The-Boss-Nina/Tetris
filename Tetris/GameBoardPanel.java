package game.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.SwingUtilities;

import game.tetris.Tetromino.Tetrominoes;

public class GameBoardPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 6802492405004738658L;

    // Configurações do tabuleiro
    private static final int BoardWidth = 10;
    private static final int BoardHeight = 20;

    // Status do jogo e timer
    private Timer timer;
    private boolean isFallingDone = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int currentScore = 0;

    // Tetromino atual
    private Tetromino curBlock;
    private int curX = 0;
    private int curY = 0;

    // Tabuleiro do jogo
    private Tetrominoes[] gameBoard;
    private Color[] colorTable;

    public GameBoardPanel(GameWindow tetrisFrame, int timerResolution) {
        setFocusable(true);
        setBackground(new Color(0, 30, 30));
        curBlock = new Tetromino();
        timer = new Timer(timerResolution, this);
        timer.start();

        gameBoard = new Tetrominoes[BoardWidth * BoardHeight];

        // Cores dos tetrominos
        colorTable = new Color[] {
            new Color(0, 0, 0), new Color(238, 64, 53),
            new Color(243, 119, 54), new Color(255, 201, 14),
            new Color(123, 192, 67), new Color(3, 146, 207),
            new Color(235, 214, 135), new Color(164, 135, 235)
        };

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isStarted || curBlock.getShape() == Tetrominoes.NO_BLOCK) {
                    return;
                }

                int keycode = e.getKeyCode();

                if (keycode == 'p' || keycode == 'P') {
                    pause();
                    return;
                }

                if (isPaused) {
                    return;
                }

                switch (keycode) {
                    case KeyEvent.VK_LEFT:
                        isMovable(curBlock, curX - 1, curY);
                        break;
                    case KeyEvent.VK_RIGHT:
                        isMovable(curBlock, curX + 1, curY);
                        break;
                    case KeyEvent.VK_UP:
                        isMovable(curBlock.rotateRight(), curX, curY);
                        break;
                    case KeyEvent.VK_DOWN:
                        advanceOneLine();
                        break;
                    case KeyEvent.VK_SPACE:
                        advanceToEnd();
                        break;
                }
            }
        });

        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < BoardWidth * BoardHeight; i++) {
            gameBoard[i] = Tetrominoes.NO_BLOCK;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingDone) {
            isFallingDone = false;
            newTetromino();
        } else {
            advanceOneLine();
        }
    }

    public void start() {
        if (isPaused) {
            return;
        }

        isStarted = true;
        isFallingDone = false;
        currentScore = 0;
        initBoard();

        newTetromino();
        timer.start();
    }

    public void pause() {
        if (!isStarted) {
            return;
        }

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }

        repaint();
    }

    private int blockWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    private int blockHeight() {
        return (int) getSize().getHeight() / BoardHeight;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    
        if (!isPaused) {
            Graphics2D g2d = (Graphics2D) g;
    
            String currentStatus = "Score: " + currentScore;
            String currentLevel = "Level: " + (currentScore / 10 + 1);
    
            g2d.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString(currentStatus, 15, 35);
            g2d.drawString(currentLevel, 15, 70);
        }

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * blockHeight();

        // Renderiza o tabuleiro
        for (int i = 0; i < BoardHeight; i++) {
            for (int j = 0; j < BoardWidth; j++) {
                Tetrominoes shape = curTetrominoPos(j, BoardHeight - i - 1);
                if (shape != Tetrominoes.NO_BLOCK) {
                    drawTetromino(g, j * blockWidth(), boardTop + i * blockHeight(), shape, false);
                }
            }
        }

        // Renderiza o tetromino atual
        if (curBlock.getShape() != Tetrominoes.NO_BLOCK) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curBlock.getX(i);
                int y = curY - curBlock.getY(i);
                drawTetromino(g, x * blockWidth(), boardTop + (BoardHeight - y - 1) * blockHeight(),
                    curBlock.getShape(), false);
            }
        }
    }

    private void drawTetromino(Graphics g, int x, int y, Tetrominoes shape, boolean isShadow) {
        Color curColor = colorTable[shape.ordinal()];
        g.setColor(isShadow ? curColor.darker().darker() : curColor);
        g.fillRect(x + 1, y + 1, blockWidth() - 2, blockHeight() - 2);
    }

    private boolean isMovable(Tetromino chkBlock, int chkX, int chkY) {
        for (int i = 0; i < 4; i++) {
            int x = chkX + chkBlock.getX(i);
            int y = chkY - chkBlock.getY(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (curTetrominoPos(x, y) != Tetrominoes.NO_BLOCK) {
                return false;
            }
        }
        curBlock = chkBlock;
        curX = chkX;
        curY = chkY;
        repaint();
        return true;
    }

    private void advanceOneLine() {
        if (!isMovable(curBlock, curX, curY - 1)) {
            tetrominoFixo();
        }
    }

    private void advanceToEnd() {
        int tempY = curY;
        while (tempY > 0) {
            if (!isMovable(curBlock, curX, tempY - 1)) {
                break;
            }
            tempY--;
        }
        curY = tempY;
        tetrominoFixo();
    }

    private void tetrominoFixo() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curBlock.getX(i);
            int y = curY - curBlock.getY(i);
            gameBoard[(y * BoardWidth) + x] = curBlock.getShape();
        }
        removeFullLines();
        if (!isFallingDone) {
            newTetromino();
        }
    }

    private void removeFullLines() {
        int fullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; i--) {
            boolean isFull = true;

            for (int j = 0; j < BoardWidth; j++) {
                if (curTetrominoPos(j, i) == Tetrominoes.NO_BLOCK) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                fullLines++;
                for (int k = i; k < BoardHeight - 1; k++) {
                    for (int l = 0; l < BoardWidth; l++) {
                        gameBoard[(k * BoardWidth) + l] = curTetrominoPos(l, k + 1);
                    }
                }
            }
        }

        if (fullLines > 0) {
            currentScore += fullLines;
            isFallingDone = true;
            curBlock.setShape(Tetrominoes.NO_BLOCK);
            repaint();
        }
    }

    private Tetrominoes curTetrominoPos(int x, int y) {
        return gameBoard[(y * BoardWidth) + x];
    }

    private void newTetromino() {
        curBlock.setRandomShape();
        curX = BoardWidth / 2 - 1;
        curY = BoardHeight - 1 + curBlock.minY();
    
        if (!isMovable(curBlock, curX, curY)) {
            curBlock.setShape(Tetrominoes.NO_BLOCK);
            timer.stop();
            isStarted = false;
    
            SwingUtilities.invokeLater(() -> new GameFinale((GameWindow) SwingUtilities.getWindowAncestor(this)));
        }
    }
}
    