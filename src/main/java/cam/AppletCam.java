package cam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JApplet;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class AppletCam extends JApplet {

    final static int BLANC = -1;
    final static int NOIR = 1;
    final static int VIDE = 0;
    final static int MAN = 1;
    final static int KNIGHT = 2;
    final static int CASTLE = 3;
    final static int INTERDIT = 10;

    final private static int LARGEUR = 7;
    final private static int HAUTEUR = 13;
    final private static int COIN = 3;
    final private static int TAILLEPIONS = 40;

    private static final long serialVersionUID = 1L;
    private JList brdList;
    private CamBoard displayBoard;
    private JScrollPane scrollPane;
    private DefaultListModel listModel;
    private Frame myFrame;

    static int cpt = 0;

    @Override
    public void init() {

        System.out.println("Initialisation BoardApplet" + cpt++);
        buildUI(getContentPane());
    }

    public void buildUI(Container container) {
        setBackground(Color.white);

        int[][] temp = new int[HAUTEUR][LARGEUR];

        for (int i = 0; i < HAUTEUR; i++) {
            for (int j = 0; j < LARGEUR; j++) {
                temp[i][j] = VIDE;
            }
        }

        displayBoard = new CamBoard("Coups", temp);

        listModel = new DefaultListModel();
        listModel.addElement(displayBoard);

        brdList = new JList(listModel);
        brdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        brdList.setSelectedIndex(0);
        scrollPane = new JScrollPane(brdList);
        Dimension d = scrollPane.getSize();
        scrollPane.setPreferredSize(new Dimension(200, d.height));

        brdList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                brdList_keyPressed(e);
            }
        });
        brdList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                brdList_mouseClicked(e);
            }
        });
        container.add(displayBoard, BorderLayout.CENTER);
        container.add(scrollPane, BorderLayout.EAST);
    }

    public void update(Graphics g, Insets in) {
        Insets tempIn = in;
        g.translate(tempIn.left, tempIn.top);
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        displayBoard.paint(g);
    }

    public void addBoard(String move, int[][] board) {
        CamBoard tempEntrop = new CamBoard(move, board);
        listModel.addElement(new CamBoard(move, board));
        brdList.setSelectedIndex(listModel.getSize() - 1);
        brdList.ensureIndexIsVisible(listModel.getSize() - 1);
        displayBoard = tempEntrop;
        update(myFrame.getGraphics(), myFrame.getInsets());
    }

    public void setMyFrame(Frame f) {
        myFrame = f;
    }

    void brdList_keyPressed(KeyEvent e) {
        int index = brdList.getSelectedIndex();
        if (e.getKeyCode() == KeyEvent.VK_UP && index > 0) {
            displayBoard = (CamBoard) listModel.getElementAt(index - 1);
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && index < (listModel.getSize() - 1)) {
            displayBoard = (CamBoard) listModel.getElementAt(index + 1);
        }

        update(myFrame.getGraphics(), myFrame.getInsets());

    }

    void brdList_mouseClicked(MouseEvent e) {
        displayBoard = (CamBoard) listModel.getElementAt(brdList.getSelectedIndex());
        update(myFrame.getGraphics(), myFrame.getInsets());
    }

    // Sous classe qui dessine le plateau de jeu
    class CamBoard extends JPanel {

        private static final long serialVersionUID = 1L;
        int[][] boardState;
        String move;

	// The string will be the move details
        // and the array the details of the board after the move has been applied.
        public CamBoard(String mv, int[][] bs) {
            boardState = bs;
            move = mv;
        }

        public void drawBoard(Graphics g) {
	    // First draw the lines
            // Board
            int bx = 30;
            int by = 30;
            boolean alt = true;

            // axis labels
            g.setColor(new Color(0, 0, 0));
            for (int i = 1; i <= LARGEUR; i++) {
                g.drawString("" + (char) ('A' + i - 1), i * TAILLEPIONS + 10, 20);
            }
            for (int i = 1; i <= HAUTEUR; i++) {
                g.drawString("" + i, 5, i * TAILLEPIONS + 10);
            }

            // draw the squares
            Color c1 = new Color(210, 210, 210);
            Color c2 = new Color(190, 190, 190);
            for (int i = 0; i < TAILLEPIONS * LARGEUR; i += TAILLEPIONS) {
                for (int j = 0; j < TAILLEPIONS * HAUTEUR; j += TAILLEPIONS) {
                    g.setColor(alt ? c1 : c2);
                    alt = !alt;
                    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
                }
            }

            // make the corners
            g.setColor(Color.white);

            for (int i = 0; i < TAILLEPIONS * COIN; i += TAILLEPIONS) {
                for (int j = 0; j < TAILLEPIONS * COIN - i; j += TAILLEPIONS) {
                    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
                }
            }
            for (int i = TAILLEPIONS * (LARGEUR - COIN); i < TAILLEPIONS * LARGEUR; i += TAILLEPIONS) {
                for (int j = 0; j < i - TAILLEPIONS * (LARGEUR - COIN - 1); j += TAILLEPIONS) {
                    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
                }
            }
            for (int j = TAILLEPIONS * (HAUTEUR - COIN); j < TAILLEPIONS * HAUTEUR; j += TAILLEPIONS) {
                for (int i = 0; i < TAILLEPIONS * COIN + j - TAILLEPIONS * (HAUTEUR - 1); i += TAILLEPIONS) {
                    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
                }
            }
            for (int j = TAILLEPIONS * (HAUTEUR - COIN); j < TAILLEPIONS * HAUTEUR; j += TAILLEPIONS) {
                for (int i = TAILLEPIONS * (LARGEUR - COIN) - j + TAILLEPIONS * (HAUTEUR - 1); i < TAILLEPIONS
                        * LARGEUR; i += TAILLEPIONS) {
                    g.fillRect(bx + i, by + j, TAILLEPIONS, TAILLEPIONS);
                }
            }

            // Draw the pieces by referencing boardState array
            for (int i = 0; i < LARGEUR; i++) {
                for (int j = 0; j < HAUTEUR; j++) {
                    switch (boardState[j][i]) {
                        case (BLANC * MAN):
                            g.setColor(new Color(255, 255, 255));
                            g.fillOval(bx + TAILLEPIONS * i + 6, by + TAILLEPIONS * j + 6, TAILLEPIONS - 12,
                                    TAILLEPIONS - 12);
                            break;
                        case (BLANC * KNIGHT):
                            g.setColor(new Color(255, 255, 255));
                            g.fillRoundRect(bx + TAILLEPIONS * i + 6, by + TAILLEPIONS * j + 6, TAILLEPIONS - 12,
                                    TAILLEPIONS - 12, 9, 9);
                            break;
                        case (BLANC * CASTLE):
                            g.setColor(new Color(245, 245, 245));
                            g.fillRect(bx + TAILLEPIONS * i + 2, by + TAILLEPIONS * j + 2, TAILLEPIONS - 4, TAILLEPIONS - 4);
                            break;
                        case (NOIR * MAN):
                            g.setColor(new Color(0, 0, 0));
                            g.fillOval(bx + TAILLEPIONS * i + 6, by + TAILLEPIONS * j + 6, TAILLEPIONS - 12,
                                    TAILLEPIONS - 12);
                            break;
                        case (NOIR * KNIGHT):
                            g.setColor(new Color(0, 0, 0));
                            g.fillRoundRect(bx + TAILLEPIONS * i + 6, by + TAILLEPIONS * j + 6, TAILLEPIONS - 12,
                                    TAILLEPIONS - 12, 9, 9);
                            break;
                        case (NOIR * CASTLE):
                            g.setColor(new Color(10, 10, 10));
                            g.fillRect(bx + TAILLEPIONS * i + 2, by + TAILLEPIONS * j + 2, TAILLEPIONS - 4, TAILLEPIONS - 4);
                            break;
                    }
                }
            }
        }

        @Override
        public void paint(Graphics g) {
            drawBoard(g);
        }

        @Override
        public void update(Graphics g) {
            drawBoard(g);
        }

        @Override
        public String toString() {
            return move;
        }
    }
}
