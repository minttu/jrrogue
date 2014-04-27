package me.pieso.jrrogue.item;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jdk.nashorn.internal.codegen.types.Type;
import me.pieso.jrrogue.core.Game;
import me.pieso.jrrogue.util.GraphicsUtils;
import me.pieso.jrrogue.core.ResourceManager;

public class Inventory {

    private final ArrayList<Item> items;
    private final int maxitems;
    public static final char[] letters = {'q', 'w', 'e', 'a', 's', 'd', 'z', 'x', 'c'};

    public Inventory() {
        this.items = new ArrayList<>();
        this.maxitems = 9;
    }

    public Item findLinked(Class cls) {
        for (Item i : items) {
            if (i.linked() == cls) {
                return i;
            }
        }
        return null;
    }

    public Item find(String name) {
        for (Item i : items) {
            if (i.name().equals(name)) {
                return i;
            }
        }
        return null;
    }

    public Item findClass(Class cls) {
        for (Item i : items) {
            if (i.getClass() == cls) {
                return i;
            }
        }
        return null;
    }

    public boolean add(Item it) {
        if (it == null) {
            return false;
        }
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                i.add(it.amount());
                return true;
            }
        }
        if (items.size() + 1 < maxitems) {
            items.add(it);
            return true;
        }
        return false;
    }

    public boolean remove(Item it) {
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                i.remove(it.amount());
                return true;
            }
        }
        return false;
    }

    public boolean remove(int i, int amount) {
        if (i + 1 > items.size()) {
            return false;
        }
        Item it = items.get(i);

        if (it.amount() >= amount) {
            it.remove(amount);
            return true;
        }
        return false;
    }

    public boolean contains(Item it) {
        for (Item i : items) {
            if (i.getClass().isInstance(it)) {
                return true;
            }
        }
        return false;
    }

    public void checkUnloads(Game game) {
        for (Item it : items) {
            if (it.ready()) {
                it.unloadLinked(game);
            }
        }
    }

    public void draw(Graphics g, Rectangle rec) {
        int single = (rec.width - 6) / 3;
        int height = (rec.height - 4) / 3 * (items.size() / 3 + 1) + 3;
        for (int x = rec.x - rec.width; x < rec.x; x += 32) {
            g.drawImage(ResourceManager.getImage("bdown"), x, rec.y - height - 10, null);
        }
        for (int y = rec.y - height; y < rec.y; y += 32) {
            g.drawImage(ResourceManager.getImage("bright"), rec.x - rec.width - 10, y, null);
        }
        g.drawImage(ResourceManager.getImage("cdr"), rec.x - 12, rec.y - 12 - height, null);
        g.drawImage(ResourceManager.getImage("cdr"), rec.x - 12 - rec.width, rec.y - 12, null);
        g.drawImage(ResourceManager.getImage("cdro"), rec.x - 12 - rec.width, rec.y - 12 - height, null);
        g.setColor(new Color(0f, 0f, 0f));
        rec.x -= rec.width;
        rec.y -= height;
        g.fillRect(rec.x, rec.y, rec.width, height);
        for (int i = 0; i < items.size(); i++) {
            int row = i / 3;
            int col = i % 3;
            Rectangle nrec = new Rectangle(
                    rec.x + (single + 2) * col + 2,
                    rec.y + (single + 2) * row + 2,
                    single, single);

            items.get(i).draw(g, nrec);
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            GraphicsUtils.drawTextOutlined(g,
                    new Rectangle(nrec.x + 1, nrec.y + 10, 0, 0),
                    "" + letters[i], Color.WHITE, Color.BLACK);
        }
    }
}
