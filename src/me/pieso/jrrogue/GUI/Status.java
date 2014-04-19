package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import me.pieso.jrrogue.core.Game;

final class Status extends JTextArea implements Runnable {

    private final Game game;

    public Status(Game game) {
        super("Status");
        setEditable(false);
        setBackground(Color.gray.darker().darker());
        setForeground(Color.white);
        setBorder(null);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        this.game = game;
        game.addHook(this);
        run();
    }

    @Override
    public void run() {
        setText(game.getPlayer().toStatus());
    }

}
