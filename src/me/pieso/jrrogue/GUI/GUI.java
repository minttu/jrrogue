package me.pieso.jrrogue.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.item.SwordItem;

public class GUI implements Runnable {

    private JFrame main;
    private final Game game;
    private GameArea ga;

    public GUI() {
        this.game = new Game();
    }

    @Override
    public void run() {
        main = new JFrame("Junior Rogue");
        main.setPreferredSize(new Dimension(20 * 32, 20 * 32));
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel pane = new JPanel(new BorderLayout());
        createMainComponents(pane);
        main.add(pane);
        main.pack();
        main.setVisible(true);
        ga.requestFocus();
    }

    public void createMainComponents(Container pane) {

        this.ga = new GameArea();
        game.addHook(ga);
        Status status = new Status();
        game.addHook(status);
        game.runHooks();
        KB kb = new KB(game);
        ga.setFocusable(true);
        ga.addKeyListener(kb);
        ga.requestFocus();
        ga.addMouseListener(new Mouse(game, ga));

        // Warning: inline stuff
        final JMenuBar bar = new JMenuBar();

        final JMenu menu = new JMenu("File");
        bar.add(menu);
        final JMenuItem newgame = new JMenuItem("New game");
        menu.add(newgame);

        final JMenu debug = new JMenu("Debug");
        bar.add(debug);
        final JCheckBoxMenuItem vision = new JCheckBoxMenuItem("Give full vision");
        debug.add(vision);
        final JMenuItem resetvision = new JMenuItem("Reset vision");
        debug.add(resetvision);
        final JMenuItem givesword = new JMenuItem("Give sword");
        debug.add(givesword);

        newgame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                // ToDo: this
            }

        });

        vision.addChangeListener(
                new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent ce) {
                        game.setVision(vision.getState());
                    }
                }
        );

        resetvision.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                game.resetVision();
                game.calculateVision();
                game.runHooks();
            }

        });

        givesword.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                game.getPlayer().inventory().add(new SwordItem());
                game.calculateVision();
                game.runHooks();
            }

        });

        pane.add(bar, BorderLayout.NORTH);

        pane.add(ga, BorderLayout.CENTER);

        pane.add(status, BorderLayout.SOUTH);
    }

}
