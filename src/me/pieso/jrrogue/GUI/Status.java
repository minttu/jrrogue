package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextArea;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.util.GameHook;
import me.pieso.jrrogue.util.GraphicsUtils;

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
        int cur = 0;
        for (String statusb1 : statusb) {
            if (statusb1.contains("\r")) {
                sb.append(statusb1).append("\n");
            } else {
                cur += statusb1.length();
                if (cur > getWidth() / getColumnWidth()) {
                    cur = 0;
                    sb.append("\n");
                }
                if (cur == 0 && statusb1.charAt(0) == ' ') {
                    sb.append(statusb1.substring(1));
                } else {
                    sb.append(statusb1);
                }
            }
        }
        setText(sb.toString());
        //setText(game.getPlayer().toStatus());
    }

    @Override
    protected void paintComponent(Graphics g) {

        BufferedImage bg = ResourceManager.getImage("statusbg");
        for (int x = 0; x <= getWidth(); x += bg.getWidth() * 2) {
            for (int y = 0; y <= getHeight(); y += bg.getHeight() * 2) {
                g.drawImage(bg, x, y, bg.getWidth() * 2, bg.getHeight() * 2, null);
            }
        }
        //super.paintComponent(g);
        g.setFont(getFont());
        g.setColor(getForeground());
        String[] status = getText().split("\n");
        for (int i = 0; i < status.length; i++) {
            GraphicsUtils.drawTextOutlined(g, new Rectangle(4, (int) (getFontMetrics(getFont()).getHeight() * (i + 0.75)), 0, 0), status[i], Color.white, Color.black);
        }
    }

}
