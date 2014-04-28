package me.pieso.jrrogue.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private int per;
    private boolean bbb;

    public GameArea() {
        super.setBackground(Color.WHITE);
        bbb = true;
    }

    public int offsetX() {
        return lastoffsetx;
    }

    public int offsetY() {
        return lastoffsety;
    }

    private void minimap(Graphics g) {
        Floor[][] data = (Floor[][]) game.getData();
        Player player = game.getPlayer();
        int hei = (getHeight() / 4 / data.length) * data.length;

        per = Math.min(
                Math.max(1, getWidth() / 4 / data[0].length),
                Math.max(1, getHeight() / 4 / data.length)
        );

        for (int x = (per * data[0].length); x > 0; x -= 32) {
            g.drawImage(ResourceManager.getImage("bdown"), x - 32, getHeight() - hei - 10, null);
        }
        for (int y = getHeight() - hei; y < getHeight(); y += 32) {
            g.drawImage(ResourceManager.getImage("bleft"), per * data[0].length, y, null);
        }
        g.drawImage(ResourceManager.getImage("cdl"), 0, getHeight() - 12 - hei, null);
        g.drawImage(ResourceManager.getImage("cdl"), per * data[0].length, getHeight() - 12, null);
        g.drawImage(ResourceManager.getImage("cdlo"), per * data[0].length, getHeight() - 12 - hei, null);

        g.setColor(new Color(0f, 0f, 0f, 1f));
        g.fillRect(0, getHeight() - hei, per * data[0].length, per * data.length);

        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                if (data[y][x] != null) {
                    if (data[y][x].hasBeenSeen() && !data[y][x].solid()) {
                        g.setColor(new Color(1f, 1f, 1f, .5f));
                        g.fillRect(x * per, getHeight() - hei + y * per, per, per);
                    }
                }
            }
        }
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[y].length; x++) {
                if (data[y][x] != null) {
                    if (player.x() == x && player.y() == y) {
                        g.setColor(new Color(1f, 0f, 0f));
                    } else {
                        if (data[y][x].lightLevel() > 0) {
                            if (data[y][x] instanceof Trap) {
                                g.setColor(new Color(0f, 1f, 0f));
                            } else if (data[y][x].get() instanceof Monster) {
                                g.setColor(new Color(0f, 0f, 1f));
                            } else if (data[y][x].get() instanceof Pickup) {
                                g.setColor(new Color(1f, 0.75f, 0f));
                            }
                        } else {
                            g.setColor(new Color(0f, 0f, 0f, 0f));
                        }
                    }
                    g.fillRoundRect((x - 1) * per, getHeight() - hei + (y - 1) * per, per * 3, per * 3, per, per);
                    g.setColor(new Color(0f, 0f, 0f, 0f));
                }
            }
        }
    }

    private void stats(Graphics g) {
        Player player = game.getPlayer();

        int rows = 5;

        int width = per * 128;
        int height = per * 16 * rows;
        int x = (getWidth() - width) / 2;
        int y = getHeight() - height;

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, per * 12));

        for (int xx = x; xx < x + width; xx += 32) {
            g.drawImage(ResourceManager.getImage("bdown"), xx, y - 10, null);
        }
        for (int yy = y; yy < y + height; yy += 32) {
            g.drawImage(ResourceManager.getImage("bright"), x - 10, yy, null);
            g.drawImage(ResourceManager.getImage("bleft"), x + width, yy, null);
        }
        g.drawImage(ResourceManager.getImage("cdr"), x - 10, y + height - 10, null);
        g.drawImage(ResourceManager.getImage("cdl"), x + width - 10, y + height - 10, null);

        g.drawImage(ResourceManager.getImage("cdro"), x - 12, y - 12, null);
        g.drawImage(ResourceManager.getImage("cdlo"), x + width, y - 12, null);

        g.setColor(Color.black);
        g.fillRect(x, y, width, height);

        int[] vals = new int[]{
            (int) (((double) width / (double) player.maxhp()) * ((double) player.hp())),
            (int) (((double) width / (double) player.maxXP()) * ((double) player.XP())),
            (int) (((double) width) * (double) (1 - player.hunger())),
            0, 0
        };
        Color[] cols = new Color[]{
            Color.red,
            Color.blue,
            Color.yellow,
            Color.white,
            Color.white
        };
        for (int i = 0; i < vals.length; i++) {
            g.setColor(new Color(1f, 1f, 1f, 0.25f));
            g.fillRect(x, y + height / rows * i, width, height / rows);
            g.setColor(cols[i]);
            g.fillRect(x, y + height / rows * i, vals[i], height / rows);
            g.setColor(new Color(1f, 1f, 1f, 0.5f));
            g.fillRect(x, y + height / rows * i, vals[i], (height / rows) / 3);
            g.setColor(Color.black);
            g.drawRect(x, y + height / rows * i, width, height / rows);
        }

        String[] msgs = new String[]{
            "HP " + player.hp() + "/" + player.maxhp(),
            "(LVL " + player.level() + ") XP " + player.XP() + "/" + player.maxXP(),
            String.format("Fullness %d%%", 100 - (int) (player.hunger() * 100)),
            "DMG " + (player.dmg().min() + player.dmg().max()) / 2 + "  ACC " + (int) (player.hitrate() * 100) + "%",
             player.dungeon() + ". dungeon "
        };
        for (int i = 1; i <= msgs.length; i++) {
            Rectangle2D r2d = g.getFontMetrics().getStringBounds(msgs[i - 1], g);
            Rectangle drec = new Rectangle(getWidth() / 2 - (int) r2d.getWidth() / 2,
                    y + height / rows * i - (per * 4), 0, 0);
            GraphicsUtils.drawTextOutlined(g, drec, msgs[i - 1], Color.WHITE, Color.BLACK);
        }
    }

    private void msgs(Graphics g) {
        String raw = game.getPlayer().toStatus();
        List<String> all = new ArrayList<>();
        all.addAll(Arrays.asList(raw.split("\n")));
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (14) * per));
        for (int i = 0; i < all.size(); i++) {
            String s = all.get(i);
            Color col = Color.WHITE;
            if (s.contains("@")) {
                int cur = Integer.parseInt(s.split("@")[0]);
                s = s.split("@")[1];
                if (game.getPlayer().lret() - cur > 1) {
                    col = new Color(.75f, .75f, .75f);
                }
            }
            GraphicsUtils.drawTextOutlined(g, new Rectangle(12, 8 + ((i + 1) * ((14) * per + 2)), 0, 0), s, col, Color.BLACK);
        }
        if (game.typing) {
            //bbb = !bbb;
            String s = game.type;
            String ss = ":" + s.substring(0, game.typing_caret) + (bbb ? "|" : " ") + s.substring(game.typing_caret);
            //g.setColor(Color.WHITE);
            //g.drawString(ss, 12, 8 + ((11) * ((12) * per + 2)));
            GraphicsUtils.drawTextOutlined(g, new Rectangle(12, 8 + ((11) * ((14) * per + 2)), 0, 0), ss, Color.WHITE, Color.BLACK);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        side = game.getSide();

        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        Floor[][] data = (Floor[][]) game.getData();
        Player player = game.getPlayer();
        int hei = (getHeight() / 4 / data.length) * data.length;

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

        // minimap
        minimap(g);
        stats(g);
        msgs(g);

        int invside = (32 * per) * 3 + 7;
        player.inventory().draw(g, new Rectangle(getWidth(), getHeight(), invside, invside));

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
