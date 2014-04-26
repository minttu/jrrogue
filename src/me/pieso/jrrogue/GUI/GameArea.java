package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.living.Monster;
import me.pieso.jrrogue.entity.living.Player;
import me.pieso.jrrogue.entity.pickup.Pickup;
import me.pieso.jrrogue.entity.trap.Trap;
import me.pieso.jrrogue.util.GameHook;
import me.pieso.jrrogue.util.GraphicsUtils;

class GameArea extends JPanel implements GameHook {

    private int side;
    private Game game;
    private int lastoffsetx;
    private int lastoffsety;

    public GameArea() {
        super.setBackground(Color.WHITE);
    }

    public int offsetX() {
        return lastoffsetx;
    }

    public int offsetY() {
        return lastoffsety;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        side = game.getSide();

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        Floor[][] data = (Floor[][]) game.getData();
        Player player = game.getPlayer();

        int mx = (getWidth() / 2) - ((player.x()) * side) - 16;
        int my = (getHeight() / 2) - ((player.y()) * side) - 16;
        lastoffsetx = mx;
        lastoffsety = my;

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
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

        // minimap
        int per = Math.max(1, getWidth() / 4 / data.length);
        /*g.setColor(new Color(1f, 1f, 1f, .5f));
         g.drawRect(12, 12, (data[0].length + 1) * per - 1, data.length * per);
         g.drawRect(13, 13, (data[0].length + 1) * per - 3, data.length * per - 2);
         g.setColor(new Color(1f, 1f, 1f, .25f));
         g.fillRect(14, 14, (data[0].length + 1) * per - 3, data[0].length * per - 2);*/
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                if (data[y][x] != null) {
                    if (player.x() == x && player.y() == y) {
                        g.setColor(new Color(1f, 0f, 0f, .5f));
                    } else {
                        if (data[y][x].hasBeenSeen()) {
                            if (data[y][x] instanceof Trap) {
                                g.setColor(new Color(0f, 1f, 0f, .5f));
                            } else if (data[y][x].get() instanceof Monster) {
                                g.setColor(new Color(1f, 1f, 1f, .5f));
                            } else if (data[y][x].get() == null && !data[y][x].solid()) {
                                g.setColor(new Color(1f, 1f, 1f, .5f));
                            } else if (data[y][x].get() instanceof Pickup) {
                                g.setColor(new Color(1f, 0.75f, 0f, .5f));
                            } else {
                                g.setColor(new Color(1f, 1f, 1f, .0f));
                            }
                        } else {
                            g.setColor(new Color(1f, 1f, 1f, .0f));
                        }
                    }
                    g.fillRect(14 + x * per, 14 + y * per, per, per);
                }
            }
        }

        int invside = 32 * 3 + 7;
        player.inventory().draw(g, new Rectangle(getWidth() - 12 - invside, 11, invside, invside));

        if (!player.living()) {
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getWidth() / 16));
            String s = "You have died. Game over.";

            Rectangle2D r2d = g.getFontMetrics().getStringBounds(s, g);
            Rectangle drec = new Rectangle(getWidth() / 2 - (int) r2d.getWidth() / 2,
                    getHeight() / 2 + (int) r2d.getHeight() / 3, 0, 0);

            GraphicsUtils.drawTextOutlined(g, drec, s, Color.RED, Color.BLACK);
        }
    }

    @Override
    public void hook(Game game) {
        this.game = game;
        repaint();
    }
}
