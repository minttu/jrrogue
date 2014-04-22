package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.util.GameHook;

final class Status extends JTextArea implements GameHook {

    public Status() {
        super("Status");
        setEditable(false);
        setBackground(Color.black); //Color.gray.darker().darker());
        setForeground(Color.white);
        setBorder(null);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setLineWrap(true);
    }

    @Override
    public void hook(Game game) {
        setText(game.getPlayer().toStatus());
    }

}
