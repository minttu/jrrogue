package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.Player;

class GameArea extends JPanel implements Runnable {

    private final int side;
    private final Game game;

    public GameArea(Game game) {
        super.setBackground(Color.WHITE);
        this.side = 32;
        this.game = game;
        game.addHook(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.gray.darker().darker());
        g.fillRect(0, 0, getWidth(), getHeight());
        Entity[][] data = game.getData();
        Player player = game.getPlayer();

        int mx = (getWidth() / 2) - ((player.x()) * side) - 16;
        int my = (getHeight() / 2) - ((player.y()) * side) - 16;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                if ((x - 1) * side + side + mx > getWidth()) {
                    break;
                }
                data[y][x].draw(g, mx + x * side, my + y * side, side);
            }
            if (y * side + side + my > getHeight()) {
                break;
            }
        }
        for (int x = 0; x < getWidth(); x += 32) {
            g.drawImage(ResourceManager.getImage("bup"), x, 0, null);
            g.drawImage(ResourceManager.getImage("bdown"), x, getHeight() - 10, null);
        }
        for (int y = 0; y < getHeight(); y += 32) {
            g.drawImage(ResourceManager.getImage("bleft"), 0, y, null);
            g.drawImage(ResourceManager.getImage("bright"), getWidth() - 10, y, null);
        }
        g.drawImage(ResourceManager.getImage("cul"), 0, 0, null);
        g.drawImage(ResourceManager.getImage("cur"), getWidth() - 12, 0, null);
        g.drawImage(ResourceManager.getImage("cdl"), 0, getHeight() - 12, null);
        g.drawImage(ResourceManager.getImage("cdr"), getWidth() - 12, getHeight() - 12, null);

        if (!player.living()) {
            g.setFont(new Font("monospace", Font.BOLD, getWidth() / 10));
            String s = "You have died";
            
            Rectangle2D r2d = g.getFontMetrics().getStringBounds(s, g);
            
            g.setColor(Color.BLACK);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2 + 4,
                    getHeight() / 2 + (int) r2d.getHeight() / 3 + 4);
            
            g.setColor(Color.RED.darker());
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2,
                    getHeight() / 2 + (int) r2d.getHeight() / 3);
        }
    }

    @Override
    public void run() {
        repaint();
    }
}
