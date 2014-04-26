package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JTextArea;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.util.GameHook;

final class Status extends JTextArea implements GameHook {

    public Status() {
        super("Status");
        setEditable(false);
        setBackground(new Color(0f, 0f, 0f, 0f));
        setForeground(Color.white);
        setBorder(null);
        setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        setLineWrap(true);
    }

    @Override
    public void hook(Game game) {
        String[] statusb = game.getPlayer().toStatus().split("\n");
        StringBuilder sb = new StringBuilder();
        for (int st = 0; st < statusb.length; st++) {
            sb.append(" ").append(statusb[st]).append(st < statusb.length - 1 ? "\n" : "");
        }
        setText(sb.toString());
    }

    @Override
    protected void paintComponent(Graphics g) {

        BufferedImage bg = ResourceManager.getImage("statusbg");
        for (int x = 0; x <= getWidth(); x += bg.getWidth() * 2) {
            for (int y = 0; y <= getHeight(); y += bg.getHeight() * 2) {
                g.drawImage(bg, x, y, bg.getWidth() * 2, bg.getHeight() * 2, null);
            }
        }
        super.paintComponent(g);
    }

}
