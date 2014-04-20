package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.GameHook;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Entity;
import me.pieso.jrrogue.entity.living.Player;

class GameArea extends JPanel implements GameHook {

    private int side;
    private Game game;

    public GameArea() {
        super.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        side = game.getSide();

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        Entity[][] data = game.getData();
        Player player = game.getPlayer();

        int mx = (getWidth() / 2) - ((player.x()) * side) - 16;
        int my = (getHeight() / 2) - ((player.y()) * side) - 16;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                // If we have gone too far
                if (data[y][x] != null) {
                    if (x * side + mx > getWidth()) {
                        break;
                    }
                    // are we there yet?
                    if ((x + 1) * side + mx > 0 && (y + 1) * side + my > 0) {
                        data[y][x].draw(g, mx + x * side, my + y * side, side);
                    }
                }
            }
            // If we have gone too far
            if ((y + 1) * side + my > getHeight()) {
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
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getWidth() / 10));
            String s = "You have died";

            Rectangle2D r2d = g.getFontMetrics().getStringBounds(s, g);

            g.setColor(Color.BLACK);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2 + 1,
                    getHeight() / 2 + (int) r2d.getHeight() / 3 + 1);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2 - 1,
                    getHeight() / 2 + (int) r2d.getHeight() / 3 + 1);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2 - 1,
                    getHeight() / 2 + (int) r2d.getHeight() / 3 - 1);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2 + 1,
                    getHeight() / 2 + (int) r2d.getHeight() / 3 - 1);

            g.setColor(Color.WHITE);
            g.drawString(s, getWidth() / 2 - (int) r2d.getWidth() / 2,
                    getHeight() / 2 + (int) r2d.getHeight() / 3);
        }
    }

    @Override
    public void hook(Game game) {
        this.game = game;
        repaint();
    }
}
