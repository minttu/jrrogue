package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.util.GraphicsUtils;
import me.pieso.jrrogue.core.ResourceManager;
import me.pieso.jrrogue.entity.Floor;
import me.pieso.jrrogue.entity.pickup.Pickup;

public abstract class Item {

    private BufferedImage img;
    private int amount;
    protected boolean showAmount;
    private String name;
    private Class link;
    private boolean ready;

    public Item(String name, BufferedImage img, int amount) {
        this.name = name;
        this.img = img;
        this.amount = amount;
        this.showAmount = true;
        this.link = null;
        this.ready = false;
    }

    public Item(String name, BufferedImage img) {
        this(name, img, 0);
    }

    public BufferedImage image() {
        return img;
    }

    public void setImage(BufferedImage img) {
        this.img = img;
    }

    public boolean ready() {
        return ready;
    }

    public Class linked() {
        return link;
    }

    public void linkPickup(Class p) {
        link = p;
    }

    public void unlickPickup() {
        link = null;
    }

    public void unloadLinked(Game game) {
        ready = false;
        Pickup pick;
        try {
            pick = (Pickup) link.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            return;
        }
        if (pick != null) {
            List<Floor> possible = game.getNeighbours(game.getPlayer().x(), game.getPlayer().y());
            while (true) {
                int len = possible.size();
                if (len == 0) {
                    add(1);
                    return;
                }
                int pos = new Random().nextInt(len);
                if (!possible.get(pos).set(pick)) {
                    possible.remove(pos);
                } else {
                    break;
                }
            }
            game.addLiving(pick);
            game.getPlayer().addStatus("You dropped", name());
        }
    }

    public String name() {
        return name;
    }

    public int amount() {
        return amount;
    }

    public void remove(int i) {
        this.amount = Math.max(0, amount - i);
        if (link != null) {
            ready = true;
        }
    }

    public void remove() {
        remove(1);
    }

    public void add(int i) {
        this.amount += i;
    }

    public void add() {
        add(1);
    }

    public void draw(Graphics g, Rectangle rec) {
        g.setColor(ResourceManager.DGRAY);
        g.fillRect(rec.x, rec.y, rec.width, rec.height);
        g.drawImage(img, rec.x, rec.y, rec.width, rec.height, null);
        if (amount == 0) {
            g.setColor(new Color(0f, 0f, 0f, 0.25f));
            g.fillRect(rec.x, rec.y, rec.width, rec.height);
        }
        if (this.showAmount) {
            String s = amount + "";
            Rectangle2D r2d = g.getFontMetrics().getStringBounds(s, g);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            GraphicsUtils.drawTextOutlined(g,
                    new Rectangle((int) (rec.x + rec.width - r2d.getWidth() - 2), rec.y + rec.height - 2, 0, 0),
                    s, Color.white, Color.black);
        }
    }

    public void setAmount(int i) {
        amount = i;
    }

}
