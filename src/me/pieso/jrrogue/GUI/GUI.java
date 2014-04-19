package me.pieso.jrrogue.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import me.pieso.jrrogue.core.Game;

public class GUI implements Runnable {

    private JFrame frame;
    private final Game game;
    private GameArea ga;

    public GUI() {
        this.game = new Game();
    }

    @Override
    public void run() {
        frame = new JFrame("Junior Rogue");
        frame.setPreferredSize(new Dimension((15 * 32), (15 * 32)));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel pane = new JPanel(new BorderLayout());
        createComponents(pane);
        frame.add(pane);
        frame.pack();
        frame.setVisible(true);
        ga.requestFocus();
    }

    public void createComponents(Container pane) {
        
        this.ga = new GameArea(game);
        Status status = new Status(game);
        KB kb = new KB(game);
        ga.setFocusable(true);
        ga.addKeyListener(kb);
        ga.requestFocus();
        ga.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                ga.requestFocus();
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        bar.add(menu);
        menu.add(new JMenuItem("New game"));

        pane.add(bar, BorderLayout.NORTH);
        pane.add(ga, BorderLayout.CENTER);
        pane.add(status, BorderLayout.SOUTH);
    }

}
